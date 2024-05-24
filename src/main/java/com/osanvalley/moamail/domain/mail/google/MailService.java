package com.osanvalley.moamail.domain.mail.google;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.mail.google.dto.MailResponseDto;
import com.osanvalley.moamail.domain.mail.repository.MailRepository;
import com.osanvalley.moamail.domain.member.entity.Member;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailRepository mailRepository;

    @Transactional(readOnly = true)
    public List<MailResponseDto> readMails(Member member) {
        List<Mail> mails = mailRepository.findAllBySocialMember_Member(member);

        return mails.stream()
            .map(MailResponseDto::of)
            .collect(Collectors.toList());
    }
}
