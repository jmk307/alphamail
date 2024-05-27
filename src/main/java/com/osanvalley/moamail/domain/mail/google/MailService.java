package com.osanvalley.moamail.domain.mail.google;

import com.osanvalley.moamail.domain.mail.google.dto.PageDto;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public PageDto showMails(Member member, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 20);
        Page<Mail> mails = mailRepository.findAllBySocialMember_Member(member, pageable);

        return PageDto.of(mails);
    }
}
