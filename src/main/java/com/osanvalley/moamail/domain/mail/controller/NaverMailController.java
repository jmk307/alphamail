package com.osanvalley.moamail.domain.mail.controller;

import com.osanvalley.moamail.domain.mail.dto.NaverMailSetContentsRequestDto;
import com.osanvalley.moamail.domain.mail.dto.NaverMailMemberRequestDto;
import com.osanvalley.moamail.domain.mail.dto.NaverMailMemberResponseDto;
import com.osanvalley.moamail.domain.mail.entity.NaverMailMember;
import com.osanvalley.moamail.domain.mail.service.NaverMailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class NaverMailController {
    private final NaverMailService naverMailService;

    public NaverMailController(NaverMailService naverMailService) {
        this.naverMailService = naverMailService;
    }

    /**
     * 정보 : 인-메모리 데이터베이스 내에 네이버 메일 사용자를 저장
     * @param reqDto Naver Mail 서비스와 연결을 위해 전달받은 DTO
     */
    @PostMapping("mail/naver/set-connect-info")
    public void setNaverMailMemberInfo(NaverMailMemberRequestDto reqDto) {
        NaverMailMember naverMailMember = new NaverMailMember(reqDto.getMemberId(), reqDto.getEmailAddress(), reqDto.getPassword());
        naverMailService.join(naverMailMember);
    }

    /**
     * 정보 : 인-메모리 데이터베이스 내에 저장된 네이버 메일 사용자 불러오기
     * @param memberId Alpha Mail 서비스의 meberId
     * @return resDto 사용자의 정보(이메일 주소, 계정 ID) 리턴
     */
    @GetMapping("mail/naver/get-connect-info")
    public NaverMailMemberResponseDto getNaverMailMemberInfo(
            @RequestParam("memberId") String memberId
    ) {
        NaverMailMember member = naverMailService.getMailConnectInfo(memberId);
        NaverMailMemberResponseDto resDto = new NaverMailMemberResponseDto(member.getEmailAddress(), member.getMemberId());
        return resDto;
    }

    @PostMapping("mail/naver/print-mail-contents")
    public void printNaverMailContents(NaverMailSetContentsRequestDto reqDto) throws MessagingException, IOException {
        int mailCount = reqDto.getMailCount();
        String memberId = reqDto.getMemberId();
        naverMailService.printNaverMailContents(mailCount, memberId);
    }

    /**
     * TODO : 어떤 형태로 데이터를 던질지 아직 결정되지 않은 상태이므로, Console에 출력하는 방식으로 구현되어 있음. <br/>
     * 추후에, 반환 형태 변경 필요. <br/>
     * 네이버 메일은 구글 메일과 달리 메일 수신을 위해 별도의 계정 정보가 필요. <br/>
     * 즉, getNaverMailUserInfo 메서드에 의존.
     * @param mailCount 불러올 메일의 개수
     */
//    public void receiveMailContents(String memberId) {
//        Message[] msgArray = naverMailService.getMailContents(10, memberId);
//        return msgArray;
//    }

    /**
     * TODO : 메일 송신 기능 구현 필요
     */
    public void sendMailContent() {
    }
}
