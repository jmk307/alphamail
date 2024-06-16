package com.osanvalley.moamail.mail;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImapsMailDemoApp {
        /**
         * 정보 : InteliJ의 VM Options을 별도로 설정해야 동작하는 데모 앱입니다. <br/>
         * VM Options 값 : -Ddemoapp.mail.userEmail="네이버ID" -Ddemoapp.mail.password="패스워드"
         */
    public static void main(String[] args) throws MessagingException, IOException {
        String userEmail = System.getProperty("demoapp.mail.userEmail");
        String password = System.getProperty("demoapp.mail.password");

        ImapsMailService imapsMailService = new ImapsMailService(userEmail, password);
        System.out.println("네이버 메일 서비스와 연결을 수립합니다.");
        Folder inbox = imapsMailService.connect();

        Message[] msgArray = imapsMailService.getMessages(false);
        int mailContentCount = 5;
        System.out.println("msg = " + msgArray[0]);
        for(int i = msgArray.length - 1; i >= msgArray.length - mailContentCount; i--) {
            //        제목, 발신자, 수신자(리스트), 참조자(리스트), 컨텐츠, 히스토리ID
            Message msg = msgArray[i];
            System.out.printf("메일제목: %s%n", msg.getSubject());
            System.out.print("발신자: ");
            for(Address address : msg.getFrom()) {
                System.out.print(convertToStringGetAddress(address) + " ");
            }
            System.out.println();

            System.out.print("수신자: ");
            Address[] TO = msg.getRecipients(Message.RecipientType.TO);
            if(TO != null) {
                for (Address address : TO) {
                    System.out.print(convertToStringGetAddress(address) + " ");
                }
            }
            System.out.println();

            System.out.print("참조자: ");
            Address[] CC = msg.getRecipients(Message.RecipientType.CC);
            if(CC != null) {
                for (Address address : CC) {
                    System.out.print(convertToStringGetAddress(address) + " ");
                }
            }
            System.out.println();

            String contentType = msg.getContentType().split(";")[0];
            if(contentType.equals("multipart/alternative")) {
                System.out.printf("메일내용: %s%n", convertToHTML((MimeMultipart) msg.getContent()));
                System.out.println("multipart -> html 전환 함수 호출!");
            } else {
                System.out.printf("메일내용: %s%n", msg.getContent());
            }

            long messageUID = ((UIDFolder) inbox).getUID(msg);
            System.out.println("messageUID = " + messageUID);

            System.out.println("==================================");
        }

        System.out.println("네이버 메일 서비스와 연결을 종료합니다.");
        imapsMailService.disconnect();
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

class ImapsMailService {
    private Session session;
    private Store store;

    private Folder folder;
    private String protocol = "imaps";
    private String file = "INBOX";
    private String username;
    private String password;
    private final int port = 993;
    private final String host = "imap.naver.com";

    public ImapsMailService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 정보 : 네이버 메일 서버 연결
     * @throws MessagingException
     */

    public Folder connect() throws MessagingException {
        URLName url = new URLName(protocol, host, port, file, username, password);
        if(session == null) {
            Properties props = null;
            try {
                props = System.getProperties();
            } catch (SecurityException securityException) {
                props = new Properties();
            }
            session = Session.getInstance(props);
        }
        store = session.getStore(url);
        store.connect();
        folder = store.getFolder("inbox");

        folder.open(Folder.READ_ONLY);
        
        return folder;
    }

    /**
     * 정보 : 네이버 메일 서버 로그아웃
     * @throws MessagingException
     */
    public void disconnect() throws MessagingException {
        folder.close(false);
        store.close();
        store = null;
        session = null;
    }

    /**
     * 정보 : 이메일 리스트 가져오기
     * @param onlyNotRead false : 읽은 메시지까지 가져오기 / true : 안 읽은 메시지만 불러오기
     * @return Message[] : 메일 컨텐츠 묶음
     * @throws MessagingException
     */
    public Message[] getMessages(boolean onlyNotRead) throws MessagingException {
        if (onlyNotRead) {
            return folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        }
        else {
            return folder.getMessages();
        }
    }

}

