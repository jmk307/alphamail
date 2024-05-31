package com.osanvalley.moamail.domain.mail.repository;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.member.entity.Member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MailRepository extends JpaRepository<Mail, Long> {
    Page<Mail> findAllBySocialMember_Member(Member member, Pageable pageable);

    /*@Query("select m from Mail m " +
            "where m.socialMember.member = ?1 and " +
            "SELECT " +
            "CASE " +
            "WHEN LOCATE(' <', m.fromEmail) > 0 THEN SUBSTRING(m.fromEmail, LOCATE('<', m.fromEmail) + 1, LOCATE('>', m.fromEmail) - LOCATE('<', m.fromEmail) - 1) " +
            "ELSE m.fromEmail " +
            "END AS extractedEmail " +
            "FROM Mail m " +
            "WHERE m.fromEmail IN :fromEmails")
    Page<Mail> findAllBySocialMember_MemberAndFromEmailIn(Member member, List<String> fromEmails, Pageable pageable);*/

    @Query("select m from Mail m where m.socialMember.member = ?1 and m.fromEmail in ?2")
    Page<Mail> findAllBySocialMember_MemberAndFromEmailIn(Member member, List<String> fromEmails, Pageable pageable);
}