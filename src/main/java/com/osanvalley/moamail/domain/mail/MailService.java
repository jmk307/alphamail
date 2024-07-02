package com.osanvalley.moamail.domain.mail;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.mail.google.dto.MailEvent;
import com.osanvalley.moamail.domain.mail.google.dto.PageDto;
import com.osanvalley.moamail.domain.member.dto.SocialAuthCodeDto;
import com.osanvalley.moamail.domain.member.dto.SocialMemberRequestDto;
import com.osanvalley.moamail.domain.member.model.RegisterType;
import com.osanvalley.moamail.global.imap.NaverUtils;
import com.osanvalley.moamail.global.imap.NaverImapMailConnector;
import com.osanvalley.moamail.domain.mail.repository.MailBatchRepository;
import com.osanvalley.moamail.domain.mail.repository.MailCustomRepository;
import com.osanvalley.moamail.domain.mail.repository.MailRepository;
import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.Social;
import com.osanvalley.moamail.domain.member.repository.SocialMemberRepository;
import com.osanvalley.moamail.global.config.security.encrypt.TwoWayEncryptService;
import com.osanvalley.moamail.global.error.ErrorCode;
import com.osanvalley.moamail.global.error.exception.BadRequestException;
import com.osanvalley.moamail.global.oauth.GoogleUtils;
import com.osanvalley.moamail.global.oauth.dto.GoogleAccessTokenDto;
import com.osanvalley.moamail.global.oauth.dto.GoogleMemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.UIDFolder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.osanvalley.moamail.domain.member.MemberService.validateRegisterType;
import static com.osanvalley.moamail.domain.member.MemberService.validateSocialType;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailRepository mailRepository;
    private final GoogleUtils googleUtils;
    private final MailCustomRepository mailCustomRepository;
    private final MailBatchRepository mailBatchRepository;
    private final SocialMemberRepository socialMemberRepository;
    private final TwoWayEncryptService twoWayEncryptService;
    private final ApplicationEventPublisher applicationEventPublisher;

    // 소셜 계정 연동 및 메일 저장(Gmail) -> 지민
    @Transactional
    public String linkGoogleAndSaveGmails(Member member, SocialAuthCodeDto socialAuthCodeDto) {
        Social social = validateSocialType(socialAuthCodeDto.getProvider());
        RegisterType registerType = validateRegisterType(socialAuthCodeDto.getProvider());
        SocialMemberRequestDto socialMemberRequestDto = setSocialMemberRequestDto(socialAuthCodeDto, registerType);

        SocialMember socialMember;

        if (socialMemberRepository.existsBySocialIdAndMember(socialMemberRequestDto.getSocialId(), member)) {
            throw new BadRequestException(ErrorCode.SOCIAL_MEMBER_ALREADY_EXISTS);
        } else {
            socialMember = SocialMemberRequestDto.socialMemberToEntity(social, member, socialMemberRequestDto);
            socialMemberRepository.saveAndFlush(socialMember);
        }

        // 구글 어세스토큰 유효성검사
        if (!googleUtils.isGoogleAccessTokenValid(socialMember.getGoogleAccessToken())) {
            googleUtils.reissueGoogleAccessToken(member);
        }

        googleUtils.saveGmails(socialMember, null);

        int[] mailIds = mailRepository.findAllBySocialMember_Member(member).stream()
                .map(Mail::getId)
                .mapToInt(Long::intValue).toArray();

        // 스팸처리할 메일id들 -> sqs
        applicationEventPublisher.publishEvent(new MailEvent(mailIds));

        return "메일 저장 완료";
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

    @Transactional(readOnly = true)
    public PageDto showAllMails(Member member, int pageNumber) {
        // 전체메일 보기 -> 지민(완)
        Pageable pageable = PageRequest.of(pageNumber - 1, 20, Sort.by(Sort.Direction.DESC, "sendDate"));
        Page<Mail> mails = mailRepository.findAllBySocialMember_Member(member, pageable);

        return PageDto.of(mails);
    }

    // 받은메일함 보기 -> 지민(완)
    @Transactional(readOnly = true)
    public PageDto showReceivedMails(Member member, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 20, Sort.by(Sort.Direction.DESC, "sendDate"));

        List<String> memberEmails = member.getSocialMembers().stream()
                .map(SocialMember::getEmail)
                .collect(Collectors.toList());
        Page<Mail> receivedMails = mailCustomRepository.findAllReceivedMails(member, memberEmails, pageable);

        return PageDto.of(receivedMails);
    }

    // 보낸메일함 보기 -> 지민(완)
    @Transactional(readOnly = true)
    public PageDto showSentMails(Member member, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 20, Sort.by(Sort.Direction.DESC, "sendDate"));

        List<String> memberEmails = member.getSocialMembers().stream()
                .map(SocialMember::getEmail)
                .collect(Collectors.toList());
        Page<Mail> fromEmails = mailRepository.findAllBySocialMember_MemberAndFromEmailIn(member, memberEmails, pageable);

        return PageDto.of(fromEmails);
    }

    // 내게쓴메일함 보기


    // 스팸메일함 보기


    // 메일 발신하기(Gmail) -> 지민


    // 네이버 메서드 //
    // 메일 저장하기(네이버)
    public String saveNaverMails(Member member) throws MessagingException, IOException {
        long beforeTime = System.currentTimeMillis();

        SocialMember findSocialMember = socialMemberRepository.findByMember_AuthIdAndSocial(member.getAuthId(), Social.IMAP_NAVER)
                .orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));

        String decryptPassword = twoWayEncryptService.decrypt(findSocialMember.getImapPassword());
        NaverImapMailConnector conn = new NaverImapMailConnector(
                findSocialMember.getEmail(),
                decryptPassword
        );

        Folder inbox = conn.connect();
        Message[] messages = conn.getMessages();
        int messageCount = inbox.getMessageCount();
        NaverUtils naverUtils = new NaverUtils(inbox);
        List<Mail> mails = new ArrayList<>();
        long lastStoredMsgUID = findSocialMember.getLastStoredMsgUID();

        for(int i = 0; i < messageCount; i++) {
            long messageUID = ((UIDFolder) inbox).getUID(messages[i]);
            if(messageUID > lastStoredMsgUID) {
                System.out.println("메일 엔티티로 " + ((int)i+1) +"번째 변환 중입니다.");
                Mail mail = naverUtils.toMailEntity(messages[i], findSocialMember);
                mails.add(mail);
            } else {
                break;
            }
        }

        mailBatchRepository.saveAll(mails);
        conn.disconnect();

        long afterTime = System.currentTimeMillis();
        long secDiffTime = (afterTime - beforeTime) / 1000;
        System.out.println("시간차이(m) : " + secDiffTime);

        return "메일 저장 완료";
    }

    // 메일 발신하기(네이버) -> 재환


}
