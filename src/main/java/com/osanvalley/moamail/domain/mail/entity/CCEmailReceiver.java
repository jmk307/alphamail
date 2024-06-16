package com.osanvalley.moamail.domain.mail.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CCEmailReceiver {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ccEmailReceiver;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mail mail;
    
    @Builder
    public CCEmailReceiver(Long id, String ccEmailReceiver, Mail mail) {
        this.id = id;
        this.ccEmailReceiver = ccEmailReceiver;
        this.mail = mail;
    }
}
