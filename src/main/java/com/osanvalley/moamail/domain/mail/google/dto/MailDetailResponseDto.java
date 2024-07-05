package com.osanvalley.moamail.domain.mail.google.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.global.util.CustomLocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailDetailResponseDto {
    private Long mailId;

    private String title;

    private String fromEmail;

    private String toEmailReceivers;

    private String ccEmailReceivers;

    private String html;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime sendDate;

    @Builder
    public MailDetailResponseDto(Long mailId, String title, String fromEmail, String toEmailReceivers, String ccEmailReceivers, String html, LocalDateTime sendDate) {
        this.mailId = mailId;
        this.title = title;
        this.fromEmail = fromEmail;
        this.toEmailReceivers = toEmailReceivers;
        this.ccEmailReceivers = ccEmailReceivers;
        this.html = html;
        this.sendDate = sendDate;
    }

    public static MailDetailResponseDto of(Mail mail) {
        return MailDetailResponseDto.builder()
            .mailId(mail.getId())
            .title(mail.getTitle())
            .fromEmail(mail.getFromEmail())
            .toEmailReceivers(mail.getToEmailReceivers())
            .ccEmailReceivers(mail.getCcEmailReceivers())
            .html(mail.getHtml())
            .sendDate(mail.getSendDate())
            .build();
    }
}
