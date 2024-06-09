package com.osanvalley.moamail.domain.member.repository;

import com.osanvalley.moamail.domain.member.entity.SocialMember;
import com.osanvalley.moamail.domain.member.model.RegisterType;
import com.osanvalley.moamail.domain.member.model.Social;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {
    Optional<SocialMember> findBySocialIdAndMember_RegisterType(String socialId, RegisterType registerType);
    Optional<SocialMember> findByMember_AuthIdAndSocial(String authId, Social social);
    boolean existsBySocialId(String socialId);
}
