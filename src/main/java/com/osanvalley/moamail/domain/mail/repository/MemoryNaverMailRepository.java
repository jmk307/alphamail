package com.osanvalley.moamail.domain.mail.repository;

import com.osanvalley.moamail.domain.mail.entity.NaverMailMember;
import org.springframework.stereotype.Repository;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MemoryNaverMailRepository implements NaverMailRepository {
    private static final Map<String, NaverMailMember> memberStore = new HashMap<>();

    /**
     * 정보 : 인-메모리 DB에 사용자 저장하기
     * @param member 인-메모리 DB에 저장할 사용자 정보
     */
    @Override
    public void saveConnectInfo(NaverMailMember member) {
        memberStore.put(member.getMemberId(), member);
    }

    /**
     * 정보 : 인-메모리 DB에서 사용자 불러오기
     * @param memberId 인-메모리 DB에서 사용할 키 값
     * @return findMember 사용자 정보를 반환
     */
    @Override
    public NaverMailMember findConnectInfoByMemberId(String memberId) {
        NaverMailMember findMember = memberStore.get(memberId);
        return findMember;
    }

    @Override
    public void saveContents(Message message) throws MessagingException, IOException {
//        로컬 테스트용에서는 따로 저장하지 않고 콘솔에 출력
    }

    @Override
    public Message[] findByMailMessages() {
//        로컬 테스트용에서는 구현 대신 콘솔에 출력
        return new Message[0];
    }

}
