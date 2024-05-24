package com.osanvalley.moamail.domain.mail.google;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osanvalley.moamail.domain.mail.google.dto.MailResponseDto;
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

    @GetMapping()
    @ApiOperation(value = "메일 전체 읽기")
    public ResponseEntity<CommonApiResponse<List<MailResponseDto>>> getCurrentGps(
            @ApiIgnore @LoginUser Member member) {
        return ResponseEntity.ok(CommonApiResponse.of(mailService.readMails(member)));
    }
}
