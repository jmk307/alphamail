package com.osanvalley.moamail.domain.mail.util;

import com.osanvalley.moamail.domain.mail.naver.dto.PostNaverMailSendRequestDto;

import javax.mail.MessagingException;
import javax.mail.Session;

public interface SmtpMailConnector {
    public Session connect();
    public void sendMessage(Session session, PostNaverMailSendRequestDto reqDto) throws MessagingException;
}
