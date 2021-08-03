package com.soma.ishadow.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.user.QUserConvertor;
import com.soma.ishadow.domains.user.UserConvertor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserConvertorRepositoryImpl extends QuerydslRepositorySupport implements UserConvertorRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public UserConvertorRepositoryImpl(JPAQueryFactory queryFactory) {
        super(UserConvertor.class);
        this.queryFactory = queryFactory;
    }


    @Override
    public Optional<UserConvertor> findById(Long userId) {
        QUserConvertor qUserConvertor = QUserConvertor.userConvertor;
        return Optional.ofNullable(queryFactory.selectFrom(qUserConvertor)
                .where(qUserConvertor.userId.eq(userId))
                .fetchOne());
    }
}
