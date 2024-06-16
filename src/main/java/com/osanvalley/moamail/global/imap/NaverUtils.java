package com.osanvalley.moamail.global.imap;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.mail.model.Readable;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.Social;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NaverUtils {

    private final Folder folder;

    public NaverUtils(Folder folder) {
        this.folder = folder;
    }

    public Mail toMailEntity(Message message, SocialMember socialMember) throws MessagingException, IOException {
        // Title
        String title = message.getSubject();

        // Alias & From
        Address address = message.getFrom()[0];
        String[] splitAliasAndFromAddress = address.toString().split("<");
        String alias = convertToKoreanFromRegexs(splitAliasAndFromAddress[0]);
        String from = splitAliasAndFromAddress.length >= 2 ? splitAliasAndFromAddress[1].substring(0, splitAliasAndFromAddress[1].length() - 1) : splitAliasAndFromAddress[0];

        // TO & CC
        Address[] toAddresses = message.getRecipients(Message.RecipientType.TO);
        StringBuilder to = new StringBuilder();
        if(toAddresses != null) {
            for (Address toAddress : toAddresses) {
                String[] splitToAddress = toAddress.toString().split("<");
                String toEmailReceiver = splitToAddress.length >= 2 ? splitToAddress[1].substring(0, splitToAddress[1].length() - 1) : splitToAddress[0];
                to.append(toEmailReceiver).append(" ");
            }
        }

        Address[] ccAddresses = message.getRecipients(Message.RecipientType.CC);
        StringBuilder cc = new StringBuilder();
        if(ccAddresses != null) {
            for (Address ccAddress : ccAddresses) {
                String[] splitCcAddress = ccAddress.toString().split("<");
                String ccEmailReceiver = splitCcAddress.length >= 2 ? splitCcAddress[1].substring(0, splitCcAddress[1].length() - 1) : splitCcAddress[0];
                to.append(ccEmailReceiver).append(" ");
            }
        }

        // Content && HTML
        String content = message.getContent().toString();

        String contentType = message.getContentType().split(";")[0];
        String contentHtml = contentType.equals("multipart/alternative")
                ? convertToHTML((MimeMultipart) message.getContent())
                : message.getContent().toString();

        // HasReads
        Readable hasRead = message.isSet(Flags.Flag.SEEN) ? Readable.READ : Readable.UNREAD;

        // HistoryId (MessageUid)
        long messageUID = ((UIDFolder) folder).getUID(message);
        socialMember.updateLastStoredMsgUID(messageUID);

        // SendDate
        LocalDateTime sentDate = message.getSentDate().toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

        return Mail.builder()
                .socialMember(socialMember)
                .social(Social.NAVER)
                .title(title)
                .alias(alias)
                .fromEmail(from)
                .toEmailReceivers(to.toString())
                .ccEmailReceivers(cc.toString())
                .content(content)
                .html(contentHtml)
                .hasRead(hasRead)
                .historyId(String.valueOf(messageUID))
                .sendDate(sentDate)
            .build();
    }

    private String convertToKoreanFromRegexs(String before_data) {
        String regex_UTF8 = "(?:\\?utf-8\\?B\\?|\\?UTF-8\\?B\\?)([^?]+)\\?\\s*";
        Pattern pattern_UTF8 = Pattern.compile(regex_UTF8);
        Matcher matcher_UTF8 = pattern_UTF8.matcher(before_data);

        if (matcher_UTF8.find()) {
            String extractedString = matcher_UTF8.group(1);
            byte[] decodedBytes = Base64.getDecoder().decode(extractedString);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        }

        String regex_EUCKR = "(?:\\?euc-kr\\?B\\?|\\?EUC-KR\\?B\\?)([^?]+)\\?\\s*";
        Pattern pattern_EUCKR = Pattern.compile(regex_EUCKR);
        Matcher matcher_EUCKR = pattern_EUCKR.matcher(before_data);

        if (matcher_EUCKR.find()) {
            String extractedString = matcher_EUCKR.group(1);
            byte[] decodedBytes = Base64.getDecoder().decode(extractedString);
            return new String(decodedBytes, Charset.forName("EUC-KR"));
        }

        // EUC-KR & UTF-8 이 아니라면 원본 데이터 추출
        return before_data;
    }
            
    public static String convertToHTML(MimeMultipart multipart) {
        StringBuilder htmlContent = new StringBuilder();

        try {
            int count = multipart.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String contentType = bodyPart.getContentType();

                if (contentType.toLowerCase().contains("text/html")) {
                    htmlContent.append((String) bodyPart.getContent());
                }
            }
        } catch (MessagingException | java.io.IOException e) {
            e.printStackTrace();
        }

        return htmlContent.toString();
    }

}
