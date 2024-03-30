package com.osanvalley.moamail.domain.mail.google;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osanvalley.moamail.global.config.CommonApiResponse;
import com.osanvalley.moamail.global.oauth.dto.GmailResponseDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Api(tags = "Gmail API")
@RequestMapping("api/mails")
public class GmailApiRepository {
    private final MailService mailService;
}
