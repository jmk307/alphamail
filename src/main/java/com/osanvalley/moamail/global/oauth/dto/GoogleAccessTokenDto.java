package com.osanvalley.moamail.global.oauth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleAccessTokenDto {
    private String access_token;

    private int expires_in;

    private String refresh_token;

    private String scope;

    private String token_type;
}
