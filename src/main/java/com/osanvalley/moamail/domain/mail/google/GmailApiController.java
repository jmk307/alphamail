package com.osanvalley.moamail.domain.mail.google;

import com.osanvalley.moamail.domain.mail.google.dto.PageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.global.config.CommonApiResponse;
import com.osanvalley.moamail.global.config.security.jwt.annotation.LoginUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@Api(tags = "Gmail API")
@RequestMapping("api/mails")
public class GmailApiController {
    private final MailService mailService;

    @GetMapping
    @ApiOperation(value = "메일 전체 읽기")
    public ResponseEntity<CommonApiResponse<PageDto>> showEmails(
            @ApiIgnore @LoginUser Member member,
            @RequestParam int pageNumber) {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.showMails(member, pageNumber)));
    }
}
