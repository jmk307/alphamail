package com.osanvalley.moamail.domain.member;

import com.osanvalley.moamail.domain.member.dto.*;
import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.global.config.CommonApiResponse;
import com.osanvalley.moamail.global.config.security.jwt.annotation.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@Api(tags = "회원가입/로그인")
@RequestMapping("api/members")
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("common/signup")
    @ApiOperation(value = "일반 회원가입")
    public ResponseEntity<CommonApiResponse<MemberResponseDto>> signUpCommon(
            @Valid @RequestBody MemberRequestDto memberReqeustDto) {
        return memberService.signUpCommon(memberReqeustDto);
    }

    @GetMapping
    @ApiOperation(value = "아이디 중복 체크")
    public ResponseEntity<CommonApiResponse<Boolean>> validateAuthId(@RequestParam String authId) {
        return ResponseEntity.ok(CommonApiResponse.of(memberService.validateAuthId(authId)));
    }
    
    @PostMapping("common/signin")
    @ApiOperation(value = "일반 로그인")
    public ResponseEntity<CommonApiResponse<MemberResponseDto>> signInCommon(
            @Valid @RequestBody LoginDto loginDto) {
        return memberService.signInCommon(loginDto);
    }

    @PostMapping("common/linkSocialAccount")
    @ApiOperation(value = "소셜 계정 연동")
    public ResponseEntity<CommonApiResponse<String>> linkSocialAccount(
            @ApiIgnore @LoginUser Member member,
            @Valid @RequestBody SocialAuthCodeDto socialAuthCodeDto) {
        return ResponseEntity.ok(CommonApiResponse.of(memberService.linkSocialAccount(member, socialAuthCodeDto)));
    }

    @PostMapping("social/signupAndIn")
    @ApiOperation(value = "소셜 회원가입/로그인")
    public ResponseEntity<CommonApiResponse<MemberResponseDto>> signUpAndInSocial(
            @RequestBody SocialAuthCodeDto socialAuthCodeDto) {
        return memberService.signUpAndInSocial(socialAuthCodeDto);
    }

    @GetMapping("social/check")
    @ApiOperation(value = "소셜 계정 연동 여부 확인")
    public ResponseEntity<CommonApiResponse<SocialHasDto>> checkSocial(
            @ApiIgnore @LoginUser Member member) {
        return ResponseEntity.ok(CommonApiResponse.of(memberService.checkSocial(member)));
    }

    @PostMapping("social/setImapAccount")
    @ApiOperation(value = "로그인한 유저의 IMAP 계정 셋팅(NAVER)")
    public ResponseEntity<CommonApiResponse<String>> setImapAccount(
            @ApiIgnore @LoginUser Member member,
            @RequestBody ImapAccountRequestDto imapAccountRequestDto) {
        return ResponseEntity.ok(CommonApiResponse.of(memberService.setImapAccount(member, imapAccountRequestDto)));
    }
}
