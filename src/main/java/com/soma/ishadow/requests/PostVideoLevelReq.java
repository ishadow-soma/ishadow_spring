package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostVideoLevelReq {

    @JsonProperty("Level")
    private final float level;

    @JsonProperty("content")
    private final String content;

    public PostVideoLevelReq(float level, String content) {
        this.level = level;
        this.content = content;
    }

    public float getLevel() {
        return level;
    }

    public String getContent() {
        return content;
    }
}
