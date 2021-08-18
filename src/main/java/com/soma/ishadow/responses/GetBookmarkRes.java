package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

public class GetBookmarkRes {

    @JsonProperty("groupId")
    private final Long groupId;

    @JsonProperty("sentences")
    private final List<Long> sentences;

    @Builder
    @JsonCreator
    public GetBookmarkRes(Long groupId, List<Long> sentences) {
        this.groupId = groupId;
        this.sentences = sentences;
    }

    public Long getGroupId() {
        return groupId;
    }

    public List<Long> getSentences() {
        return sentences;
    }

    public void addSentences(Long sentenceId) {
        this.sentences.add(sentenceId);
    }
}
