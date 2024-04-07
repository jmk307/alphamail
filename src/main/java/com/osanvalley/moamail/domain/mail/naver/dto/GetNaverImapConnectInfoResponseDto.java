package com.osanvalley.moamail.domain.mail.naver.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class GetNaverImapConnectInfoResponseDto {
    @ApiModelProperty(value = "이메일 주소", example = "wo8934@naver.com")
    @NonNull
    @Size(max = 30)
    private String emailAddress;
}
