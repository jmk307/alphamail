package com.osanvalley.moamail.domain.mail.naver.service;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.mail.naver.dto.GetNaverImapConnectInfoResponseDto;
import com.osanvalley.moamail.domain.mail.naver.dto.PostNaverImapConnectInfoResponseDto;
import com.osanvalley.moamail.domain.mail.naver.dto.PostNaverMailContentImportResponseDto;
import com.osanvalley.moamail.domain.mail.naver.util.MessageToEntityConverter;
import com.osanvalley.moamail.domain.mail.naver.util.NaverMailConnector;
import com.osanvalley.moamail.domain.mail.repository.MailRepository;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.repository.SocialMemberRepository;
import com.osanvalley.moamail.global.config.security.encrypt.TwoWayEncryptService;
import com.osanvalley.moamail.global.error.ErrorCode;
import com.osanvalley.moamail.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NaverMailService {
    /**
     * MVP 단계에서는 별도로 IMAP Member를 정규화 하지 않음.
     * 따라서, Member 도메인에 의존함.
     */
    private final SocialMemberRepository socialMemberRepository;
    private final TwoWayEncryptService twoWayEncryptService;
    private final MailRepository mailRepository;
    /**
     * 정보 : Naver Mail 서비스 연동을 위해 사용자 정보 저장
     *
     * @param emailAddress IMAP 이메일 주소
     * @param password     IMAP 비밀번호
     */
    @Transactional
    public PostNaverImapConnectInfoResponseDto setImapConnectInfo(String socialId, String emailAddress, String password) {
        SocialMember findSocialMember = socialMemberRepository.findBySocialId(socialId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));
        String encryptedPassword = twoWayEncryptService.encrypt(password);
        findSocialMember.setEmail(emailAddress);
        findSocialMember.setImapPassword(encryptedPassword);

        return new PostNaverImapConnectInfoResponseDto(findSocialMember.getEmail());
    }


    /**
     * 정보 : Naver Mail 서비스 연동을 위해 사용할 사용자 정보 반환
     *
     * @param socialId DB에 저장되어 있는 소셜 사용자 정보
     */
    @Transactional(readOnly = true)
    public GetNaverImapConnectInfoResponseDto getMailConnectInfo(String socialId) {
        SocialMember findSocialMember = socialMemberRepository.findBySocialId(socialId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));
        return new GetNaverImapConnectInfoResponseDto(findSocialMember.getEmail());
    }

    /**
     * 정보 : 네이버 메일과 IMAP 연동 후 Mail Content들을 DB에 저장하는 메서드
     * @param accessToken 사용자의 IMAP 정보를 받아오기 위해 필요한 액세스 토큰 정보 (인증 기능 개발 이후)
     * @param socialId 사용자의 IMAP 정보를 받아오기 위해 필요한 소셜 ID 정보 (인증 기능 개발 이전)
     */
    public PostNaverMailContentImportResponseDto saveMailContents(String socialId) throws MessagingException, IOException {
        SocialMember findSocialMember = socialMemberRepository.findBySocialId(socialId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));

        int mailCount = 0;
        String decryptPassword = twoWayEncryptService.decrypt(findSocialMember.getImapPassword());
        NaverMailConnector conn = new NaverMailConnector(
                findSocialMember.getEmail(),
                decryptPassword
        );

        conn.connect();
        Message[] messages = conn.getMessages();

        for(Message message : messages) {
            MessageToEntityConverter converter = new MessageToEntityConverter();
            Mail messageEntity = converter.toMailEntity(message, findSocialMember);
            mailRepository.save(messageEntity);
            mailCount++;
            if(mailCount == 10) {
                break;
            }
        }

        conn.disconnect();
        return new PostNaverMailContentImportResponseDto(mailCount);
    }
}
