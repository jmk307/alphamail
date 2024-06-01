package com.osanvalley.moamail.domain.member;

import javax.validation.Valid;

import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.global.config.security.jwt.annotation.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osanvalley.moamail.domain.member.dto.LoginDto;
import com.osanvalley.moamail.domain.member.dto.MemberRequestDto;
import com.osanvalley.moamail.domain.member.dto.MemberResponseDto;
import com.osanvalley.moamail.domain.member.dto.SocialMemberRequestDto;
import com.osanvalley.moamail.global.config.CommonApiResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;


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
            @Valid @RequestBody SocialMemberRequestDto socialMemberRequestDto) {
        return ResponseEntity.ok(CommonApiResponse.of(memberService.linkSocialAccount(member, socialMemberRequestDto)));
    }

    @PostMapping("social/signupAndIn")
    @ApiOperation(value = "소셜 회원가입/로그인")
    public ResponseEntity<CommonApiResponse<MemberResponseDto>> signUpAndInSocial(
            @RequestBody SocialMemberRequestDto socialMemberRequestDto) {
        return memberService.signUpAndInSocial(socialMemberRequestDto);
    }
}
