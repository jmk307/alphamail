package com.osanvalley.moamail.domain.mail;

import com.osanvalley.moamail.domain.mail.google.dto.PageDto;
import com.osanvalley.moamail.domain.mail.google.dto.SocialRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class MailApiController {
    private final MailService mailService;

    @PostMapping("google")
    @ApiOperation(value = "Gmail 메일 저장하기")
    public ResponseEntity<CommonApiResponse<String>> saveGmails(
            @ApiIgnore @LoginUser Member member,
            @RequestBody SocialRequest socialRequest) {
        socialRequest.setMember(member);
        return ResponseEntity.ok(CommonApiResponse.of(mailService.saveGmails(socialRequest)));
    }

    @GetMapping
    @ApiOperation(value = "메일 전체 읽기")
    public ResponseEntity<CommonApiResponse<PageDto>> showAllmails(
            @ApiIgnore @LoginUser Member member,
            @RequestParam int pageNumber) {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.showAllMails(member, pageNumber)));
    }
}
