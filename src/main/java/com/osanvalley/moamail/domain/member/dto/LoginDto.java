package com.osanvalley.moamail.domain.member.dto;

import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDto {
    private String authId;
    
    private String password;
}
