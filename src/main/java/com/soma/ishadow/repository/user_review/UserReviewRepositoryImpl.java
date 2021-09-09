package com.soma.ishadow.repository.user_review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.user_review.UserReview;
import com.soma.ishadow.domains.user_video.UserVideo;
import com.soma.ishadow.repository.user_video.UserVideoRepositoryQuerydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserReviewRepositoryImpl extends QuerydslRepositorySupport implements UserReviewRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public UserReviewRepositoryImpl(JPAQueryFactory queryFactory, EntityManager em) {
        super(UserReview.class);
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Override
    public UserReview save(UserReview userReview) {
        em.persist(userReview);
        return userReview;
    }
}
