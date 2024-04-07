package com.osanvalley.moamail.domain.mail.repository;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.osanvalley.moamail.global.util.Uuid.createUUID;

@Repository
@RequiredArgsConstructor
public class MailBatchRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Mail> mails) {
        String sql = "insert into mail " +
            "(created_at, updated_at, content, from_email, to_email_receivers, cc_email_receivers, history_id, social, social_member_id, title, id) " +
        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, 
                mails,
                mails.size(),
                (PreparedStatement ps, Mail mail) -> {
                    byte[] id = createUUID();

                    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(3, mail.getContent());
                    ps.setString(4, mail.getFromEmail());
                    ps.setString(5, mail.getToEmailReceivers());
                    ps.setString(6, mail.getCcEmailReceivers());
                    ps.setString(7, mail.getHistoryId());
                    ps.setString(8, mail.getSocial().name());
                    ps.setLong(9, mail.getSocialMember().getId());
                    ps.setString(10, mail.getTitle());
                    ps.setBytes(11, id);
                });
    }
}
