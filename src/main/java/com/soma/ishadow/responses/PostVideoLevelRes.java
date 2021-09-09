package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PostVideoLevelRes implements Serializable {

    @JsonProperty
    private final Long videoId;

    public PostVideoLevelRes(Long videoId) {
        this.videoId = videoId;
    }

    public Long getVideoId() {
        return videoId;
    }
}
