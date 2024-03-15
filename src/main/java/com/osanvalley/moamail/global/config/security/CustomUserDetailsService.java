package com.osanvalley.moamail.global.config.security;

import com.osanvalley.moamail.domain.member.MemberRepository;
import com.osanvalley.moamail.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String phoneNumber) {
        return memberRepository.findByPhoneNumber(phoneNumber)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException(phoneNumber + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private User createUser(Member member) {
        return new User(member.getPhoneNumber(), member.getPassword(), authorities());
    }

    private static Collection<? extends GrantedAuthority> authorities() {
        Collection<GrantedAuthority> role = new ArrayList<>();
        role.add(new SimpleGrantedAuthority("ROLE_USER"));
        return role;
    }
}
