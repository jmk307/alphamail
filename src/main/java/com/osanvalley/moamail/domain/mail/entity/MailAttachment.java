package com.osanvalley.moamail.domain.mail.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailAttachment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String mimeType;

    private Integer size;

    private String attachmentDownloadUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mail mail;

    @Builder
    public MailAttachment(Long id, String fileName, String mimeType, Integer size, String attachmentDownloadUrl, Mail mail) {
        this.id = id;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.size = size;
        this.attachmentDownloadUrl = attachmentDownloadUrl;
        this.mail = mail;
    }
}
