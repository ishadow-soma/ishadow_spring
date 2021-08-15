package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

public class GetSentenceEn {

    @JsonProperty
    private final String videoURL;

    @JsonProperty
    private final String videoTitle;

    @JsonProperty
    private final List<GetSentenceEn> sentences = new ArrayList<>();

    @JsonCreator
    @Builder
    public GetSentenceEn(String videoURL, String videoTitle) {
        this.videoURL = videoURL;
        this.videoTitle = videoTitle;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public List<GetSentenceEn> getSentences() {
        return sentences;
    }

    public void addSentence(GetSentenceEn sentence) {
        this.sentences.add(sentence);
    }
}
