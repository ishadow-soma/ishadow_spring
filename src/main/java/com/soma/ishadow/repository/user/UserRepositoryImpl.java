package com.soma.ishadow.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.BaseResponseStatus;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.user.QUser;
import com.soma.ishadow.domains.user.User;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public UserRepositoryImpl(JPAQueryFactory queryFactory, EntityManager em) {
        super(User.class);
        this.queryFactory = queryFactory;
        this.em = em;
    }


    @Override
    public User save(User user) {
        em.persist(user);
        return user;
    }

    @Override
    public List<User> findByAll() {
        QUser user = QUser.user;
        return queryFactory.selectFrom(user)
                .where(user.status.eq(Status.YES))
                .fetch();
    }

    @Override
    public Optional<User> findById(Long userId) {
        QUser user = QUser.user;
        return Optional.ofNullable(queryFactory.selectFrom(user)
                .where(user.userId.eq(userId),user.status.eq(Status.YES))
                .fetchOne());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        QUser user = QUser.user;
        return Optional.ofNullable(queryFactory.selectFrom(user)
                .where(user.email.eq(email),user.status.eq(Status.YES))
                .fetchOne());
    }

    @Override
    public Optional<User> delete(Long userId) throws BaseException {
        Optional<User> user = Optional.ofNullable(findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.FAILED_TO_GET_USER)));
        em.remove(user);
        return user;
    }
}
