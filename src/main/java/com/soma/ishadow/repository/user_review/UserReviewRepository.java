package com.soma.ishadow.repository.user_review;

import com.soma.ishadow.domains.user_review.UserReview;
import com.soma.ishadow.domains.user_review.UserReviewId;
import com.soma.ishadow.domains.user_video.UserVideo;
import com.soma.ishadow.domains.user_video.UserVideoId;
import com.soma.ishadow.repository.user_video.UserVideoRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReviewRepository extends JpaRepository<UserReview, UserReviewId>, UserReviewRepositoryQuerydsl {

    UserReview save(UserReview userReview);
}
