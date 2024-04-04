package com.osanvalley.moamail.domain.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class PostNaverImapConnectInfoResponseDto {
    @NotNull
    @Size(max = 30)
    @Setter
    private String emailAddress;

    @NotNull
    @Setter
    private String memberId;
}
