package com.osanvalley.moamail.domain.mail.google.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osanvalley.moamail.domain.mail.model.Readable;

import java.time.LocalDateTime;

public interface MailPrevAndNextDto {
    Long getId();

    String getAlias();

    Readable getHas_Read();

    String getTitle();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM.dd HH:mm", timezone = "Asia/Seoul")
    LocalDateTime getSend_Date();
}
