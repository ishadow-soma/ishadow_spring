package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soma.ishadow.domains.sentence_en.SentenceEn;
import lombok.Builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetSentenceEn implements Serializable {

    @JsonProperty("videoURL")
    private final String videoURL;

    @JsonProperty("sentences")
    private final List<SentenceEn> sentences = new ArrayList<>();

    @JsonCreator
    @Builder
    public GetSentenceEn(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoURL() {
        return videoURL;
    }


    public List<SentenceEn> getSentences() {
        return this.sentences;
    }

    public void addSentence(List<SentenceEn> sentences) {
        this.sentences.addAll(sentences);
    }
}
