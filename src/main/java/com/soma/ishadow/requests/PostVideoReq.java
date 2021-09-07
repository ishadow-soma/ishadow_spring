package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostVideoReq{

    @JsonProperty("type")
    private String type;

    @JsonProperty("categoryId")
    private List<Long> categoryId = new ArrayList<>();

    @JsonProperty("youtubeURL")
    private String youtubeURL;

    @Builder
    public PostVideoReq(String type, String youtubeURL) {
        this.type = type;
        this.youtubeURL = youtubeURL;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategoryId(List<Long> categoryId) {
        this.categoryId = categoryId;
    }

    public void setYoutubeURL(String youtubeURL) {
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
