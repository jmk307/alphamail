package com.osanvalley.moamail.domain.member.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.osanvalley.moamail.domain.member.model.Social;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;

import javax.persistence.FetchType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialMember {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

    private String email;

    @Enumerated(EnumType.STRING)
    private Social social;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public SocialMember(Long id, String socialId, String email, Social social, Member member) {
        this.id = id;
        this.socialId = socialId;
        this.email = email;
        this.social = social;
        this.member = member;
    }
}
