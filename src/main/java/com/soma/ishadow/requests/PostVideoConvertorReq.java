package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class PostVideoConvertorReq {

    private final String info;

    @Builder
    @JsonCreator
    public PostVideoConvertorReq(
            @JsonProperty("info") String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
