package com.osanvalley.moamail.global.config.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
@Component
public class SQSSender implements SQSSenderImpl {
    @Value("${cloud.aws.sqs.queue.url}")
    private String url;

    private final ObjectMapper objectMapper;
    private final AmazonSQS amazonSQS;

    @Override
    public void sendMessage(MessageDto message) throws JsonProcessingException {
        String messageBody = objectMapper.writeValueAsString(message);
        SendMessageRequest sendMessageRequest = new SendMessageRequest(url, messageBody);
        amazonSQS.sendMessage(sendMessageRequest);
    }
}
