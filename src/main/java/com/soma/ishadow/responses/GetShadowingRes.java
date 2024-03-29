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

    @JsonProperty("videoName")
    private final String videoName;

    @JsonProperty("videoEvaluation")
    private final Boolean videoEvaluation;

    @JsonProperty("videoURL")
    private final String videoURL;

    @JsonProperty("thumbNailURL")
    private final String thumbNailURL;

    @JsonProperty("sentences")
    private final List<GetSentenceEnRes> sentences = new ArrayList<>();

    @JsonCreator
    @Builder
    public GetShadowingRes(Long videoId, String videoName, Boolean videoEvaluation, String videoURL, String thumbNailURL) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoEvaluation = videoEvaluation;
        this.videoURL = videoURL;
        this.thumbNailURL = thumbNailURL;
    }

    public String getThumbNailURL() {
        return thumbNailURL;
    }

    public Boolean getVideoEvaluation() {
        return videoEvaluation;
    }

    public String getVideoName() {
        return videoName;
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
