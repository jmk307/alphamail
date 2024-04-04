package com.osanvalley.moamail.domain.mail.dto;

import com.osanvalley.moamail.domain.member.entity.SocialMember;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NaverMailContentImportRequestDto {
    private SocialMember socialMember;
}
