package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soma.ishadow.domains.sentence_en.SentenceEn;
import lombok.Builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetShadowingRes implements Serializable {


    @JsonProperty("videoId")
    private final Long videoId;

    @JsonProperty("videoURL")
    private final String videoURL;

    @JsonProperty("sentences")
    private final List<GetSentenceEnRes> sentences = new ArrayList<>();

    @JsonCreator
    @Builder
    public GetShadowingRes(Long videoId, String videoURL) {
        this.videoId = videoId;
        this.videoURL = videoURL;
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
