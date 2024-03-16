package com.osanvalley.moamail.domain.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class NaverMailMemberRequestDto {
    @NotNull
    @Size(max = 30)
    private String emailAddress;

    @NotNull
    private String password;

    @NotNull
    private String memberId;
}
