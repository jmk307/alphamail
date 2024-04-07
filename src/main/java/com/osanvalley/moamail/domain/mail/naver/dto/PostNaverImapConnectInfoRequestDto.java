package com.osanvalley.moamail.domain.mail.naver.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class PostNaverImapConnectInfoRequestDto {
    @ApiModelProperty(value = "소셜 ID", example = "107330656787791997843")
    @NonNull
    private String socialId;

    @ApiModelProperty(value = "이메일 주소", example = "wo8934@naver.com")
    @NonNull
    @Size(max = 30)
    private String emailAddress;

    @ApiModelProperty(value = "비밀번호", example = "a12345678@")
    @NonNull
    private String password;
}
