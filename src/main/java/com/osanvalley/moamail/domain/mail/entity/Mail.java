package com.osanvalley.moamail.domain.mail.entity;

import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.Social;
import com.osanvalley.moamail.global.config.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mail extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SocialMember socialMember;

    @Enumerated(EnumType.STRING)
    private Social social;

    private String title;

    private String fromEmail;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> toEmails;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> ccEmails;

    private String content;

    private String historyId;

    @Builder
    public Mail(Long id, SocialMember socialMember, Social social, String title, String fromEmail, List<String> toEmails, List<String> ccEmails, String content, String historyId) {
        this.id = id;
        this.socialMember = socialMember;
        this.social = social;
        this.title = title;
        this.fromEmail = fromEmail;
        this.toEmails = toEmails;
        this.ccEmails = ccEmails;
        this.content = content;
        this.historyId = historyId;
    }
}
