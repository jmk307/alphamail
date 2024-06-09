package com.osanvalley.moamail.domain.mail.naver.controller;

import com.osanvalley.moamail.domain.mail.naver.dto.*;
import com.osanvalley.moamail.domain.mail.naver.service.NaverMailService;
import com.osanvalley.moamail.global.config.CommonApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Api(tags = "Naver Mail API")
@RequestMapping("api/naverMail")
public class NaverMailApiController {
    private final NaverMailService naverMailService;

    /**
     * 정보 : 인-메모리 데이터베이스 내에 네이버 메일 사용자를 저장
     * @param reqDto Naver Mail 서비스와 연결을 위해 전달받은 DTO
     */
    @PostMapping("connectInfo/set")
    @ApiOperation(value = "네이버 IMAPS 멤버 정보 셋팅")
    public ResponseEntity<CommonApiResponse<PostNaverImapConnectInfoResponseDto>> setNaverMailMemberInfo(
            @Valid @RequestBody PostNaverImapConnectInfoRequestDto reqDto)
    {
        PostNaverImapConnectInfoResponseDto infoDto = naverMailService.setImapConnectInfo(reqDto.getSocialId(), reqDto.getEmailAddress(), reqDto.getPassword());
        return ResponseEntity.ok(CommonApiResponse.of(infoDto));
    }

    /**
     * 정보 : 인-메모리 데이터베이스 내에 저장된 네이버 메일 사용자 불러오기
     * @param socialId Alpha Mail 서비스의 소셜 아이디
     * @return resDto 사용자의 정보(이메일 주소, 계정 ID) 리턴
     */
    @GetMapping("connectInfo/get")
    @ApiOperation(value = "셋팅된 IMAPS 멤버 정보 조회")
    public ResponseEntity<CommonApiResponse<GetNaverImapConnectInfoResponseDto>> getNaverMailMemberInfo(
            @RequestParam("socialId") String socialId
    ) {
        GetNaverImapConnectInfoResponseDto infoDto = naverMailService.getMailConnectInfo(socialId);
        return ResponseEntity.ok(CommonApiResponse.of(infoDto));
    }

    @PostMapping("content/import/{socialId}")
    @ApiOperation(value = "네이버 메일 DB에 불러오기")
    public ResponseEntity<CommonApiResponse<PostNaverMailContentImportResponseDto>> importNaverMailContents(
            @PathVariable String socialId
    ) throws MessagingException, IOException {
        PostNaverMailContentImportResponseDto resDto = naverMailService.saveMailContents(socialId);
        return ResponseEntity.ok(CommonApiResponse.of(resDto));
    }

    /**
     * TODO : 메일 송신 기능 구현 필요
     */
    @PostMapping("content/send/{socialId}")
    @ApiOperation(value = "네이버 메일 전송하기")
    public ResponseEntity<CommonApiResponse<PostNaverMailSendResponseDto>> sendNaverMailContent(
            @PathVariable String socialId,
            @Valid @RequestBody PostNaverMailSendRequestDto reqDto
        ) throws MessagingException {
        PostNaverMailSendResponseDto resDto = naverMailService.sendNaverMailContent(socialId, reqDto);
        return ResponseEntity.ok(CommonApiResponse.of(resDto));
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
}
