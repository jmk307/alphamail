package com.osanvalley.moamail.domain.member.entity;

import java.util.ArrayList;
import java.util.List;

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

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.member.model.Social;
import com.osanvalley.moamail.global.config.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialMember extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32)
    private String socialId;

    private String imapPassword;

    private String googleAccessToken;

    private String googleRefreshToken;

    private String email;

    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private Social social;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "socialMember", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mail> mails = new ArrayList<>();

    @Builder
    public SocialMember(Long id, String socialId, String imapPassword, String googleAccessToken, String googleRefreshToken, String email, String profileImgUrl, Social social, Member member, List<Mail> mails) {
        this.id = id;
        this.socialId = socialId;
        this.imapPassword = imapPassword;
        this.googleAccessToken = googleAccessToken;
        this.googleRefreshToken = googleRefreshToken;
        this.email = email;
        this.profileImgUrl = profileImgUrl;
        this.social = social;
        this.member = member;
        this.mails = mails;
    }

    public void updateGoogleAccessToken(String googleAccessToken) {
        this.googleAccessToken = googleAccessToken;
    }
}
