package com.osanvalley.moamail.domain.mail.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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