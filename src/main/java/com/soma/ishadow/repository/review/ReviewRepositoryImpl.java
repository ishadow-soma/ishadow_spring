package com.soma.ishadow.repository.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.review.Review;
import com.soma.ishadow.domains.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class ReviewRepositoryImpl extends QuerydslRepositorySupport implements ReviewRepositoryQuerydsl {


    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Autowired
    public ReviewRepositoryImpl(JPAQueryFactory queryFactory, EntityManager em) {
        super(Review.class);
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Override
    public Review save(Review review) {
        em.persist(review);
        return review;
    }
}
