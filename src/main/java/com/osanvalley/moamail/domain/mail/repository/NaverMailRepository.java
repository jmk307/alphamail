package com.osanvalley.moamail.domain.mail.repository;

import com.osanvalley.moamail.domain.mail.entity.NaverMailMember;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;

public interface NaverMailRepository {
    public void saveConnectInfo(NaverMailMember naverMailMember);
    public NaverMailMember findConnectInfoByMemberId(String memberId);
    public void saveContents(Message message) throws MessagingException, IOException;
    public Message[] findByMailMessages();
}
