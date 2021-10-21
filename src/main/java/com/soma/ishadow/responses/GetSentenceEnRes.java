package com.soma.ishadow.responses;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class GetSentenceEnRes {

    @JsonProperty("sentenceId")
    private final Long sentenceId;

    @JsonProperty("content")
    private final String content;

    @JsonProperty("startTime")
    private final String startTime;

    @JsonProperty("endTime")
    private final String endTime;


    @Builder
    @JsonCreator
    public GetSentenceEnRes(Long sentenceId, String content, String startTime, String endTime) {
        this.sentenceId = sentenceId;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
