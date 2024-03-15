package com.osanvalley.moamail.domain.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osanvalley.moamail.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByPhoneNumber(String phoneNumber);
}
