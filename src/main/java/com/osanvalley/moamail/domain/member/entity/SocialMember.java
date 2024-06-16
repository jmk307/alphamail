package com.osanvalley.moamail.domain.member.entity;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.member.model.Social;
import com.osanvalley.moamail.global.config.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialMember extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32)
    private String socialId;

    private String imapAccount;

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

    @NotNull
    private Long lastStoredMsgUID;

    @Builder
    public SocialMember(Long id, String socialId, String imapAccount, String imapPassword, String googleAccessToken, String googleRefreshToken, String email, String profileImgUrl, Social social, Member member, List<Mail> mails, Long lastStoredMsgUID) {
        this.id = id;
        this.socialId = socialId;
        this.imapAccount = imapAccount;
        this.imapPassword = imapPassword;
        this.googleAccessToken = googleAccessToken;
        this.googleRefreshToken = googleRefreshToken;
        this.email = email;
        this.profileImgUrl = profileImgUrl;
        this.social = social;
        this.member = member;
        this.mails = mails;
        this.lastStoredMsgUID = lastStoredMsgUID;
    }

    public void updateGoogleAccessToken(String googleAccessToken) {
        this.googleAccessToken = googleAccessToken;
    }

    public void updateImapPassword(String encryptedPassword) {
        this.imapPassword = encryptedPassword;
    }

    public void updateLastStoredMsgUID(Long lastStoredMsgUID) {
        this.lastStoredMsgUID = lastStoredMsgUID;
    }
}
