package com.osanvalley.moamail.global.oauth;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.osanvalley.moamail.domain.mail.model.Readable;
import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.global.oauth.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.mail.repository.MailBatchRepository;
import com.osanvalley.moamail.domain.mail.repository.MailRepository;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.Social;
import com.osanvalley.moamail.domain.member.repository.SocialMemberRepository;
import com.osanvalley.moamail.global.error.ErrorCode;
import com.osanvalley.moamail.global.error.exception.BadRequestException;
import com.osanvalley.moamail.global.oauth.dto.GmailListResponseDto.Message;
import com.osanvalley.moamail.global.oauth.dto.GmailResponseDto.MessagePart;
import com.osanvalley.moamail.global.util.Date;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class GoogleUtils {
    private static final Logger log = LoggerFactory.getLogger(GoogleUtils.class);
    private final MailRepository mailRepository;
    private final SocialMemberRepository socialMemberRepository;
    private final MailBatchRepository mailBatchRepository;
    private final WebClient webClient;

    @Value("${google.clientId}")
    private String CLIENT_ID;

    @Value("${google.clientSecret}")
    private String CLIENT_SECRET;

    @Value("${google.redirectUri}")
    private String REDIRECT_URI;

    // 구글 정보 가져오기
    @Transactional(readOnly = true)
    public GoogleMemberInfoDto getGoogleMemberInfo(String accessToken) {
        final String googleMemberInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo";

        try {
            return webClient.get()
                .uri(googleMemberInfoUrl)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GoogleMemberInfoDto.class)
            .block();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadRequestException(ErrorCode.GOOGLE_BAD_REQUEST);
        }
    }

    // 구글 어세스토큰 발급
    public GoogleAccessTokenDto getGoogleAccessToken(String code) {
        final String googleTokenUrl = "https://oauth2.googleapis.com/token";
        GoogleAccessTokenDto googleAccessTokenDto;

        try {
            googleAccessTokenDto = webClient.post()
                .uri(googleTokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("code=" + code + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&redirect_uri=" + REDIRECT_URI + "&grant_type=authorization_code")
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(GoogleAccessTokenDto.class)
            .block();

            return googleAccessTokenDto;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadRequestException(ErrorCode.GOOGLE_BAD_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public GmailResponseDto showGmailMessage(String accessToken, String messageId) {
        return getGmailMessage(accessToken, messageId);
    }

    @Transactional(readOnly = true)
    public GmailListResponseDto showGmailMessages(String accessToken, String nextPageToken) {
        GmailListResponseDto gmailListResponseDto = getGmailMessages(accessToken, nextPageToken);

        return getGmailMessages(accessToken, nextPageToken);
    }

    public String saveGmails(SocialMember socialMember, String nextPageToken) {
        long beforeTime = System.currentTimeMillis();
        String pageToken = nextPageToken;
        int count = 0;
        while (true) {
            GmailListResponseDto gmailList = getGmailMessages(socialMember.getGoogleAccessToken(), pageToken);
            if (gmailList == null) {
                break;
            }

            List<String> messageIds = gmailList.messages.stream()
                .map(Message::getId)
                .collect(Collectors.toList());
            count += messageIds.size();

            List<GmailResponseDto> gmails = new ArrayList<>();

            if (messageIds.size() == 100) {
                for (int i = 0; i < messageIds.size(); i += 20) {
                    int start = i;
                    int end = Math.min(i + 20, messageIds.size());

                    List<String> subMessageIds = messageIds.subList(start, end);
                    gmails.addAll(fluxReadGmails(subMessageIds, socialMember.getGoogleAccessToken(), socialMember));
                }
            } else {
                // 각 이메일 ID에 대한 병렬 요청 생성
                gmails.addAll(fluxReadGmails(messageIds, socialMember.getGoogleAccessToken(), socialMember));
            }

            batchSaveGmails(socialMember, gmails);

            pageToken = gmailList.getNextPageToken();
            if (count <= 100 || pageToken == null) {
                break;
            }

            System.out.println("메일 bulk insert 성공...!");
            System.out.println(count);
        }
        
        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime) / 1000;
        System.out.println("시간차이(m) : " + secDiffTime);

        return pageToken;
    }

    @Async("mail")
    public CompletableFuture<Void> saveRemainingGmails(SocialMember socialMember, String nextPageToken) {
        long beforeTime = System.currentTimeMillis();
        String pageToken = nextPageToken;
        int count = 0;
        while (true) {
            GmailListResponseDto gmailList = getGmailMessages(socialMember.getGoogleAccessToken(), pageToken);
            if (gmailList == null) {
                break;
            }

            List<String> messageIds = gmailList.messages.stream()
                    .map(Message::getId)
                    .collect(Collectors.toList());
            count += messageIds.size();

            List<GmailResponseDto> gmails = new ArrayList<>();

            if (messageIds.size() == 100) {
                for (int i = 0; i < messageIds.size(); i += 20) {
                    int start = i;
                    int end = Math.min(i + 20, messageIds.size());

                    List<String> subMessageIds = messageIds.subList(start, end);
                    gmails.addAll(fluxReadGmails(subMessageIds, socialMember.getGoogleAccessToken(), socialMember));
                }
            } else {
                // 각 이메일 ID에 대한 병렬 요청 생성
                gmails.addAll(fluxReadGmails(messageIds, socialMember.getGoogleAccessToken(), socialMember));
            }

            batchSaveGmails(socialMember, gmails);

            pageToken = gmailList.getNextPageToken();
            if (count >= 900 || pageToken == null) {
                break;
            }

            System.out.println("메일 bulk insert 성공...!");
            System.out.println(count);
        }

        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime) / 1000;
        System.out.println("시간차이(m) : " + secDiffTime);

        return CompletableFuture.completedFuture(null);
    }

    public List<GmailResponseDto> fluxReadGmails(List<String> messageIds, String accessToken, SocialMember socialMember) {
        return Flux.fromIterable(messageIds)
                .parallel() // 병렬 실행을 위해 Flux를 병렬 스트림으로 변환
                .runOn(Schedulers.parallel()) // 병렬 스트림에서 실행을 병렬 스케줄러로 지정
                .flatMap(messageId -> createIndividualRequest(accessToken, messageId)) // 각각의 요청을 병렬적으로 실행
                .sequential() // 결과를 병렬 스트림에서 순차적으로 처리하도록 변환
                .collectList() // 모든 Gmail을 리스트로 수집
                // .doOnNext(gmails -> batchSaveGmails(socialMember, gmails)) // 저장소에 모든 Gmail을 저장
                .block(); // 모든 작업이 완료될 때까지 블록
    }

    public void batchSaveGmails(SocialMember socialMember, List<GmailResponseDto> gmails) {
        List<Mail> mails = new ArrayList<>();

        for (GmailResponseDto gmail : gmails) {
            MessagePart payload = gmail.getPayload();

            String mailUniqueId = gmail.getId();
            String title = filterPayLoadByKeyWord(payload, "Subject");

            String rawFromEmail = filterPayLoadByKeyWord(payload, "From");
            String alias;
            String fromEmail;

            Pattern pattern = Pattern.compile("\"?(.*?)\"?\\s*<(.+?)>");
            Matcher matcher = pattern.matcher(rawFromEmail);

            if (matcher.find()) {
                // 별칭과 이메일을 추출합니다.
                alias = matcher.group(1).isEmpty() ? null : matcher.group(1);
                fromEmail = matcher.group(2);
            } else {
                // 별칭이 없는 경우, 이메일만 추출합니다.
                alias = null;
                fromEmail = rawFromEmail;
            }

            String rawToEmails = filterPayLoadByKeyWord(payload, "To");
            String rawCcEmails = filterPayLoadByKeyWord(payload, "Cc");
            String filterToEmails = filterCcAndToEmails(rawToEmails);
            String filterCCEmails = filterCcAndToEmails(rawCcEmails);

            String content = decodingBase64Url(filterContentAndHtml(payload, "text/plain"));
            String html = decodingBase64Url(filterContentAndHtml(payload, "text/html"));
            Readable hasRead = filterHasRead(gmail.getLabelIds());

            String historyId = gmail.getHistoryId();
            String mimeType = gmail.getPayload().getMimeType();
            LocalDateTime sendDate = Date.parseToLocalDateTime(filterSendDate(payload));

            Mail mail = Mail.builder()
                .socialMember(socialMember)
                .mailUniqueId(mailUniqueId)
                .social(Social.GOOGLE)
                .title(title)
                .alias(alias)
                .fromEmail(fromEmail)
                .toEmailReceivers(filterToEmails)
                .ccEmailReceivers(filterCCEmails)
                .content(content)
                .html(html)
                .hasRead(hasRead)
                .historyId(historyId)
                .mimeType(mimeType)
                .sendDate(sendDate)
                .build();
            mails.add(mail);
        }
        
        mailBatchRepository.saveAll(mails);
    }



    public Readable filterHasRead(List<String> labelIds) {
        return labelIds.contains("UNREAD")
                    ? Readable.UNREAD
                    : Readable.READ;
    }

    public String filterCcAndToEmails(String rawEmails) {
        if (rawEmails == null) {
            return null;
        }

        StringBuilder filterEmails = new StringBuilder();
        String patternEmail = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";

        Pattern e = Pattern.compile(patternEmail);
        Matcher m = e.matcher(rawEmails);

        while (m.find()) {
            String email = m.group();
            filterEmails.append(email).append(" ");
        }

        return filterEmails.toString().trim();
    }

    public String decodingBase64Url(String encodedBase64Url) {
        if (encodedBase64Url == null) {
            return null;
        } else {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedBase64Url);
            String decodedStr = new String(decodedBytes);

            return decodedStr;
        }
    }

    public String filterSendDate(MessagePart payload) {
        String rawSendDate = payload.getHeaders().stream()
                .filter(p -> p.getName().equals("Date"))
                .findFirst().get()
                .getValue();
        String trimSendDate = rawSendDate.replaceAll("\\s+", " ");

        return trimSendDate;
    }

    public String filterContentAndHtml(MessagePart payload, String filterKeyword) {
        if (!payload.getMimeType().equals("multipart/alternative")) {
            return payload.getBody().getData();
        } else if (payload.getMimeType().equals("multipart/alternative")) {
            if (payload.getParts().stream().noneMatch(c -> c.getMimeType().equals(filterKeyword))) {
                return null;
            }
            return payload.getParts().stream()
                    .filter(c -> c.getMimeType().equals(filterKeyword))
                    .findFirst().get()
                    .getBody().getData();
        } else {
            return null;
        }
    }

    public String filterPayLoadByKeyWord(MessagePart payload, String filterKeyword) {
        if (payload.getHeaders().stream().noneMatch(p -> p.getName().equals(filterKeyword))) {
            return null;
        }

        return payload.getHeaders().stream()
                .filter(p -> p.getName().equals(filterKeyword))
                .findFirst().get()
                .getValue();
    }

    public String deleteAllGmails() {
        mailRepository.deleteAllInBatch();
        return "성공..!!";
    }

    // Gmail 메시지 하나 가져오기
    public GmailResponseDto getGmailMessage(String accessToken, String messageId) {
        final String gmailUrl = "https://www.googleapis.com/gmail/v1/users/me/messages/{messageId}";

        try {
            return webClient.get()
                .uri(gmailUrl, messageId)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GmailResponseDto.class)
            .block();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadRequestException(ErrorCode.GOOGLE_BAD_REQUEST);
        }
    }

    // Gmail 메시지 리스트 가져오기
    public GmailListResponseDto getGmailMessages(String accessToken, String nextPageToken) {
        final String gmailUrl = "https://www.googleapis.com/gmail/v1/users/me/messages";

        try {
            return webClient.get()
                .uri(gmailUrl, builder -> builder.queryParam("pageToken", nextPageToken).build())
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GmailListResponseDto.class)
            .block();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadRequestException(ErrorCode.GOOGLE_BAD_REQUEST);
        }
    }

    public Flux<GmailResponseDto> batchGetGmails(String accessToken, List<String> messageIds) {
        try {
            return Flux.fromIterable(messageIds)
                .parallel() // 병렬 실행을 위해 Flux를 병렬 스트림으로 변환
                .runOn(Schedulers.parallel()) // 병렬 스트림에서 실행을 병렬 스케줄러로 지정
                .flatMap(messageId -> createIndividualRequest(accessToken, messageId)) // 각각의 요청을 병렬적으로 실행
                .sequential(); // 결과를 병렬 스트림에서 순차적으로 처리하도록 변환
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadRequestException(ErrorCode.GOOGLE_BAD_REQUEST);
        }
        
    }

    private Mono<GmailResponseDto> createIndividualRequest(String accessToken, String messageId) {
        try {
            return webClient.get()
                .uri("https://www.googleapis.com/gmail/v1/users/me/messages/{messageId}", messageId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(GmailResponseDto.class); // GmailResponseDto를 반환하는 요청
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadRequestException(ErrorCode.GOOGLE_BAD_REQUEST);
        }
    }

    // 구글 어세스토큰 재발급
    @Transactional
    public String reissueGoogleAccessToken(Member member) {
        SocialMember socialMember = socialMemberRepository.findByMember_AuthIdAndSocial(member.getAuthId(), Social.GOOGLE)
                .orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));

        final String googleTokenUrl = "https://oauth2.googleapis.com/token";
        GoogleAccessTokenDto googleAccessTokenDto;

        try {
            googleAccessTokenDto =  webClient.post()
                .uri(googleTokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&refresh_token=" + socialMember.getGoogleRefreshToken() + "&grant_type=refresh_token")
                .retrieve()
                .bodyToMono(GoogleAccessTokenDto.class)
            .block();

            socialMember.updateGoogleAccessToken(googleAccessTokenDto.getAccess_token());

            return "토큰 재발급 성공..!";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadRequestException(ErrorCode.GOOGLE_BAD_REQUEST);
        }
    }

    public boolean isGoogleAccessTokenValid(String googleAccessToken) {
        try {
            String tokenInfoUrl = "https://oauth2.googleapis.com/tokeninfo?access_token=" + googleAccessToken;
            webClient.get()
                    .uri(tokenInfoUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return true;
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is4xxClientError() || e.getStatusCode().is5xxServerError()) {
                return false;
            } else {
                throw new BadRequestException(ErrorCode.GOOGLE_BAD_REQUEST);
            }
        }
    }

    public GmailAttachmentResponseDto getAttachmentData(String messageId, String attachmentId, String googleAccessToken) {
        try {
            return webClient.get()
                    .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages/{messageId}/attachments/{id}", messageId, attachmentId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + googleAccessToken)
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response -> response.bodyToMono(String.class).map(Exception::new))
                    .bodyToMono(GmailAttachmentResponseDto.class)
                    .block();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadRequestException(ErrorCode.GOOGLE_BAD_REQUEST);
        }
    }
}
