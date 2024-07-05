package com.osanvalley.moamail.domain.mail.repository;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.mail.google.dto.MailPrevAndNextDto;
import com.osanvalley.moamail.domain.member.entity.Member;

import java.util.List;

import com.osanvalley.moamail.domain.member.model.Social;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MailRepository extends JpaRepository<Mail, Long> {
    List<Mail> findAllBySocialMember_Member(Member member);

    Page<Mail> findAllBySocialMember_Member(Member member, Pageable pageable);

    Page<Mail> findAllBySocialMember_MemberAndFromEmailIn(Member member, List<String> fromEmails, Pageable pageable);

    boolean existsBySocialMember_MemberAndSocial(Member member, Social social);

    List<Mail> findAllByFromEmailAndToEmailReceiversContaining(String fromEmail, String toEmail);

    Mail findBySocialMember_MemberAndId(Member member, Long id);

    @Query(value = "SELECT m.id, m.alias, m.has_read, m.title, m.send_date FROM mail m "
            + "WHERE id = (SELECT prev_no FROM (SELECT id, LAG(id, 1, -1) OVER(ORDER BY send_date desc) AS prev_no FROM mail) B "
            + "WHERE id = :id and social_member_id = :social_member_id)", nativeQuery = true)
    MailPrevAndNextDto findMailWithPrev(Long id, Long social_member_id);

    @Query(value = "SELECT m.id, m.alias, m.has_read, m.title, m.send_date FROM mail m "
            + "WHERE id = (SELECT prev_no FROM (SELECT id, LEAD(id, 1, -1) OVER(ORDER BY send_date desc) AS prev_no FROM mail) B "
            + "WHERE id = :id and social_member_id = :social_member_id)", nativeQuery = true)
    MailPrevAndNextDto findMailWithNext(Long id, Long social_member_id);
}