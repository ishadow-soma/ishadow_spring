package com.soma.ishadow.domains.user_review;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import java.io.Serializable;

@Embeddable
public class UserReviewId implements Serializable {

    @Column(name = "userId", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "reviewId", nullable = false, updatable = false)
    private Long reviewId;

    @Builder
    public UserReviewId(Long userId, Long reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }

    public UserReviewId() {

    }

    @Override
    public String toString() {
        return "UserReviewId{" +
                "userId=" + userId +
                ", reviewId=" + reviewId +
                '}';
    }
}
