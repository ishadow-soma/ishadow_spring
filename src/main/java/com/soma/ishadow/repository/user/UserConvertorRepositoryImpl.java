package com.soma.ishadow.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.user.QUserConvertor;
import com.soma.ishadow.domains.user.UserConvertor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class UserConvertorRepositoryImpl implements UserConvertorRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;


    private final EntityManager em;

    public UserConvertorRepositoryImpl(JPAQueryFactory queryFactory, EntityManager em) {
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Override
    public Optional<UserConvertor> findById(Long userId) {
        QUserConvertor qUserConvertor = QUserConvertor.userConvertor;
        return Optional.ofNullable(queryFactory.selectFrom(qUserConvertor)
                .where(qUserConvertor.userId.eq(userId))
                .fetchOne());
    }
}
