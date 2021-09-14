package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


public class GetVideoRes {

    @JsonProperty("videoId")
    private final Long videoId;

    @JsonProperty("videoName")
    private final String videoName;

    @JsonProperty("videoURL")
    private final String videoURL;

    @JsonProperty("thumbNailURL")
    private final String thumbNailURL;

    @JsonProperty("videoLevel")
    private final float videoLevel;

    @JsonProperty("categoryId")
    private final Long categoryId;

    @JsonProperty("categoryName")
    private final String categoryName;

    @Builder
    @JsonCreator
    public GetVideoRes(Long videoId, String videoName, String videoURL, String thumbNailURL, float videoLevel, Long categoryId, String categoryName) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoURL = videoURL;
        this.thumbNailURL = thumbNailURL;
        this.videoLevel = videoLevel;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getVideoId() {
        return videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbNailURL() {
        return thumbNailURL;
    }

    public float getVideoLevel() {
        return videoLevel;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
