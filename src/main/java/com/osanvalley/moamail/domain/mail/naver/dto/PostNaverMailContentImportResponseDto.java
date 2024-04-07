package com.osanvalley.moamail.domain.mail.naver.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class PostNaverMailContentImportResponseDto {
    @ApiModelProperty(value = "불러온 메일의 개수", example = "")
    @NonNull
    private int mailCount;
}
