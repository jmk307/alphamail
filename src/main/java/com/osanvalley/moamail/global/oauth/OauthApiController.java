package com.osanvalley.moamail.global.oauth;

import com.osanvalley.moamail.domain.member.MemberService;
import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.global.config.security.jwt.annotation.LoginUser;
import com.osanvalley.moamail.global.oauth.dto.GoogleAccessTokenDto;
import com.osanvalley.moamail.global.oauth.dto.GoogleMemberInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.osanvalley.moamail.global.config.CommonApiResponse;
import com.osanvalley.moamail.global.oauth.dto.GmailListResponseDto;
import com.osanvalley.moamail.global.oauth.dto.GmailResponseDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@Api(tags = "OAUTH API 테스트")
@RequestMapping("api/oauth")
// @ApiIgnore
public class OauthApiController {
    private final GoogleUtils googleUtils;

    @GetMapping("google/{accessToken}/{messageId}")
    @ApiOperation(value = "Gmail 메시지 하나 가져오기")
    public ResponseEntity<CommonApiResponse<GmailResponseDto>> showGmailMessage(
            @PathVariable String accessToken,
            @PathVariable String messageId) {
        return ResponseEntity.ok(CommonApiResponse.of(googleUtils.showGmailMessage(accessToken, messageId)));
    }
    
    @GetMapping("google/{accessToken}")
    @ApiOperation(value = "Gmail 메시지 리스트 가져오기")
    public ResponseEntity<CommonApiResponse<GmailListResponseDto>> showGmailMessages(
            @PathVariable String accessToken,
            @RequestParam(required = false) String nextPageToken) {
        return ResponseEntity.ok(CommonApiResponse.of(googleUtils.showGmailMessages(accessToken, nextPageToken)));
    }

    /*@PostMapping("google/{accessToken}")
    @ApiOperation(value = "Gmail 저장하기")
    public ResponseEntity<CommonApiResponse<String>> saveGmailMessages(
            @PathVariable String accessToken,
            @RequestParam(required = false) String nextPageToken) {
        return ResponseEntity.ok(CommonApiResponse.of(googleUtils.saveGmails(accessToken, nextPageToken)));
    }*/

    @DeleteMapping("google")
    @ApiOperation(value = "Gmail 삭제하기")
    public ResponseEntity<CommonApiResponse<String>> deleteAllMails() {
        return ResponseEntity.ok(CommonApiResponse.of(googleUtils.deleteAllGmails()));
    }

    /*@PatchMapping("google/reissue")
    @ApiOperation(value = "Gmail 토큰 재발급")
    public ResponseEntity<CommonApiResponse<String>> reissueAccessToken(
            @ApiIgnore @LoginUser Member member) {
        return ResponseEntity.ok(CommonApiResponse.of(googleUtils.reissueGoogleAccessToken(member)));
    }*/

    @GetMapping("google/token")
    @ApiOperation(value = "Gmail 토큰 가져오기")
    public ResponseEntity<CommonApiResponse<GoogleAccessTokenDto>> getGoogleAccessToken(
            @RequestParam(required = false) String code) {
        return ResponseEntity.ok(CommonApiResponse.of(googleUtils.getGoogleAccessToken(code)));
    }

    @GetMapping("google/info")
    @ApiOperation(value = "Google 정보 가져오기")
    public ResponseEntity<CommonApiResponse<GoogleMemberInfoDto>> getGoogleInfo(
            @RequestParam(required = false) String googleAccessToken) {
        return ResponseEntity.ok(CommonApiResponse.of(googleUtils.getGoogleMemberInfo(googleAccessToken)));
    }
}
