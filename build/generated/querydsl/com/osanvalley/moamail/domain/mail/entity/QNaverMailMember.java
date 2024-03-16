package com.osanvalley.moamail.domain.mail.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNaverMailMember is a Querydsl query type for NaverMailMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNaverMailMember extends EntityPathBase<NaverMailMember> {

    private static final long serialVersionUID = 469468204L;

    public static final QNaverMailMember naverMailMember = new QNaverMailMember("naverMailMember");

    public final StringPath emailAddress = createString("emailAddress");

    public final StringPath memberId = createString("memberId");

    public final StringPath password = createString("password");

    public QNaverMailMember(String variable) {
        super(NaverMailMember.class, forVariable(variable));
    }

    public QNaverMailMember(Path<? extends NaverMailMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNaverMailMember(PathMetadata metadata) {
        super(NaverMailMember.class, metadata);
    }

}

