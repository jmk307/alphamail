package com.osanvalley.moamail.global.oauth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GmailAttachmentResponseDto {
    private String size;

    private String data;
}
