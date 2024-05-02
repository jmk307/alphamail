package com.osanvalley.moamail.domain.mail.naver.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostNaverMailSendRequestDto {
    @ApiModelProperty(value = "수신자", example = "wo8934@naver.com")
    @NotNull
    @Size(max = 30)
    private String to;

    @ApiModelProperty(value = "제목", example = "title")
    @Size(max = 30)
    private String title;

    @ApiModelProperty(value = "본문 내용", example = "content")
    @NotNull
    private String content;
}