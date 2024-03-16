package com.osanvalley.moamail.domain.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class NaverMailCountRequestDto {
    @NotNull
    private int mailCount;
}
