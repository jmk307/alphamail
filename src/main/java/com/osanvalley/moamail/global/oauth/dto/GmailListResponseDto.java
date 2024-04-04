package com.osanvalley.moamail.global.oauth.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GmailListResponseDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Message {
        private String id;

        private String threadId;
    }

    public List<Message> messages = new ArrayList<>();

    private String nextPageToken;

    private String resultSizeEstimate;
}
