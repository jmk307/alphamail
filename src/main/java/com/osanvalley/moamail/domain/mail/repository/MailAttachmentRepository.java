package com.osanvalley.moamail.domain.mail.repository;

import com.osanvalley.moamail.domain.mail.entity.MailAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailAttachmentRepository extends JpaRepository<MailAttachment, Long> {
}
