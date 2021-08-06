package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class PostVideoReq {

    private String type;
    private String category;
    private String youtubeURL;

    @Builder
    @JsonCreator
    public PostVideoReq(
            @JsonProperty("type") String type,
            @JsonProperty("category") String category,
            @JsonProperty("youtubeURL") String youtubeURL) {
        this.type = type;
        this.category = category;
        this.youtubeURL = youtubeURL;
    }


    public PostVideoReq() {

    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getYoutubeURL() {
        return youtubeURL;
    }
}
