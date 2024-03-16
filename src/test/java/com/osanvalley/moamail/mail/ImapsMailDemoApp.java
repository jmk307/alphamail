package com.osanvalley.moamail.mail;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.Properties;

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
        imapsMailService.connect();

        Message[] msgArray = imapsMailService.getMessages(false);
        int mailContentCount = 10;
        for(int i = msgArray.length - 1; i >= msgArray.length - mailContentCount; i--) {
            Message msg = msgArray[i];
            System.out.println(String.format("컨텐츠타임: %s", msg.getContentType()));
            System.out.println(String.format("발신자[0]: %s", msg.getFrom()[0]));
            System.out.println(String.format("메일제목: %s", msg.getSubject()));
            System.out.println(String.format("메일내용: %s", msg.getContent()));
            System.out.println("==================================");
        }

        System.out.println("네이버 메일 서비스와 연결을 종료합니다.");
        imapsMailService.disconnect();
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

    public void connect() throws MessagingException {
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

