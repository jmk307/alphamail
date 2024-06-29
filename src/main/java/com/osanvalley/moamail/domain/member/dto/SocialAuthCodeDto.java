package com.osanvalley.moamail.domain.member.dto;

import lombok.Getter;

@Getter
public class SocialAuthCodeDto {
    private String code;

    private String provider;
}
