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
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailResponseDto {
    private Long id;

    private Boolean isRead;

    private String fromEmail;

    private Social social;

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime sendDate;

    @Builder
    public MailResponseDto(Long id, Boolean isRead, String fromEmail, Social social, String title, LocalDateTime sendDate) {
        this.id = id;
        this.isRead = isRead;
        this.fromEmail = fromEmail;
        this.social = social;
        this.title = title;
        this.sendDate = sendDate;
    }

    public static MailResponseDto of(Mail mail) {
        return MailResponseDto.builder()
            .id(mail.getId())
            .fromEmail(mail.getFromEmail())
            .social(mail.getSocial())
            .title(mail.getTitle())
            .sendDate(mail.getSendDate())
            .build();
    }
}
