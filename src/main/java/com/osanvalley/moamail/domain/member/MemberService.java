package com.osanvalley.moamail.domain.member;

import static com.osanvalley.moamail.global.config.security.jwt.TokenProvider.*;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osanvalley.moamail.domain.member.dto.LoginDto;
import com.osanvalley.moamail.domain.member.dto.MemberRequestDto;
import com.osanvalley.moamail.domain.member.dto.MemberResponseDto;
import com.osanvalley.moamail.domain.member.dto.SocialMemberRequestDto;
import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.RegisterType;
import com.osanvalley.moamail.domain.member.model.Social;
import com.osanvalley.moamail.domain.member.repository.MemberRepository;
import com.osanvalley.moamail.domain.member.repository.SocialMemberRepository;
import com.osanvalley.moamail.global.config.CommonApiResponse;
import com.osanvalley.moamail.global.config.security.jwt.TokenProvider;
import com.osanvalley.moamail.global.error.ErrorCode;
import com.osanvalley.moamail.global.error.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SocialMemberRepository socialMemberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    
    // 회원가입(일반)
    @Transactional
    public ResponseEntity<CommonApiResponse<MemberResponseDto>> signUpCommon(MemberRequestDto memberReqeustDto) {
        if (memberRepository.existsByAuthId(memberReqeustDto.getAuthId())) {
            throw new BadRequestException(ErrorCode.MEMBER_ALREADY_EXIST);
        }

        String password = passwordEncoder.encode(memberReqeustDto.getPassword());

        // 회원정보
        Member member = MemberRequestDto.memberToEntity(password, memberReqeustDto);
        memberRepository.save(member);

        String accessToken = tokenProvider.generateAccessToken(memberReqeustDto.getAuthId());
        HttpHeaders httpHeaders = makeHttpHeaders(accessToken);

        return new ResponseEntity<>(CommonApiResponse.of(MemberResponseDto.of(member, accessToken)), httpHeaders, HttpStatus.OK);
    }

    // Http Header 정보 생성
    public HttpHeaders makeHttpHeaders(final String accessToken) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, BEARER + accessToken);

        return httpHeaders;
    }

    // 아이디 중복체크
    @Transactional(readOnly = true)
    public Boolean validateAuthId(String authId) {
        return memberRepository.existsByAuthId(authId);
    }

    // 로그인(일반)
    @Transactional
    public ResponseEntity<CommonApiResponse<MemberResponseDto>> signInCommon(LoginDto loginDto) {
        Member member = memberRepository.findByAuthId(loginDto.getAuthId())
                .orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));

        validatePassword(loginDto.getPassword(), member.getPassword());

        String accessToken = tokenProvider.generateAccessToken(loginDto.getAuthId());
        HttpHeaders httpHeaders = makeHttpHeaders(accessToken);

        return new ResponseEntity<>(CommonApiResponse.of(MemberResponseDto.of(member, accessToken)), httpHeaders, HttpStatus.OK);
    }

    // 비밀번호 일치확인
    public void validatePassword(String password, String confirmPassword) {
        if (!passwordEncoder.matches(password, confirmPassword)) {
            throw new BadRequestException(ErrorCode.PASSWORD_CONFIRM_NOT_VALID);
        }
    }

    // 회원가입 및 로그인(소셜)
    @Transactional
    public ResponseEntity<CommonApiResponse<MemberResponseDto>> signUpAndInSocial(SocialMemberRequestDto socialMemberRequestDto) {
        Optional<SocialMember> socialMember = socialMemberRepository.findBySocialId(socialMemberRequestDto.getSocialId());

        if (socialMember.isPresent()) {
            Member member = socialMember.get().getMember();

            String accessToken = tokenProvider.generateAccessToken(member.getAuthId());
            HttpHeaders httpHeaders = makeHttpHeaders(accessToken);

            return new ResponseEntity<>(CommonApiResponse.of(MemberResponseDto.of(member, accessToken)), httpHeaders, HttpStatus.OK);
        } else {
            RegisterType registerType = validateRegisterType(socialMemberRequestDto.getProvider());
            Social social = validateSocialType(socialMemberRequestDto.getProvider());

            Member member = MemberRequestDto.memberToEntity(registerType, socialMemberRequestDto);
            memberRepository.saveAndFlush(member);

            SocialMember newSocialMember = SocialMemberRequestDto.socialMemberToEntity(social, member, socialMemberRequestDto);
            socialMemberRepository.saveAndFlush(newSocialMember);

            String accessToken = tokenProvider.generateAccessToken(member.getAuthId());
            HttpHeaders httpHeaders = makeHttpHeaders(accessToken);

            return new ResponseEntity<>(CommonApiResponse.of(MemberResponseDto.of(member, accessToken)), httpHeaders, HttpStatus.OK);
        }
    }

    // 회원가입 소셜 판별
    public RegisterType validateRegisterType(String provider) {
        return provider == "google"
                ? RegisterType.GOOGLE
                : RegisterType.NAVER;
    }

    // 등록 소셜 판별
    public Social validateSocialType(String provider) {
        return provider == "google"
                ? Social.GOOGLE
                : Social.NAVER;
    }
}
