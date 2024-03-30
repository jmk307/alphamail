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
public class GmailResponseDto {
    private String id;

    private String threadId;

    private List<String> labelIds = new ArrayList<>();

    private String snippet;

    private String historyId;
    
    private String internalDate;

    private MessagePart payload;

    private String sizeEstimate;

    private String raw;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Header {
        private String name;

        private String value;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MessagePart {
        private String partId;

        private String mimeType;

        private String filename;

        private List<Header> headers = new ArrayList<>();

        private MessagePartBody body;

        private List<MessagePart> parts = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MessagePartBody {
        private String attachmentId;

        private int size;

        private String data;
    }
}
