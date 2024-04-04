package com.osanvalley.moamail.domain.mail.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NaverMailMember {
    @Getter
    @Setter
    @NonNull
    @Id
    private String memberId;

    @Getter
    @Setter
    @NonNull
    private String emailAddress;

    @Getter
    @Setter
    @NonNull
    private String password;

    public NaverMailMember infoToEntity(String password) {
        return NaverMailMember.builder()
                .memberId(this.getMemberId())
                .emailAddress(this.getEmailAddress())
                .password(password)
                .build();
    }

    @Builder
    public NaverMailMember(String memberId, String emailAddress, String password) {
        this.memberId = memberId;
        this.emailAddress = emailAddress;
        this.password = password;
    }
}