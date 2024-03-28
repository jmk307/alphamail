package com.osanvalley.moamail.domain.mail.service;

import com.osanvalley.moamail.domain.mail.entity.NaverMailMember;
import com.osanvalley.moamail.domain.mail.repository.NaverMailRepository;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;

@Service
public class NaverMailService {
    private final NaverMailRepository naverMailRepository;

    public NaverMailService(NaverMailRepository naverMailRepository) {
        this.naverMailRepository = naverMailRepository;
    }
    /**
     * 정보 : Naver Mail 서비스 연동을 위해 사용자 정보 저장
     * @param member Repository에 전달할 사용자 정보
     */
    public void join(NaverMailMember member) {
        naverMailRepository.saveConnectInfo(member);
    }


    /**
     * 정보 : Naver Mail 서비스 연동을 위해 사용할 사용자 정보 반환
     * @param memberId 인-메모리 DB에 저장되어 있는 사용자 정보
     * @return member 사용자 정보
     */
    public NaverMailMember getMailConnectInfo(String memberId) {
        NaverMailMember member = naverMailRepository.findConnectInfoByMemberId(memberId);
        return member;
    }

    /**
     * 정보 : 메일 커넥션을 맺은뒤 메일 반환
     * TODO : Controller에서 메일을 열어 볼 수 있게 Repository 에서 메일 가져오는 메서드
     * @param mailCount
     */
    public void getMailContents(int mailCount, String memberId) throws MessagingException {
    }

    /**
     * 정보 : Console에 메일 불러온 후 출력하기
     * @param mailCount 불러올 메일의 개수
     * @param memberId 메일을 불러오기 위한 계정 주인
     * @throws MessagingException
     * @throws IOException
     */
    public void printNaverMailContents(int mailCount, String memberId) throws MessagingException, IOException {
        NaverMailMember member = naverMailRepository.findConnectInfoByMemberId(memberId);
        NaverMailConnector connector = new NaverMailConnector(member);
        connector.connect();
        Message[] messages = connector.getMessages();

        for(int i = messages.length - 1; i >= messages.length - mailCount; i--) {
            Message message = messages[i];
            System.out.printf("컨텐츠타임: %s%n", message.getContentType());
            System.out.printf("발신자[0]: %s%n", message.getFrom()[0]);
            System.out.printf("메일제목: %s%n", message.getSubject());
            System.out.printf("메일내용: %s%n", message.getContent());
            System.out.println("==================================");
        }

        connector.disconnect();
    }
}
