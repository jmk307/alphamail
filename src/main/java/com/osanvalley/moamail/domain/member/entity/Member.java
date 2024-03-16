package com.osanvalley.moamail.domain.member.entity;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.osanvalley.moamail.domain.member.model.RegisterType;
import com.osanvalley.moamail.domain.member.model.Sex;
import com.osanvalley.moamail.global.config.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String authId;

	private String password;

	private String phoneNumber;

	private String nickname;

	private String birth;

	@Enumerated(EnumType.STRING)
	private RegisterType registerType;

	@Enumerated(EnumType.STRING)
	private Sex sex;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SocialMember> socialMembers = new ArrayList<>();

	@Builder
	public Member(Long id, String authId, String password, String phoneNumber, String nickname, String birth, RegisterType registerType, Sex sex, List<SocialMember> socialMembers) {
		this.id = id;
		this.authId = authId;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.nickname = nickname;
		this.birth = birth;
		this.registerType = registerType;
		this.sex = sex;
		this.socialMembers = socialMembers;
	}
}
