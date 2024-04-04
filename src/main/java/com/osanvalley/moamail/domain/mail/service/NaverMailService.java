package com.osanvalley.moamail.domain.mail.service;

import com.osanvalley.moamail.domain.mail.dto.GetNaverImapConnectInfoResponseDto;
import com.osanvalley.moamail.domain.mail.entity.NaverMailMember;
import com.osanvalley.moamail.domain.mail.repository.RdsNaverMailRepository;
import com.osanvalley.moamail.domain.mail.repository.RdsNaverMemberRepository;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.global.error.ErrorCode;
import com.osanvalley.moamail.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NaverMailService {
    private final RdsNaverMailRepository naverMailRepository;
    private final RdsNaverMemberRepository naverMemberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 정보 : Naver Mail 서비스 연동을 위해 사용자 정보 저장
     * @param member Repository에 전달할 사용자 정보
     */
    @Transactional
    public void setImapConnectInfo(String memberId, String password, String emailAddress) {
        if(naverMemberRepository.existsByMemberId(memberId)) {
            throw new BadRequestException(ErrorCode.MEMBER_ALREADY_EXIST);
        }

        String encryptedPassword = passwordEncoder.encode(password);

        NaverMailMember encryptedMember = NaverMailMember.builder()
                .memberId(memberId)
                .emailAddress(emailAddress)
                .password(encryptedPassword)
            .build();

        naverMemberRepository.save(encryptedMember);
    }


    /**
     * 정보 : Naver Mail 서비스 연동을 위해 사용할 사용자 정보 반환
     *
     * @param memberId 인-메모리 DB에 저장되어 있는 사용자 정보
     * @return member 사용자 정보
     */
    @Transactional(readOnly = true)
    public GetNaverImapConnectInfoResponseDto getMailConnectInfo(String memberId) {
        Optional<NaverMailMember> member = naverMemberRepository.findByMemberId(memberId);
        return new GetNaverImapConnectInfoResponseDto(
                member.get().getMemberId(),
                member.get().getEmailAddress()
        );
    }

    /**
     * 정보 : 메일 커넥션을 맺은뒤 메일 반환
     * TODO : Controller에서 메일을 열어 볼 수 있게 Repository 에서 메일 가져오는 메서드
     * @param mailCount
     */
    public void getMailContents(int mailCount, String memberId) throws MessagingException {
    }

    public void saveNaverMailContents(SocialMember member) throws MessagingException, IOException {
//        Optional<NaverMailMember> member = naverMemberRepository.findByMemberId(member.get().getMemberId());
//        NaverMailConnector connector = new NaverMailConnector(member.get());
//        connector.connect();
//        Message[] messages = connector.getMessages();
//
////       중복 검증 로직 필요
////       중복으로 저장되는 경우가 발생할 수 있음.
//        for(int i = messages.length - 1; i >= messages.length - 10; i--) {
//            Message message = messages[i];
//
//            Mail mail = Mail.builder()
//                    .socialMember()
//                    .social()
//                    .title()
//                    .fromEmail()
//                    .toEmails()
//                    .ccEmails()
//                    .content()
//                .build();
//
//            naverMailRepository.save(mail);
//        }
//
//        connector.disconnect();
    }

    /**
     * 정보 : Console에 메일 불러온 후 출력하기
     * @param mailCount 불러올 메일의 개수
     * @param memberId 메일을 불러오기 위한 계정 주인
     * @throws MessagingException
     * @throws IOException
     */
//    public void printNaverMailContents(int mailCount, String memberId) throws MessagingException, IOException {
//        NaverMailMember member = naverMailRepository.findConnectInfoByMemberId(memberId);
//        NaverMailConnector connector = new NaverMailConnector(member);
//        connector.connect();
//        Message[] messages = connector.getMessages();
//
//        for(int i = messages.length - 1; i >= messages.length - mailCount; i--) {
//            Message message = messages[i];
//            System.out.printf("컨텐츠타임: %s%n", message.getContentType());
//            System.out.printf("발신자[0]: %s%n", message.getFrom()[0]);
//            System.out.printf("메일제목: %s%n", message.getSubject());
//            System.out.printf("메일내용: %s%n", message.getContent());
//            System.out.println("==================================");
//        }
//
//        connector.disconnect();
//    }
}
