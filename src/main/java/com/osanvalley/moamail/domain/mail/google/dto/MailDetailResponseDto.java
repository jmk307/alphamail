package com.osanvalley.moamail.domain.mail.google.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.mail.entity.MailAttachment;
import com.osanvalley.moamail.domain.member.model.Social;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailDetailResponseDto {
    private Long mailId;

    private Social social;

    private String title;

    private String fromEmail;

    private String toEmailReceivers;

    private String ccEmailReceivers;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime sendDate;

    private String html;

    private List<Attachment> attachments;

    private MailPrevAndNextDto prevMail;

    private MailPrevAndNextDto nextMail;

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Attachment {
        private String filename;

        private String mimeType;

        private Integer size;

        private String attachmentDownloadUrl;

        @Builder
        public Attachment(String filename, String mimeType, Integer size, String attachmentDownloadUrl) {
            this.filename = filename;
            this.mimeType = mimeType;
            this.size = size;
            this.attachmentDownloadUrl = attachmentDownloadUrl;
        }
    }

    @Builder
    public MailDetailResponseDto(Long mailId, Social social, String title, String fromEmail,
                                 String toEmailReceivers, String ccEmailReceivers, String html, LocalDateTime sendDate,
                                 List<Attachment> attachments, MailPrevAndNextDto prevMail, MailPrevAndNextDto nextMail) {
        this.mailId = mailId;
        this.social = social;
        this.title = title;
        this.fromEmail = fromEmail;
        this.toEmailReceivers = toEmailReceivers;
        this.ccEmailReceivers = ccEmailReceivers;
        this.html = html;
        this.sendDate = sendDate;
        this.attachments = attachments;
        this.prevMail = prevMail;
        this.nextMail = nextMail;
    }

    public static MailDetailResponseDto of(Mail mail, MailPrevAndNextDto prevMail, MailPrevAndNextDto nextMail) {
        return MailDetailResponseDto.builder()
            .mailId(mail.getId())
            .social(mail.getSocial())
            .title(mail.getTitle())
            .fromEmail(mail.getFromEmail())
            .toEmailReceivers(mail.getToEmailReceivers())
            .ccEmailReceivers(mail.getCcEmailReceivers())
            .html(mail.getHtml())
            .sendDate(mail.getSendDate())
            .attachments(mail.getMailAttachments().stream()
                .map(attachment -> Attachment.builder()
                    .filename(attachment.getFileName())
                    .mimeType(attachment.getMimeType())
                    .size(attachment.getSize())
                    .attachmentDownloadUrl(attachment.getAttachmentDownloadUrl())
                    .build()).collect(Collectors.toList()))
            .prevMail(prevMail)
            .nextMail(nextMail)
            .build();
    }
}
