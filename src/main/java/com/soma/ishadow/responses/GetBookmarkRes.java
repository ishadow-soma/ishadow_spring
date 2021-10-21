package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import lombok.Builder;

public class GetBookmarkRes {

    @JsonProperty("groupId")
    private final Long groupId;

    @JsonProperty("sentenceId")
    private final Long sentenceId;

    @JsonProperty("content")
    private final String content;

    @JsonProperty("startTime")
    private final String startTime;

    @JsonProperty("endTime")
    private final String endTime;

    @JsonCreator
    @Builder
    public GetBookmarkRes(Long groupId, Long sentenceId, String content, String startTime, String endTime) {
        this.groupId = groupId;
        this.sentenceId = sentenceId;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
