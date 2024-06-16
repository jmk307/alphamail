package com.osanvalley.moamail.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class SmtpMailDemoApp {

    public static void main(String[] args) throws UnsupportedEncodingException, MessagingException {

        String host = "smtp.naver.com";
        String user = System.getProperty("demoapp.mail.userEmail");
        String password = System.getProperty("demoapp.mail.password");

        // SMTP 서버 정보를 설정한다.
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.starttls.required", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user, "test"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user));

            // 메일 제목
            message.setSubject("SMTPS 587 STARTTLS TEST MAIL");

            // 메일 내용
            message.setText("E-Mail Send Test Success!!");

            // send the message
            Transport.send(message);
            System.out.println("Success Message Send");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}