package com.osanvalley.moamail.domain.mail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.osanvalley.moamail.domain.mail.google.dto.MailEvent;
import com.osanvalley.moamail.global.config.sqs.MessageDto;
import com.osanvalley.moamail.global.config.sqs.SQSSenderImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EventListener {
    private final SQSSenderImpl sqsService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAfterCommitMailsSaved(MailEvent mailEvent) throws JsonProcessingException {
        sqsService.sendMessage(new MessageDto(mailEvent.getMailIds()));
    }
}
