package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;

public class PostVideoLevelRes implements Serializable {

    @JsonProperty("videoId")
    private final Long videoId;

    @JsonProperty("videoEvaluation")
    private final Boolean videoEvaluation;

    @JsonProperty("reviewId")
    private final Long reviewId;

    @Builder
    public PostVideoLevelRes(Long videoId, Boolean videoEvaluation, Long reviewId) {
        this.videoId = videoId;
        this.videoEvaluation = videoEvaluation;
        this.reviewId = reviewId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public Boolean getVideoEvaluation() {
        return videoEvaluation;
    }

    public Long getReviewId() {
        return reviewId;
    }
}
