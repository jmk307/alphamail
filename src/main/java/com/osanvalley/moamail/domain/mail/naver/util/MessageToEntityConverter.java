package com.osanvalley.moamail.domain.mail.naver.util;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.Social;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageToEntityConverter {
    //        제목, 발신자, 수신자(리스트), 참조자(리스트), 컨텐츠, 히스토리ID
    public Mail toMailEntity(Message message, SocialMember socialMember) throws MessagingException, IOException {
        String title = message.getSubject();

        Address getFromAddress = message.getFrom()[0];
        String from = convertToStringGetAddress(getFromAddress);

        Address[] toAddresses = message.getRecipients(Message.RecipientType.TO);
        StringBuilder to = new StringBuilder();
        if(toAddresses != null) {
            for (Address addr : toAddresses) {
                to.append(convertToStringGetAddress(addr)).append(" ");
            }
        }

        Address[] ccAddresses = message.getRecipients(Message.RecipientType.CC);
        StringBuilder cc = new StringBuilder();
        if(ccAddresses != null) {
            for (Address addr : ccAddresses) {
                cc.append(convertToStringGetAddress(addr)).append(" ");
            }
        }

        String contentType = message.getContentType().split(";")[0];
        String contentHtml = contentType.equals("multipart/alternative")
                ? convertToHTML((MimeMultipart) message.getContent())
                : message.getContent().toString();

//            public Mail(UUID id, SocialMember socialMember, Social social, String title, String fromEmail,
//                String toEmailReceivers, String ccEmailReceivers, String content, String historyId) {

        return Mail.builder()
                .socialMember(socialMember)
                .social(Social.NAVER)
                .title(title)
                .fromEmail(from)
                .toEmailReceivers(to.toString())
                .ccEmailReceivers(cc.toString())
                .content(contentHtml)
                .historyId("")
            .build();
    };

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

    private static String convertToStringGetAddress(Address fromAddress) {
        String[] splitData = fromAddress.toString().split("<");

        try {
            if (splitData.length >= 2) {
                String base64EncodedData = splitData[0];
                String senderEmailAddress = "<" + splitData[1];

                String regex_UTF8 = "(?:\\?utf-8\\?B\\?|\\?UTF-8\\?B\\?)([^?]+)\\?\\s*";
                Pattern pattern_UTF8 = Pattern.compile(regex_UTF8);
                Matcher matcher_UTF8 = pattern_UTF8.matcher(base64EncodedData);

                if (matcher_UTF8.find()) {
                    String extractedString = matcher_UTF8.group(1);
                    byte[] decodedBytes = Base64.getDecoder().decode(extractedString);
                    return new String(decodedBytes, StandardCharsets.UTF_8) + " " + senderEmailAddress;
                }

                String regex_EUCKR = "(?:\\?euc-kr\\?B\\?|\\?EUC-KR\\?B\\?)([^?]+)\\?\\s*";
                Pattern pattern_EUCKR = Pattern.compile(regex_EUCKR);
                Matcher matcher_EUCKR = pattern_EUCKR.matcher(base64EncodedData);

                if (matcher_EUCKR.find()) {
                    String extractedString = matcher_EUCKR.group(1);
                    byte[] decodedBytes = Base64.getDecoder().decode(extractedString);
                    return new String(decodedBytes, Charset.forName("EUC-KR")) + " " + senderEmailAddress;
                }

                return senderEmailAddress;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "<" + fromAddress + ">";
    }
}
