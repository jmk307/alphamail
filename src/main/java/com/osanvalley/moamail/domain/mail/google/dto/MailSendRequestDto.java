package com.osanvalley.moamail.domain.mail.google.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailSendRequestDto {
    private String toEmail;

    private String fromEmail;

    private String subject;

    private String bodyText;
}
