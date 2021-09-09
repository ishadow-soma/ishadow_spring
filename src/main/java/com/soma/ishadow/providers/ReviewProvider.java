package com.soma.ishadow.providers;

import com.soma.ishadow.domains.review.Review;
import com.soma.ishadow.repository.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReviewProvider {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewProvider(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * 난이도 조회
     * @param userId
     * @param videoId
     * @return
     */
    public Review findReviewByUserAndVideo(Long userId, Long videoId) {

        return null;
    }
}
