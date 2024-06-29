package com.osanvalley.moamail.domain.member.dto;

import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.Social;

import lombok.*;

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

    @Builder
    public SocialMemberRequestDto(String nickname, String email, String profileImgUrl, String provider, String socialId, String googleAccessToken, String googleRefreshToken) {
        this.nickname = nickname;
        this.email = email;
        this.profileImgUrl = profileImgUrl;
        this.provider = provider;
        this.socialId = socialId;
        this.googleAccessToken = googleAccessToken;
        this.googleRefreshToken = googleRefreshToken;
    }

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
