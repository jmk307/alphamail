package com.osanvalley.moamail.domain.mail.repository;

import com.osanvalley.moamail.domain.mail.entity.NaverMailMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RdsNaverMemberRepository extends JpaRepository<NaverMailMember, Long> {
    Optional<NaverMailMember> findByMemberId(String memberId);
    boolean existsByMemberId(String naverMailMemberId);
}
