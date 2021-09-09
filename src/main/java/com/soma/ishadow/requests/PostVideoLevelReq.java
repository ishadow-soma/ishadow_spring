package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostVideoLevelReq {

    @JsonProperty("videoLevel")
    private final float videoLevel;

    public PostVideoLevelReq(float videoLevel) {
        this.videoLevel = videoLevel;
    }

    public float getVideoLevel() {
        return videoLevel;
    }
}
