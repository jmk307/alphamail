package com.osanvalley.moamail.global.oauth;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.osanvalley.moamail.domain.mail.MailRepository;
import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.Social;
import com.osanvalley.moamail.domain.member.repository.SocialMemberRepository;
import com.osanvalley.moamail.global.error.ErrorCode;
import com.osanvalley.moamail.global.error.exception.BadRequestException;
import com.osanvalley.moamail.global.oauth.dto.GmailListResponseDto;
import com.osanvalley.moamail.global.oauth.dto.GmailListResponseDto.Message;
import com.osanvalley.moamail.global.oauth.dto.GmailResponseDto;
import com.osanvalley.moamail.global.oauth.dto.GmailResponseDto.Header;
import com.osanvalley.moamail.global.oauth.dto.GmailResponseDto.MessagePart;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleUtils {
    private final MailRepository mailRepository;
    private final SocialMemberRepository socialMemberRepository;
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

        while (getGmailMessages(accessToken, nextPageToken).getNextPageToken() != null) {
            List<Mail> mails = new ArrayList<>();
            List<String> messageIds = getGmailMessages(accessToken, nextPageToken).messages.stream()
                .map(Message::getId)
                .collect(Collectors.toList());

            for (String messageId : messageIds) {
                GmailResponseDto gmail = getGmailMessage(accessToken, messageId);
                System.out.println(gmail.getId());
                MessagePart payload = gmail.getPayload();
                String historyId = gmail.getHistoryId();
                String content = decodingBase64Url(filterContent(payload));
                String rawToEmails = filterReceiveEmails(payload, "To");
                String rawCcEmails = filterReceiveEmails(payload, "Cc");

                Mail mail = Mail.builder()
                    .socialMember(socialMember)
                    .social(Social.GOOGLE)
                    .title(filterPayLoad(payload, "Subject"))
                    .fromEmail(filterPayLoad(payload, "From"))
                    .toEmails(filterCcAndToEmails(rawToEmails))
                    .ccEmails(filterCcAndToEmails(rawCcEmails))
                    .content(content)
                    .historyId(historyId)
                    .build();
                mails.add(mail);
            }
            mailRepository.saveAll(mails);
        }
        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime) / 1000;
        System.out.println("시간차이(m) : "+secDiffTime);
        
        return "성공..!!";
    }

    public List<String> filterCcAndToEmails(String rawEmails) {
        List<String> filterEmails = new ArrayList<>();
        String patternEmail = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";

        Pattern e = Pattern.compile(patternEmail);
        Matcher m = e.matcher(rawEmails);

        while (m.find()) {
            String email = m.group();
            filterEmails.add(email);
        }

        return filterEmails;
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

    public String filterContent(MessagePart payload) {
        if (payload.getParts().size() == 0) {
            return payload.getBody().getData();
        } else if (payload.getParts().size() != 0) {
            return payload.getParts().stream()
                    .filter(c -> c.getPartId().equals("0"))
                    .findFirst().get()
                    .getBody().getData();
        } else {
            return null;
        }
    }

    public String filterReceiveEmails(MessagePart payload, String filterKeyword) {
        return payload.getHeaders().stream()
                .filter(c -> c.getName().equals(filterKeyword))
                .findFirst().get()
                .getValue();
    }

    public String filterPayLoad(MessagePart payload, String filterKeyword) {
        return payload.getHeaders().stream()
                .filter(f -> f.getName().equals(filterKeyword))
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
}
