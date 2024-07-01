package com.osanvalley.moamail.domain.mail.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.osanvalley.moamail.domain.mail.model.Readable;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.Social;
import com.osanvalley.moamail.global.config.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SocialMember socialMember;

    @Enumerated(EnumType.STRING)
    private Social social;

    private String title;

    private String alias;

    private String fromEmail;

    @Column(columnDefinition = "TEXT")
    private String toEmailReceivers;

    @Column(columnDefinition = "TEXT")
    private String ccEmailReceivers;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(columnDefinition = "LONGTEXT")
    private String html;

    @Enumerated(EnumType.STRING)
    private Readable hasRead;

    private String historyId;

    private LocalDateTime sendDate;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean isSpam;

    @Builder
    public Mail(Long id, SocialMember socialMember, Social social, String title, String alias, String fromEmail,
            String toEmailReceivers, String ccEmailReceivers, String content, String html, Readable hasRead, String historyId, LocalDateTime sendDate, boolean isSpam) {
        this.id = id;
        this.socialMember = socialMember;
        this.social = social;
        this.title = title;
        this.alias = alias;
        this.fromEmail = fromEmail;
        this.toEmailReceivers = toEmailReceivers;
        this.ccEmailReceivers = ccEmailReceivers;
        this.content = content;
        this.html = html;
        this.hasRead = hasRead;
        this.historyId = historyId;
        this.sendDate = sendDate;
        this.isSpam = isSpam;
    }
}
