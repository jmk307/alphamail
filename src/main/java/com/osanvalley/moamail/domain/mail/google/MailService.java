package com.osanvalley.moamail.domain.mail.google;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osanvalley.moamail.domain.mail.repository.MailRepository;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailRepository mailRepository;

    @Transactional(readOnly = true)
    public void readGmail() {

    }
}
