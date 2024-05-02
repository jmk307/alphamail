package com.osanvalley.moamail.domain.mail.naver.util;

import com.osanvalley.moamail.domain.mail.naver.dto.PostNaverMailSendRequestDto;
import com.osanvalley.moamail.domain.mail.util.SmtpMailConnector;
import com.osanvalley.moamail.global.error.ErrorCode;
import com.osanvalley.moamail.global.error.exception.BadRequestException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class NaverSmtpMailConnector implements SmtpMailConnector {

    private String emailAddress;
    private String password;
    private final String host = "smtp.naver.com";
    private final String port = "587";

    public NaverSmtpMailConnector(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }

    @Override
    public Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.starttls.required", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, password);
            }
        });
    }

    @Override
    public void sendMessage(Session session, PostNaverMailSendRequestDto reqDto) throws MessagingException {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailAddress));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(reqDto.getTo()));
            message.setSubject(reqDto.getTitle());
            message.setText(reqDto.getContent());

            Transport.send(message);
        } catch (AuthenticationFailedException e) {
            throw new BadRequestException(ErrorCode.LOGIN_FAILED);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
