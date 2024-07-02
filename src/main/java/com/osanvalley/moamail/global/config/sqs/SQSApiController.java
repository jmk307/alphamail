package com.osanvalley.moamail.global.config.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@ApiIgnore
public class SQSApiController {
    private final SQSSenderImpl sqsService;

    @PostMapping("api/sqs/send")
    public void sendMessage(@RequestBody MessageDto message) throws JsonProcessingException {
        sqsService.sendMessage(message);
    }
}
