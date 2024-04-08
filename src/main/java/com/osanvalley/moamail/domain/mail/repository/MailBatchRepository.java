package com.osanvalley.moamail.domain.mail.repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.osanvalley.moamail.domain.mail.entity.Mail;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MailBatchRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Mail> mails) {
        String sql = "insert into mail " +
            "(created_at, updated_at, content, html, from_email, to_email_receivers, cc_email_receivers, history_id, social, social_member_id, title) " +
        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, 
                mails,
                mails.size(),
                (PreparedStatement ps, Mail mail) -> {
                    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(3, mail.getContent());
                    ps.setString(4, mail.getHtml());
                    ps.setString(5, mail.getFromEmail());
                    ps.setString(6, mail.getToEmailReceivers());
                    ps.setString(7, mail.getCcEmailReceivers());
                    ps.setString(8, mail.getHistoryId());
                    ps.setString(9, mail.getSocial().name());
                    ps.setLong(10, mail.getSocialMember().getId());
                    ps.setString(11, mail.getTitle());
                });
    }
}
