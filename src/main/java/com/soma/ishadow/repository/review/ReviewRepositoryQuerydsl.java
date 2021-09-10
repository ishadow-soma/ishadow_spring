package com.soma.ishadow.repository.review;

import com.soma.ishadow.domains.review.Review;

import java.util.Optional;

public interface ReviewRepositoryQuerydsl {
    Review save(Review review);

    Optional<Review> findReviewByUserAndVideo(Long userId, Long videoId);
}
