package com.osanvalley.moamail.domain.mail.naver.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class PostNaverMailSendResponseDto {
    @ApiModelProperty(value = "수신자", example = "wo8934@naver.com")
    @NotNull
    private String to;
}
