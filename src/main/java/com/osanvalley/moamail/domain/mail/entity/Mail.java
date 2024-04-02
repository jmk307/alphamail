package com.osanvalley.moamail.domain.mail.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

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

    @OneToMany(mappedBy = "mail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ToEmailReceiver> toEmailReceivers;

    @OneToMany(mappedBy = "mail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CCEmailReceiver> ccEmailReceivers;

    private String content;

    private String historyId;

    @Builder
    public Mail(UUID id, SocialMember socialMember, Social social, String title, String fromEmail, 
            List<ToEmailReceiver> toEmailReceivers, List<CCEmailReceiver> ccEmailReceivers, String content, String historyId) {
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
