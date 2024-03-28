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

    private String accessToken;

    @Builder
    public MemberResponseDto(Long id, String authId, String phoneNumber, String nickname, String birth, Sex sex, String accessToken) {
        this.id = id;
        this.authId = authId;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.birth = birth;
        this.sex = sex;
        this.accessToken = accessToken;
    }

    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .authId(member.getAuthId())
                .phoneNumber(member.getPhoneNumber())
                .nickname(member.getNickname())
                .birth(member.getBirth())
                .sex(member.getSex())
        .build();
    }

    public static MemberResponseDto of(Member member, String accessToken) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .authId(member.getAuthId())
                .phoneNumber(member.getPhoneNumber())
                .nickname(member.getNickname())
                .birth(member.getBirth())
                .sex(member.getSex())
                .accessToken(accessToken)
        .build();
    }
}
