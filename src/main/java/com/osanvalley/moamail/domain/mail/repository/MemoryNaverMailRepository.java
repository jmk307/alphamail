package com.osanvalley.moamail.domain.mail.repository;

import com.osanvalley.moamail.domain.mail.entity.NaverMailMember;
import org.springframework.stereotype.Repository;

import javax.mail.Message;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MemoryNaverMailRepository implements NaverMailRepository {
    private static Map<String, NaverMailMember> store = new HashMap<>();

    /**
     * 정보 : 인-메모리 DB에 사용자 저장하기
     * @param member 인-메모리 DB에 저장할 사용자 정보
     */
    @Override
    public void saveConnectInfo(NaverMailMember member) {
        store.put(member.getMemberId(), member);
    }


    /**
     * 정보 : 인-메모리 DB에서 사용자 불러오기
     * @param memberId 인-메모리 DB에서 사용할 키 값
     * @return findMember 사용자 정보를 반환
     */
    @Override
    public NaverMailMember findConnectInfoByMemberId(String memberId) {
        NaverMailMember findMember = store.get(memberId);
        return findMember;
    }

    /**
     * TODO : 컨텐츠를 DB에 저장하는 코드
     */
    @Override
    public void saveContents() {

    }

    /**
     * TODO : DB에서 컨텐츠를 가져오는 코드
     */
    @Override
    public Message[] findByMailMessages() {
        return new Message[0];
    }
}
