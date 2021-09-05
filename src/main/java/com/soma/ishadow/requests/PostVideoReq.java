package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

public class PostVideoReq {

    private String type;
    private List<Long> categoryId;
    private String youtubeURL;

    @Builder
    @JsonCreator
    public PostVideoReq(
            @JsonProperty("type") String type,
            @JsonProperty("categoryId") List<Long> categoryId,
            @JsonProperty("youtubeURL") String youtubeURL) {
        this.type = type;
        this.categoryId = categoryId;
        this.youtubeURL = youtubeURL;
    }


    public PostVideoReq() {

    }

    public String getType() {
        return type;
    }

    public List<Long> getCategoryId() {
        return categoryId;
    }

    public String getYoutubeURL() {
        return youtubeURL;
    }
}
