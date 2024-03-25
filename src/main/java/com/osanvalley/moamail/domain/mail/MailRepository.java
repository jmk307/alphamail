package com.osanvalley.moamail.domain.mail;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Mail, Long> {
}