package com.osanvalley.moamail.global.oauth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GmailAttachmentRequestDto {
    private String attachmentId;

    private String fileName;
}
