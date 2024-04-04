package com.osanvalley.moamail.domain.mail.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class PostNaverImapConnectInfoRequestDto {
    @ApiModelProperty(value = "이메일 주소", example = "wo8934@naver.com")
    @NotNull
    @Size(max = 30)
    private String emailAddress;

    @ApiModelProperty(value = "비밀번호", example = "a12345678@")
    @NotNull
    private String password;

    @ApiModelProperty(value = "아이디", example = "jhkim8934")
    @NotNull
    private String memberId;
}
