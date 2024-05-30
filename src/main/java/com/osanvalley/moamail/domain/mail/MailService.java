package com.osanvalley.moamail.domain.mail;

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

    // 메일 저장하기(Gmail) -> 지민
    @Transactional
    public String saveGmails(SocialRequest socialRequest) {
        googleUtils.saveGmails(socialRequest.getMember(), socialRequest.getGoogleAccessToken(), null);

        // TODO : 기령 만든 AI 모델로 스팸 분류 기능 추가하기

        return "메일 저장 완료";
    }

    // 메일 저장하기(네이버) -> 재환


    // 전체메일 보기 -> 지민(완)
    @Transactional(readOnly = true)
    public PageDto showAllMails(Member member, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 20, Sort.by(Sort.Direction.DESC, "sendDate"));
        Page<Mail> mails = mailRepository.findAllBySocialMember_Member(member, pageable);

        return PageDto.of(mails);
    }

    // 받은메일함 보기
    @Transactional(readOnly = true)
    public PageDto showReceivedMails(Member member, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 20, Sort.by(Sort.Direction.DESC, "sendDate"));

        return null;
    }


    // 보낸메일함 보기


    // 내게쓴메일함 보기


    // 스팸메일함 보기


    // 메일 발신하기(Gmail) -> 지민


    // 메일 발신하기(네이버) -> 재환


}
