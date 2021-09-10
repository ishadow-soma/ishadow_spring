package com.soma.ishadow.repository.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.review.QReview;
import com.soma.ishadow.domains.review.Review;
import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.domains.user_review.QUserReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

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

    @Override
    public Optional<Review> findReviewByUserAndVideo(Long userId, Long videoId) {
        QReview review = QReview.review;
        return Optional.ofNullable(queryFactory.selectFrom(review)
                .where(review.userId.eq(userId), review.videoId.eq(videoId), review.status.eq(Status.YES))
                .fetchOne());
    }
}
