package com.osanvalley.moamail.domain.member;

import com.osanvalley.moamail.domain.mail.google.dto.SocialRequest;
import com.osanvalley.moamail.domain.mail.repository.MailRepository;
import com.osanvalley.moamail.domain.member.dto.*;
import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.RegisterType;
import com.osanvalley.moamail.domain.member.model.Social;
import com.osanvalley.moamail.domain.member.repository.MemberRepository;
import com.osanvalley.moamail.domain.member.repository.SocialMemberRepository;
import com.osanvalley.moamail.global.config.CommonApiResponse;
import com.osanvalley.moamail.global.config.security.encrypt.TwoWayEncryptService;
import com.osanvalley.moamail.global.config.security.jwt.TokenProvider;
import com.osanvalley.moamail.global.error.ErrorCode;
import com.osanvalley.moamail.global.error.exception.BadRequestException;
import com.osanvalley.moamail.global.oauth.GoogleUtils;
import com.osanvalley.moamail.global.oauth.dto.GoogleAccessTokenDto;
import com.osanvalley.moamail.global.oauth.dto.GoogleMemberInfoDto;
import com.osanvalley.moamail.global.util.Uuid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.osanvalley.moamail.global.config.security.jwt.TokenProvider.AUTHORIZATION;
import static com.osanvalley.moamail.global.config.security.jwt.TokenProvider.BEARER;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SocialMemberRepository socialMemberRepository;
    private final TokenProvider tokenProvider;
    private final MailRepository mailRepository;
    private final PasswordEncoder passwordEncoder;
    private final TwoWayEncryptService twoWayEncryptService;
    private final GoogleUtils googleUtils;

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

        Boolean hasGoogle = mailRepository.existsBySocialMember_MemberAndSocial(member, Social.GOOGLE);
        Boolean hasNaver = mailRepository.existsBySocialMember_MemberAndSocial(member, Social.NAVER);

        return new ResponseEntity<>(CommonApiResponse.of(MemberResponseDto.of(member, hasGoogle, hasNaver, accessToken)), httpHeaders, HttpStatus.OK);
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

        Boolean hasGoogle = mailRepository.existsBySocialMember_MemberAndSocial(member, Social.GOOGLE);
        Boolean hasNaver = mailRepository.existsBySocialMember_MemberAndSocial(member, Social.NAVER);

        return new ResponseEntity<>(CommonApiResponse.of(MemberResponseDto.of(member, hasGoogle, hasNaver, accessToken)), httpHeaders, HttpStatus.OK);
    }

    // 비밀번호 일치확인
    public void validatePassword(String password, String confirmPassword) {
        if (!passwordEncoder.matches(password, confirmPassword)) {
            throw new BadRequestException(ErrorCode.PASSWORD_CONFIRM_NOT_VALID);
        }
    }

    // 회원가입 및 로그인(소셜)
    @Transactional
    public ResponseEntity<CommonApiResponse<MemberResponseDto>> signUpAndInSocial(SocialAuthCodeDto socialAuthCodeDto) {
        RegisterType registerType = validateRegisterType(socialAuthCodeDto.getProvider());
        SocialMemberRequestDto socialMemberRequestDto = setSocialMemberRequestDto(socialAuthCodeDto, registerType);

        Optional<SocialMember> socialMember = socialMemberRepository.findBySocialIdAndMember_RegisterType(socialMemberRequestDto.getSocialId(), registerType);

        if (socialMember.isPresent()) {
            Member member = socialMember.get().getMember();

            String accessToken = tokenProvider.generateAccessToken(member.getAuthId());
            HttpHeaders httpHeaders = makeHttpHeaders(accessToken);

            Boolean hasGoogle = mailRepository.existsBySocialMember_MemberAndSocial(member, Social.GOOGLE);
            Boolean hasNaver = mailRepository.existsBySocialMember_MemberAndSocial(member, Social.NAVER);

            return new ResponseEntity<>(CommonApiResponse.of(MemberResponseDto.of(member, hasGoogle, hasNaver, accessToken)), httpHeaders, HttpStatus.OK);
        } else {
            Member member = MemberRequestDto.memberToEntity(registerType, socialMemberRequestDto);
            memberRepository.saveAndFlush(member);

            String accessToken = tokenProvider.generateAccessToken(member.getAuthId());
            HttpHeaders httpHeaders = makeHttpHeaders(accessToken);

            Boolean hasGoogle = mailRepository.existsBySocialMember_MemberAndSocial(member, Social.GOOGLE);
            Boolean hasNaver = mailRepository.existsBySocialMember_MemberAndSocial(member, Social.NAVER);

            return new ResponseEntity<>(CommonApiResponse.of(MemberResponseDto.of(member, hasGoogle, hasNaver, accessToken)), httpHeaders, HttpStatus.OK);
        }
    }

    // 소셜로그인 회원정보 세팅
    private SocialMemberRequestDto setSocialMemberRequestDto(SocialAuthCodeDto socialAuthCodeDto, RegisterType registerType) {
        SocialMemberRequestDto socialMemberRequestDto;

        if (registerType.equals(RegisterType.NAVER)) {
            // 재환 추가
            socialMemberRequestDto = SocialMemberRequestDto.builder()
                    .nickname(null)
                    .email(null)
                    .profileImgUrl(null)
                    .provider(socialAuthCodeDto.getProvider())
                    .socialId(null)
                    .build();
        } else {
            GoogleAccessTokenDto googleAccessTokenDto = googleUtils.getGoogleAccessToken(socialAuthCodeDto.getCode());
            GoogleMemberInfoDto googleMemberInfoDto = googleUtils.getGoogleMemberInfo(googleAccessTokenDto.getAccess_token());

            socialMemberRequestDto = SocialMemberRequestDto.builder()
                    .nickname(googleMemberInfoDto.getName())
                    .email(googleMemberInfoDto.getEmail())
                    .profileImgUrl(googleMemberInfoDto.getPicture())
                    .provider(socialAuthCodeDto.getProvider())
                    .socialId(googleMemberInfoDto.getId())
                    .googleAccessToken(googleAccessTokenDto.getAccess_token())
                    .googleRefreshToken(googleAccessTokenDto.getRefresh_token())
                    .build();
        }
        return socialMemberRequestDto;
    }

    // 회원가입 소셜 판별
    public static RegisterType validateRegisterType(String provider) {
        return provider.equals("google")
                ? RegisterType.GOOGLE
                : RegisterType.NAVER;
    }

    // 등록 소셜 판별
    public static Social validateSocialType(String provider) {
        return provider.equals("google")
                ? Social.GOOGLE
                : Social.NAVER;
    }

    // 유저 메일 존재여부
    @Transactional(readOnly = true)
    public SocialHasDto checkSocial(Member member) {
        Boolean hasGoogle = mailRepository.existsBySocialMember_MemberAndSocial(member, Social.GOOGLE);
        Boolean hasNaver = mailRepository.existsBySocialMember_MemberAndSocial(member, Social.NAVER);

        return SocialHasDto.of(hasGoogle, hasNaver);
    }

    // IMAP 셋팅
    @Transactional
    public String setImapAccount(Member member, ImapAccountRequestDto imapAccountRequestDto) {
        Optional<SocialMember> findSocialMember = socialMemberRepository.findByMember_AuthIdAndEmail(member.getAuthId(), imapAccountRequestDto.getImapAccount() + "@naver.com");
        if(findSocialMember.isPresent()) {
            SocialMember socialMember = findSocialMember.get();
            if(socialMember.getImapAccount() != imapAccountRequestDto.getImapAccount()) { // 불러올 IMAP ID가 변경된 경우
                socialMember.updateLastStoredMsgUID(0L);
            }
            String encryptedPassword = twoWayEncryptService.encrypt(imapAccountRequestDto.getImapPassword());
            socialMember.updateImapPassword(encryptedPassword);
        } else { // IMAP 값을 처음 셋팅하는 경우
            byte[] uuidByte = Uuid.createUUID();
            String uuidHex = Uuid.bytesToHex(uuidByte);
            String encryptedPassword = twoWayEncryptService.encrypt(imapAccountRequestDto.getImapPassword());
            SocialMember socialMember = imapAccountRequestDto.socialMember_ImapAccount_ToEntity(uuidHex, member, imapAccountRequestDto.getImapAccount(), encryptedPassword, 0L);
            socialMemberRepository.save(socialMember);
        }

        return "IMAP 계정 셋팅 완료";
    }
}
