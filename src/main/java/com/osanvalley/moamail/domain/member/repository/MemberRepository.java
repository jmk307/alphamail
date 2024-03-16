package com.osanvalley.moamail.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osanvalley.moamail.domain.member.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByAuthId(String authId);

    boolean existsByAuthId(String authId);

    boolean existsByNickname(String nickname);
}
