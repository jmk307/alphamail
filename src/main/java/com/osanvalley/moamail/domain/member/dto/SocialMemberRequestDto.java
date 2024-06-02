package com.osanvalley.moamail.domain.member.dto;

import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.Social;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialMemberRequestDto {
    private String nickname;

    private String email;

    private String profileImgUrl;

    private String provider;

    private String socialId;

    private String googleAccessToken;

    private String googleRefreshToken;

    public static SocialMember socialMemberToEntity(Social social, Member member, SocialMemberRequestDto socialMemberRequestDto) {
        return SocialMember.builder()
                .socialId(socialMemberRequestDto.getSocialId())
                .email(socialMemberRequestDto.getEmail())
                .profileImgUrl(socialMemberRequestDto.getProfileImgUrl())
                .social(social)
                .googleAccessToken(socialMemberRequestDto.getGoogleAccessToken())
                .googleRefreshToken(socialMemberRequestDto.getGoogleRefreshToken())
                .member(member)
            .build();
    }
}
