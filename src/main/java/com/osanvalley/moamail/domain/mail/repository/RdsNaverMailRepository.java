package com.osanvalley.moamail.domain.mail.repository;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RdsNaverMailRepository extends JpaRepository<Mail, Long> {

}
