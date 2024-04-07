package com.osanvalley.moamail.domain.mail.entity;

import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.Social;
import com.osanvalley.moamail.global.config.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mail extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SocialMember socialMember;

    @Enumerated(EnumType.STRING)
    private Social social;

    private String title;

    private String fromEmail;

    @Column(length = 200)
    private String toEmailReceivers;

    @Column(length = 200)
    private String ccEmailReceivers;

    @Column(length = 100000)
    private String content;

    private String historyId;

    @Builder
    public Mail(UUID id, SocialMember socialMember, Social social, String title, String fromEmail, 
            String toEmailReceivers, String ccEmailReceivers, String content, String historyId) {
        this.id = id;
        this.socialMember = socialMember;
        this.social = social;
        this.title = title;
        this.fromEmail = fromEmail;
        this.toEmailReceivers = toEmailReceivers;
        this.ccEmailReceivers = ccEmailReceivers;
        this.content = content;
        this.historyId = historyId;
    }
}
