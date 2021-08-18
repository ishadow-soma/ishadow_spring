package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;

public class PostVideoRes implements Serializable {

    @JsonProperty
    private final Long videoId;


    @JsonProperty
    private final String videoName;

    @JsonProperty
    private final String url;

    @Builder
    public PostVideoRes(Long videoId, String videoName, String url) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.url = url;
    }

}
