package com.osanvalley.moamail.domain.mail.google.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.member.model.Social;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailResponseDto {
    private Long id;

    private Social social;

    private String title;

    private String fromEmail;

    private String toEmailReceivers;

    private String ccEmailReceivers;

    private String content;

    private String html;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime sendDate;

    @Builder
    public MailResponseDto(Long id, Social social, String title, String fromEmail, String toEmailReceivers, String ccEmailReceivers, String content, String html, LocalDateTime sendDate) {
        this.id = id;
        this.social = social;
        this.title = title;
        this.fromEmail = fromEmail;
        this.toEmailReceivers = toEmailReceivers;
        this.ccEmailReceivers = ccEmailReceivers;
        this.content = content;
        this.html = html;
        this.sendDate = sendDate;
    }

    public static MailResponseDto of(Mail mail) {
        return MailResponseDto.builder()
            .id(mail.getId())
            .social(mail.getSocial())
            .title(mail.getTitle())
            .fromEmail(mail.getFromEmail())
            .toEmailReceivers(mail.getToEmailReceivers())
            .ccEmailReceivers(mail.getCcEmailReceivers())
            .content(mail.getContent())
            .html(mail.getHtml())
            .sendDate(mail.getSendDate())
            .build();
    }
}
