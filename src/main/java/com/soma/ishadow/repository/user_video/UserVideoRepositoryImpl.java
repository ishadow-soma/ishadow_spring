package com.soma.ishadow.repository.user_video;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.user_video.UserVideo;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserVideoRepositoryImpl extends QuerydslRepositorySupport implements UserVideoRepositoryQuerydsl {


    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public UserVideoRepositoryImpl(JPAQueryFactory queryFactory, EntityManager em) {
        super(UserVideo.class);
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Override
    public UserVideo save(UserVideo userVideo) {
        em.persist(userVideo);
        return userVideo;
    }
}
