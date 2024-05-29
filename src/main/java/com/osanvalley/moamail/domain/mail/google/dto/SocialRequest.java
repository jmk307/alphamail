package com.osanvalley.moamail.domain.mail.google.dto;

import com.osanvalley.moamail.domain.member.entity.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialRequest {
    @ApiModelProperty(hidden = true)
    private Member member;

    private String googleAccessToken;
}
