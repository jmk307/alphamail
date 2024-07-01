package com.osanvalley.moamail.domain.mail.google.dto;

import lombok.Getter;

@Getter
public class MailEvent {
    private int[] mailIds;

    public MailEvent(int[] mailIds) {
        this.mailIds = mailIds;
    }
}
