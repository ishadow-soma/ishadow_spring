package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class PostVideoRes {

    private final Long videoId;

    @Builder
    @JsonCreator
    public PostVideoRes(
            @JsonProperty("videoId") Long videoId) {
        this.videoId = videoId;
    }
}
