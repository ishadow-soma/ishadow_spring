package com.soma.ishadow.providers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.domains.review.Review;
import com.soma.ishadow.repository.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.soma.ishadow.configures.BaseResponseStatus.ALREADY_EXISTED_REVIEW;

@Service
@Transactional(readOnly = true)
public class ReviewProvider {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewProvider(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Boolean reviewCheck(Long userId, Long videoId) throws BaseException {

        Optional<Review> review = reviewRepository.findReviewByUserAndVideo(userId, videoId);

        //review 존재 -> 이미 난이도 평가
        if(review.isPresent()) return true;

        //난이도 평가 아직 하지 않음
        return false;

    }
}
