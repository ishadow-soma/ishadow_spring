package com.soma.ishadow.providers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.domains.review.Review;
import com.soma.ishadow.repository.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.soma.ishadow.configures.BaseResponseStatus.ALREADY_EXISTED_REVIEW;

@Service
@Transactional(readOnly = true)
public class ReviewProvider {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewProvider(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review findReviewByUserAndVideo(Long userId, Long videoId) throws BaseException {

        return reviewRepository.findReviewByUserAndVideo(userId, videoId)
                .orElseThrow(() -> new BaseException(ALREADY_EXISTED_REVIEW));
    }
}
