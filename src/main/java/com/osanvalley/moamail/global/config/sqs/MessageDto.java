package com.osanvalley.moamail.global.config.sqs;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageDto {
    private int[] email_id;

    public MessageDto(int[] email_id) {
        this.email_id = email_id;
    }
}
