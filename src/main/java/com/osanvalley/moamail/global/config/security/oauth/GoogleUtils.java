package com.osanvalley.moamail.global.config.security.oauth;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.osanvalley.moamail.global.config.security.oauth.dto.GoogleMemberDto;
import com.osanvalley.moamail.global.error.ErrorCode;
import com.osanvalley.moamail.global.error.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleUtils {
    private final WebClient webClient;

    private final String GOOGLE_API_URL = "https://www.googleapis.com/oauth2/v1/userinfo";

    public GoogleMemberDto getGoogleMember(String googleAccessToken) {
        try {
            return webClient.get()
                    .uri(GOOGLE_API_URL)
                    .header("Authorization", "Bearer " + googleAccessToken)
                    .retrieve()
                    .bodyToMono(GoogleMemberDto.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(ErrorCode.GOOGLE_BAD_REQUEST);
        }
    }
}
