package com.osanvalley.moamail.global.oauth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleMemberInfoDto {
    private String id;

    private String email;

    private String verified_email;

    private String name;

    private String given_name;

    private String family_name;

    private String picture;

    private String locale;
}
