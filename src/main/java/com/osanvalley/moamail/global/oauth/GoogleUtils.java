package com.osanvalley.moamail.global.oauth;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.mail.repository.MailBatchRepository;
import com.osanvalley.moamail.domain.mail.repository.MailRepository;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.Social;
import com.osanvalley.moamail.domain.member.repository.SocialMemberRepository;
import com.osanvalley.moamail.global.error.ErrorCode;
import com.osanvalley.moamail.global.error.exception.BadRequestException;
import com.osanvalley.moamail.global.oauth.dto.GmailListResponseDto;
import com.osanvalley.moamail.global.oauth.dto.GmailListResponseDto.Message;
import com.osanvalley.moamail.global.oauth.dto.GmailResponseDto;
import com.osanvalley.moamail.global.oauth.dto.GmailResponseDto.MessagePart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GoogleUtils {
    private final MailRepository mailRepository;
    private final SocialMemberRepository socialMemberRepository;
    private final MailBatchRepository mailBatchRepository;
    private final WebClient webClient;

    @Transactional(readOnly = true)
    public GmailResponseDto showGmailMessage(String accessToken, String messageId) {
        return getGmailMessage(accessToken, messageId);
    }

    @Transactional(readOnly = true)
    public GmailListResponseDto showGmailMessages(String accessToken, String nextPageToken) {
        return getGmailMessages(accessToken, nextPageToken);
    }

    @Transactional
    public String saveGmails(String accessToken, String nextPageToken) {
        long beforeTime = System.currentTimeMillis();
        SocialMember socialMember = socialMemberRepository.findBySocialId("107330656787791997842")
                .orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));

        String pageToken = nextPageToken;
        int count = 0;
        while (true) {
            System.out.println(pageToken);
            GmailListResponseDto gmailList = getGmailMessages(accessToken, pageToken);
            if (gmailList == null) {
                break;
            }
            List<String> messageIds = gmailList.messages.stream()
                .map(Message::getId)
                .collect(Collectors.toList());
            count += messageIds.size();
            
            // 각 이메일 ID에 대한 병렬 요청 생성
            Flux.fromIterable(messageIds)
                .parallel() // 병렬 실행을 위해 Flux를 병렬 스트림으로 변환
                .runOn(Schedulers.parallel()) // 병렬 스트림에서 실행을 병렬 스케줄러로 지정
                .flatMap(messageId -> createIndividualRequest(accessToken, messageId)) // 각각의 요청을 병렬적으로 실행
                .sequential() // 결과를 병렬 스트림에서 순차적으로 처리하도록 변환
                .collectList() // 모든 Gmail을 리스트로 수집
                .doOnNext(gmails -> batchSaveGmails(socialMember, gmails)) // 저장소에 모든 Gmail을 저장
                .block(); // 모든 작업이 완료될 때까지 블록
            
            System.out.println("메일 bulk insert 성공...!");
            System.out.println(count);
            pageToken = gmailList.getNextPageToken();
            if (count == 2000) {
                break;
            }
        }

        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime) / 1000;
        System.out.println("시간차이(m) : " + secDiffTime);
        
        return "성공..!!";
    }

    public void batchSaveGmails(SocialMember socialMember, List<GmailResponseDto> gmails) {
        List<Mail> mails = new ArrayList<>();

        for (GmailResponseDto gmail : gmails) {
            MessagePart payload = gmail.getPayload();

            String title = filterPayLoadByKeyWord(payload, "Subject");
            String fromEmail = filterPayLoadByKeyWord(payload, "From");

            String rawToEmails = filterPayLoadByKeyWord(payload, "To");
            String rawCcEmails = filterPayLoadByKeyWord(payload, "Cc");
            String filterToEmails = filterCcAndToEmails(rawToEmails);
            String filterCCEmails = filterCcAndToEmails(rawCcEmails);

            String content = decodingBase64Url(filterContentAndHtml(payload, "text/plain"));
            String html = decodingBase64Url(filterContentAndHtml(payload, "text/html"));
            String historyId = gmail.getHistoryId();

            Mail mail = Mail.builder()
                .socialMember(socialMember)
                .social(Social.GOOGLE)
                .title(title)
                .fromEmail(fromEmail)
                .toEmailReceivers(filterToEmails)
                .ccEmailReceivers(filterCCEmails)
                .content(content)
                .html(html)
                .historyId(historyId)
                .build();
            mails.add(mail);
        }
        
        mailBatchRepository.saveAll(mails);
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
                .onStatus(status -> status.is4xxClientError(), response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(GmailResponseDto.class); // GmailResponseDto를 반환하는 요청
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadRequestException(ErrorCode.GOOGLE_BAD_REQUEST);
        }
        
    }
}
