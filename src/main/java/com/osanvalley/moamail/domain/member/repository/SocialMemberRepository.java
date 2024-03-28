package com.osanvalley.moamail.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osanvalley.moamail.domain.member.entity.SocialMember;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {
    Optional<SocialMember> findBySocialId(String socialId);
}
