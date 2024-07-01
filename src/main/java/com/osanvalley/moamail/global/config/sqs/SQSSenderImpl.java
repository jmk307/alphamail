package com.osanvalley.moamail.global.config.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface SQSSenderImpl {
    void sendMessage(MessageDto message) throws JsonProcessingException;
}
