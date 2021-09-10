package com.soma.ishadow.repository.review;

import com.soma.ishadow.domains.category.Category;
import com.soma.ishadow.domains.review.Review;
import com.soma.ishadow.repository.category.CategoryRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryQuerydsl {

    Review save(Review review);

    Optional<Review> findReviewByUserAndVideo(Long userId, Long videoId);
}
