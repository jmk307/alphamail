package com.osanvalley.moamail.domain.mail;

import com.osanvalley.moamail.domain.mail.google.dto.PageDto;
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

@RestController
@RequiredArgsConstructor
@Api(tags = "mail API")
@RequestMapping("api/mails")
public class MailApiController {
    private final MailService mailService;

    @PostMapping("google")
    @ApiOperation(value = "Gmail 메일 저장하기")
    public ResponseEntity<CommonApiResponse<String>> saveGmails(
            @ApiIgnore @LoginUser Member member) {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.saveGmails(member)));
    }

    @GetMapping
    @ApiOperation(value = "메일 전체 읽기")
    public ResponseEntity<CommonApiResponse<PageDto>> showAllmails(
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

    @PostMapping("naver")
    @ApiOperation(value = "NAVER 메일 저장하기")
    public ResponseEntity<CommonApiResponse<String>> saveNaverMails(
            @ApiIgnore @LoginUser Member member) throws MessagingException, IOException {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.saveNaverMails(member)));
    }
}
