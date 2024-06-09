package com.osanvalley.moamail.domain.mail.repository;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import com.osanvalley.moamail.domain.mail.entity.QMail;
import com.osanvalley.moamail.domain.member.entity.Member;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MailCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<Mail> findAllReceivedMails(Member member, List<String> mails, Pageable pageable) {
        List<Mail> fetch = jpaQueryFactory
                .selectFrom(QMail.mail)
                .where(QMail.mail.socialMember.member.eq(member),
                        filterReceivedMails(mails))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Mail> count = jpaQueryFactory
                .selectFrom(QMail.mail)
                .where(QMail.mail.socialMember.member.eq(member),
                        filterReceivedMails(mails));

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchCount);
    }

    private BooleanExpression filterReceivedMails(List<String> mails) {
        BooleanExpression expression = null;
        for (String mail : mails) {
            if (expression == null) {
                expression = QMail.mail.toEmailReceivers.like("%" + mail + "%");
            } else {
                expression = expression.or(QMail.mail.toEmailReceivers.like("%" + mail + "%"));
            }
        }
        return expression;
    }
}
