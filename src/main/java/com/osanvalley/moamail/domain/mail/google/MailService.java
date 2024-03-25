package com.osanvalley.moamail.domain.mail.google;

import com.osanvalley.moamail.domain.mail.MailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailRepository mailRepository;

    @Transactional(readOnly = true)
    public void readGmail() {

    }
}
