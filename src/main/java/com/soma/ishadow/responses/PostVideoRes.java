package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;

public class PostVideoRes implements Serializable {

    @JsonProperty
    private final Long videoId;

    @JsonProperty
    private final String url;

    @Builder
    public PostVideoRes(Long videoId, String url) {
        this.videoId = videoId;
        this.url = url;
    }

}
