package com.osanvalley.moamail.domain.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialHasDto {
    private Boolean hasGoogle;

    private Boolean hasNaver;

    @Builder
    public SocialHasDto(Boolean hasGoogle, Boolean hasNaver) {
        this.hasGoogle = hasGoogle;
        this.hasNaver = hasNaver;
    }

    public static SocialHasDto of(Boolean hasGoogle, Boolean hasNaver) {
        return SocialHasDto.builder()
                .hasGoogle(hasGoogle)
                .hasNaver(hasNaver)
            .build();
    }
}
