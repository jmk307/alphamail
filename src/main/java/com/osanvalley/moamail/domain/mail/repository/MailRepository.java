package com.osanvalley.moamail.domain.mail.repository;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.member.entity.Member;

import java.util.List;

import com.osanvalley.moamail.domain.member.model.Social;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MailRepository extends JpaRepository<Mail, Long> {
    Page<Mail> findAllBySocialMember_Member(Member member, Pageable pageable);

    Page<Mail> findAllBySocialMember_MemberAndFromEmailIn(Member member, List<String> fromEmails, Pageable pageable);

    boolean existsBySocialMember_MemberAndSocial(Member member, Social social);
}