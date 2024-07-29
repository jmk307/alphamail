package com.osanvalley.moamail.domain.mail;

import com.osanvalley.moamail.domain.mail.google.dto.MailDetailResponseDto;
import com.osanvalley.moamail.domain.mail.google.dto.MailSendRequestDto;
import com.osanvalley.moamail.domain.mail.google.dto.PageDto;
import com.osanvalley.moamail.domain.member.dto.SocialAuthCodeDto;
import com.osanvalley.moamail.global.oauth.dto.GmailAttachmentRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.global.config.CommonApiResponse;
import com.osanvalley.moamail.global.config.security.jwt.annotation.LoginUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "Mail API")
@RequestMapping("api/mails")
public class MailApiController {
    private final MailService mailService;

    @PostMapping("google")
    @ApiOperation(value = "구글 계정 연동 및 Gmail 메일 저장하기")
    public ResponseEntity<CommonApiResponse<String>> linkGoogleAndSaveGmails(
            @ApiIgnore @LoginUser Member member,
            @RequestBody SocialAuthCodeDto socialAuthCodeDto) {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.linkGoogleAndSaveGmails(member, socialAuthCodeDto)));
    }

    @PostMapping("naver")
    @ApiOperation(value = "네이버 메일 저장하기")
    public ResponseEntity<CommonApiResponse<String>> saveNaverMails(
            @ApiIgnore @LoginUser Member member) throws MessagingException, IOException {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.saveNaverMails(member)));
    }

    @GetMapping
    @ApiOperation(value = "메일 전체 읽기")
    public ResponseEntity<CommonApiResponse<PageDto>> showAllMails(
            @ApiIgnore @LoginUser Member member,
            @RequestParam int pageNumber) {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.showAllMails(member, pageNumber)));
    }

    @GetMapping("received")
    @ApiOperation(value = "받은 메일함 읽기")
    public ResponseEntity<CommonApiResponse<PageDto>> showReceivedMails(
            @ApiIgnore @LoginUser Member member,
            @RequestParam int pageNumber) {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.showReceivedMails(member, pageNumber)));
    }

    @GetMapping("sent")
    @ApiOperation(value = "보낸 메일함 읽기")
    public ResponseEntity<CommonApiResponse<PageDto>> showSentMails(
            @ApiIgnore @LoginUser Member member,
            @RequestParam int pageNumber) {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.showSentMails(member, pageNumber)));
    }

    @GetMapping("me")
    @ApiOperation(value = "내게 쓴 메일함 읽기")
    public ResponseEntity<CommonApiResponse<PageDto>> showMailsSentByMe(
            @ApiIgnore @LoginUser Member member,
            @RequestParam int pageNumber) {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.showMailsSentByMe(member, pageNumber)));
    }

    @GetMapping("{mailId}")
    @ApiOperation(value = "메일 상세 보기")
    public ResponseEntity<CommonApiResponse<MailDetailResponseDto>> showMail(
            @ApiIgnore @LoginUser Member member,
            @PathVariable Long mailId) {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.showMail(member, mailId)));
    }

    @GetMapping("current")
    @ApiOperation(value = "유저 추가한 메일 리스트 보기")
    public ResponseEntity<CommonApiResponse<List<String>>> showMemberSocialEmails(
            @ApiIgnore @LoginUser Member member) {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.showMemberSocialEmails(member)));
    }

    @PostMapping("google/send")
    @ApiOperation(value = "Gmail 보내기")
    public ResponseEntity<CommonApiResponse<String>> sendGmail(
            @ApiIgnore @LoginUser Member member,
            @RequestBody MailSendRequestDto mailSendRequestDto) throws MessagingException, IOException {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.sendGmail(member, mailSendRequestDto)));
    }
}
