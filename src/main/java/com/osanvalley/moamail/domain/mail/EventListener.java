package com.osanvalley.moamail.domain.mail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.mail.google.dto.MailEvent;
import com.osanvalley.moamail.domain.mail.repository.MailRepository;
import com.osanvalley.moamail.global.config.sqs.MessageDto;
import com.osanvalley.moamail.global.config.sqs.SQSSenderImpl;
import com.osanvalley.moamail.global.oauth.GoogleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EventListener {
    private final SQSSenderImpl sqsService;
    private final MailRepository mailRepository;
    private final GoogleUtils googleUtils;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAfterCommitMailsSaved(MailEvent mailEvent) throws JsonProcessingException {
        int[] mailIds = mailRepository.findAllBySocialMember_Member(mailEvent.getMember()).stream()
                .map(Mail::getId)
                .mapToInt(Long::intValue).toArray();

        sendMailIdsToSQSEachly(mailIds);

        if (mailEvent.getNextPageToken() != null) {
            googleUtils.saveRemainingGmails(mailEvent.getSocialMember(), mailEvent.getNextPageToken());
        }
    }

    private void sendMailIdsToSQSEachly(int[] mailIds) throws JsonProcessingException {
        for (int i = 0; i < mailIds.length; i += 10) {
            int end = Math.min(mailIds.length, i + 10);

            int[] chunk = new int[end - i];
            System.arraycopy(mailIds, i, chunk, 0, end - i);

            sqsService.sendMessage(new MessageDto(chunk));
        }
    }
}
