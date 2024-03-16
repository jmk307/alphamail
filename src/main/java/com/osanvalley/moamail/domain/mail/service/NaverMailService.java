package com.osanvalley.moamail.domain.mail.service;

import com.osanvalley.moamail.domain.mail.entity.NaverMailMember;
import com.osanvalley.moamail.domain.mail.repository.NaverMailRepository;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

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
//        NaverMailMember member = naverMailRepository.findConnectInfoByMemberId(memberId);
//        conn.connect();
    }

    /**
     * TODO : Controller에서 메일을 전송할 수 있게 Form을 작성해 주는 메서드
     */
    public void setMailContent() {

    }
}
