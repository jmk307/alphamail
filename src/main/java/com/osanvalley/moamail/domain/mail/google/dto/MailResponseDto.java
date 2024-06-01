package com.osanvalley.moamail.domain.mail.google.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.mail.model.Readable;
import com.osanvalley.moamail.domain.member.model.Social;

import com.osanvalley.moamail.global.util.CustomLocalDateTimeSerializer;
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

    private Readable hasRead;

    private String alias;

    private Social social;

    private String title;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime sendDate;

    @Builder
    public MailResponseDto(Long id, Readable hasRead, String alias, Social social, String title, LocalDateTime sendDate) {
        this.id = id;
        this.hasRead = hasRead;
        this.alias = alias;
        this.social = social;
        this.title = title;
        this.sendDate = sendDate;
    }

    public static MailResponseDto of(Mail mail) {
        return MailResponseDto.builder()
            .id(mail.getId())
            .hasRead(mail.getHasRead())
            .alias(mail.getAlias())
            .social(mail.getSocial())
            .title(mail.getTitle())
            .sendDate(mail.getSendDate())
            .build();
    }
}
