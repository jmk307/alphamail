package com.osanvalley.moamail.domain.mail.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
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

}
