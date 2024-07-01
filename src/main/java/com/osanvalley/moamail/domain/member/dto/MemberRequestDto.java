package com.osanvalley.moamail.domain.member.dto;

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.domain.member.model.RegisterType;
import com.osanvalley.moamail.domain.member.model.Sex;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequestDto {
    @ApiModelProperty(value = "아이디", example = "jimin112688")
    @NotNull
    @Pattern(regexp = "^[a-z0-9]{5,20}$", message = "영어 소문자 숫자 5~20자리")
    private String authId;

    @ApiModelProperty(value = "패스워드", example = "a1234@")
    @NotNull
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{9,16}", message = "숫자 영어 특수문자 합 9~16자리")
    private String password;

    @ApiModelProperty(value = "휴대폰 번호", example = "01063471084")
    @NotNull
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$")
    private String phoneNumber;

    @ApiModelProperty(value = "닉네임", example = "작통(소년)단")
    @NotNull
    @Size(max = 10)
    private String nickname;

    @ApiModelProperty(value = "생년월일", example = "011126")
    @NotNull
    private String birth;

    @ApiModelProperty(value = "성별", example = "MALE")
    @NotNull
    private Sex sex;

    public static Member memberToEntity(String password, MemberRequestDto memberRequestDto) {
        return Member.builder()
                .authId(memberRequestDto.getAuthId())
                .password(password)
                .phoneNumber(memberRequestDto.getPhoneNumber())
                .nickname(memberRequestDto.getNickname())
                .birth(memberRequestDto.getBirth())
                .registerType(RegisterType.COMMON)
                .sex(memberRequestDto.getSex())
        .build();
    }

    public static Member memberToEntity(RegisterType registerType, SocialMemberRequestDto socialMemberRequestDto) {
        String uuid = UUID.randomUUID().toString();

        return Member.builder()
                .authId(uuid)
                .password(uuid)
                .phoneNumber(null)
                .nickname(socialMemberRequestDto.getNickname())
                .birth(null)
                .registerType(registerType)
                .sex(null)
            .build();
    }
}
