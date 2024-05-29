package com.osanvalley.moamail.domain.mail.google;

import com.osanvalley.moamail.domain.mail.google.dto.PageDto;
import com.osanvalley.moamail.domain.mail.google.dto.SocialRequest;
import com.osanvalley.moamail.global.oauth.GoogleUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.mail.repository.MailRepository;
import com.osanvalley.moamail.domain.member.entity.Member;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailRepository mailRepository;
    private final GoogleUtils googleUtils;

    // 메일 저장하기(Gmail)
    @Transactional
    public String saveGmails(SocialRequest socialRequest) {
        googleUtils.saveGmails(socialRequest.getMember(), socialRequest.getGoogleAccessToken(), null);

        return "메일 저장 완료";
    }

    // 메일 전체 보기
    @Transactional(readOnly = true)
    public PageDto showMails(Member member, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 20, Sort.by(Sort.Direction.DESC, "sendDate"));
        Page<Mail> mails = mailRepository.findAllBySocialMember_Member(member, pageable);

        return PageDto.of(mails);
    }
}
