package com.osanvalley.moamail.domain.mail.google;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Api(tags = "Gmail API")
@RequestMapping("api/mails")
public class GmailApiController {
    
}
