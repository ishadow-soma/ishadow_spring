package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;

public class PostVideoRes implements Serializable {

    @JsonProperty
    private final Long videoId;

    @Builder
    public PostVideoRes(Long videoId) {
        this.videoId = videoId;
    }

}
