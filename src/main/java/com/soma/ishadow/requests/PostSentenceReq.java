package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PostSentenceReq {

    @JsonProperty("videoId")
    private final Long videoId;

    @JsonProperty("sentences")
    private final List<Long> sentences;

    @JsonProperty("sentenceSaveType")
    private final String sentenceSaveType;

    public PostSentenceReq(Long videoId, List<Long> sentences, String sentenceSaveType) {
        this.videoId = videoId;
        this.sentences = sentences;
        this.sentenceSaveType = sentenceSaveType;
    }

    public Long getVideoId() {
        return videoId;
    }

    public List<Long> getSentences() {
        return sentences;
    }

    public String getSentenceSaveType() {
        return sentenceSaveType;
    }
}
