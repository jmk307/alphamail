package com.osanvalley.moamail.domain.mail.repository;

import com.osanvalley.moamail.domain.mail.entity.NaverMailMember;

import javax.mail.Message;

public interface NaverMailRepository {
    public void saveConnectInfo(NaverMailMember naverMailMember);
    public NaverMailMember findConnectInfoByMemberId(String memberId);
    public void saveContents();
    public Message[] findByMailMessages();
}
