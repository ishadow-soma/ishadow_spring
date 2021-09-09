package com.soma.ishadow.domains.user_review;

import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.review.Review;
import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.domains.user_video.UserVideoId;
import com.soma.ishadow.domains.video.Video;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "user_review")
public class UserReview implements Serializable {

    @EmbeddedId
    private UserReviewId userReviewId;

    @MapsId(value = "userId")
    @ManyToOne
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @MapsId(value = "reviewId")
    @ManyToOne
    @JoinColumn(name = "reviewId",nullable = false)
    private Review review;

    @Column(name = "createAt")
    private Timestamp createdAt;

    @Column(name = "updateAt")
    private Timestamp updateAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public UserReview(UserReviewId userReviewId, User user, Review review, Timestamp createdAt, Timestamp updateAt, Status status) {
        this.userReviewId = userReviewId;
        this.user = user;
        this.review = review;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.status = status;
    }
    public UserReview() {

    }

    public UserReviewId getUserReviewId() {
        return userReviewId;
    }

    public User getUser() {
        return user;
    }

    public Review getReview() {
        return review;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public Status getStatus() {
        return status;
    }
}
