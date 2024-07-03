package com.osanvalley.moamail.domain.mail.google.dto;

import com.osanvalley.moamail.domain.member.entity.Member;
import com.osanvalley.moamail.domain.member.entity.SocialMember;
import lombok.Getter;

@Getter
public class MailEvent {
    private Member member;

    private SocialMember socialMember;

    private String nextPageToken;

    public MailEvent(Member member, SocialMember socialMember, String nextPageToken) {
        this.member = member;
        this.socialMember = socialMember;
        this.nextPageToken = nextPageToken;
    }
}
