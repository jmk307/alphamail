package com.osanvalley.moamail.domain.mail.naver.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostNaverMailContentImportResponseDto {
    @ApiModelProperty(value = "불러온 메일의 개수", example = "")
    @NonNull
    private int mailCount;
}
