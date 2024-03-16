package com.osanvalley.moamail.global.config.security.oauth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleMemberDto {
    private String id;

    private String email;

    private Boolean verifiedEmail;

    private String name;

    private String givenName;

    private String familyName;

    private String picture;
    
    private String locale;
}
