package com.osanvalley.moamail.domain.member.dto;

import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.Social;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImapAccountRequestDto {
    private String imapAccount;
    private String imapPassword;

    public static SocialMember socialMember_ImapAccount_ToEntity(String socialId, Member member, String imapAccount, String encryptedPassword, Long lastStoredMsgUID) {
        return SocialMember.builder()
                .socialId(socialId)
                .email(imapAccount + "@naver.com")
                .social(Social.IMAP_NAVER)
                .imapAccount(imapAccount)
                .imapPassword(encryptedPassword)
                .lastStoredMsgUID(lastStoredMsgUID)
                .member(member)
            .build();
    }
}
