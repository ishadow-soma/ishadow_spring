package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;

public class PostVideoLevelRes implements Serializable {

    @JsonProperty("videoId")
    private final Long videoId;

    @JsonProperty("videoEvaluate")
    private final Boolean videoEvaluate;

    @JsonProperty("reviewId")
    private final Long reviewId;

    @Builder
    public PostVideoLevelRes(Long videoId, Boolean videoEvaluate, Long reviewId) {
        this.videoId = videoId;
        this.videoEvaluate = videoEvaluate;
        this.reviewId = reviewId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public Boolean getVideoEvaluate() {
        return videoEvaluate;
    }

    public Long getReviewId() {
        return reviewId;
    }
}
