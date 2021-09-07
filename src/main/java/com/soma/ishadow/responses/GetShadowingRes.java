package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetShadowingRes implements Serializable {


    @JsonProperty("videoId")
    private final Long videoId;

    @JsonProperty("videoTitle")
    private final String videoTitle;

    @JsonProperty("videoURL")
    private final String videoURL;

    @JsonProperty("thumbNailURL")
    private final String thumbNailURL;

    @JsonProperty("sentences")
    private final List<GetSentenceEnRes> sentences = new ArrayList<>();

    @JsonCreator
    @Builder
    public GetShadowingRes(Long videoId, String videoTitle, String videoURL, String thumbNailURL) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.videoURL = videoURL;
        this.thumbNailURL = thumbNailURL;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public Long getVideoId() {
        return videoId;
    }

    public List<GetSentenceEnRes> getSentences() {
        return sentences;
    }

    public void addSentence(List<GetSentenceEnRes> sentences) {
        this.sentences.addAll(sentences);
    }
}
