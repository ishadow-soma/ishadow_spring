package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;

public class PostVideoLevelRes implements Serializable {

    @JsonProperty("videoId")
    private final Long videoId;

    @JsonProperty("reviewId")
    private final Long reviewId;

    @Builder
    public PostVideoLevelRes(Long videoId, Long reviewId) {
        this.videoId = videoId;
        this.reviewId = reviewId;
    }

    public Long getVideoId() {
        return videoId;
    }
}
