package com.osanvalley.moamail.domain.mail.repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.osanvalley.moamail.domain.mail.entity.CCEmailReceiver;
import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.mail.entity.ToEmailReceiver;
import com.osanvalley.moamail.global.config.redis.RedisService;

import lombok.RequiredArgsConstructor;
import static com.osanvalley.moamail.global.util.Uuid.createUUID;
import static com.osanvalley.moamail.global.util.Uuid.bytesToHex;;

@Repository
@RequiredArgsConstructor
public class MailBatchRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RedisService redisService;

    @Transactional
    public void saveAll(List<Mail> mails, List<String> toEmailReceivers, List<String> ccEmailReceivers) {
        String sql = "insert into mail " +
            "(created_at, updated_at, content, from_email, history_id, social, social_member_id, title, id) " +
        "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, 
                mails,
                mails.size(),
                (PreparedStatement ps, Mail mail) -> {
                    byte[] id = createUUID();
                    String hexId = bytesToHex(id);

                    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(3, mail.getContent());
                    ps.setString(4, mail.getFromEmail());
                    ps.setString(5, mail.getHistoryId());
                    ps.setString(6, mail.getSocial().name());
                    ps.setLong(7, mail.getSocialMember().getId());
                    ps.setString(8, mail.getTitle());
                    ps.setBytes(9, id);

                    redisService.setListValues(hexId, toEmailReceivers);

                    if (!ccEmailReceivers.isEmpty()) {
                        redisService.setListValues(hexId, ccEmailReceivers);
                    }
                });
    }
}
