package com.osanvalley.moamail.domain.member.repository;

import com.osanvalley.moamail.domain.member.entity.SocialMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {
    Optional<SocialMember> findBySocialId(String socialId);

    boolean existsBySocialId(String socialId);
}
