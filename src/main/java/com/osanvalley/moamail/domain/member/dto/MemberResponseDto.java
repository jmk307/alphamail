package com.osanvalley.moamail.domain.member.dto;

import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.domain.member.model.Sex;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponseDto {
    private Long id;

    private String authId;
    
    private String phoneNumber;

    private String nickname;

    private String birth;

    private Sex sex;

    private Boolean hasGoogle;

    private Boolean hasNaver;

    private String accessToken;

    @Builder
    public MemberResponseDto(Long id, String authId, String phoneNumber, String nickname, String birth, Sex sex, Boolean hasGoogle, Boolean hasNaver, String accessToken) {
        this.id = id;
        this.authId = authId;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.birth = birth;
        this.sex = sex;
        this.hasGoogle = hasGoogle;
        this.hasNaver = hasNaver;
        this.accessToken = accessToken;
    }

    public static MemberResponseDto of(Member member, Boolean hasGoogle, Boolean hasNaver) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .authId(member.getAuthId())
                .phoneNumber(member.getPhoneNumber())
                .nickname(member.getNickname())
                .birth(member.getBirth())
                .sex(member.getSex())
                .hasGoogle(hasGoogle)
                .hasNaver(hasNaver)
        .build();
    }

    public static MemberResponseDto of(Member member, Boolean hasGoogle, Boolean hasNaver, String accessToken) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .authId(member.getAuthId())
                .phoneNumber(member.getPhoneNumber())
                .nickname(member.getNickname())
                .birth(member.getBirth())
                .sex(member.getSex())
                .hasGoogle(hasGoogle)
                .hasNaver(hasNaver)
                .accessToken(accessToken)
        .build();
    }
}
