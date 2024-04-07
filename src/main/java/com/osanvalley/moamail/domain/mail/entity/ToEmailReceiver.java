package com.osanvalley.moamail.domain.mail.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToEmailReceiver {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String toEmailReceiver;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mail mail;
    
    @Builder
    public ToEmailReceiver(Long id, String toEmailReceiver, Mail mail) {
        this.id = id;
        this.toEmailReceiver = toEmailReceiver;
        this.mail = mail;
    }
}
