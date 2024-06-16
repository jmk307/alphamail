package com.osanvalley.moamail.global.imap;

import javax.mail.*;
import java.util.Properties;

public class NaverImapMailConnector implements ImapMailConnector {
    private Session session;
    private Store store;
    private Folder folder;
    private String emailAddress;
    private String password;
    private final String protocol = "imaps";
    private final String file = "INBOX";
    private final int port = 993;
    private final String host = "imap.naver.com";

    public NaverImapMailConnector(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }

    /**
     * 정보 : 네이버 메일 서버 연결
     * @throws MessagingException
     */
    public Folder connect() throws MessagingException {
        URLName url = new URLName(protocol, host, port, file, emailAddress, password);
        if(session == null) {
            Properties props = null;
            try {
                props = System.getProperties();
            } catch (SecurityException securityException) {
                props = new Properties();
            }
            props.setProperty("mail.imaps.ssl.protocols", "TLSv1.2");
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
     * @return Message[] : 메일 컨텐츠 묶음
     * @throws MessagingException
     */
    public Message[] getMessages() throws MessagingException {
        return folder.getMessages();
    }
}
