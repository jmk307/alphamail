package com.osanvalley.moamail.global.oauth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleAccessTokenDto {
    private String accessToken;

    private int expiresIn;

    private String scope;

    private String tokenType;
}
