package com.osanvalley.moamail.domain.member.repository;

import java.util.Optional;

import com.osanvalley.moamail.domain.member.model.Social;
import org.springframework.data.jpa.repository.JpaRepository;

import com.osanvalley.moamail.domain.member.entity.SocialMember;
import org.springframework.data.jpa.repository.Query;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {
    Optional<SocialMember> findBySocialId(String socialId);

    Optional<SocialMember> findByMember_AuthIdAndSocial(String authId, Social social);
}
