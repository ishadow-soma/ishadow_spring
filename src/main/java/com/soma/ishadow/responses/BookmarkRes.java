package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

public class BookmarkRes {

    @JsonProperty("groupId")
    private final Long groupId;

    @JsonProperty("sentences")
    private final List<Long> sentences;

    @Builder
    @JsonCreator
    public BookmarkRes(Long groupId, List<Long> sentences) {
        this.groupId = groupId;
        this.sentences = sentences;
    }

    public Long getGroupId() {
        return groupId;
    }

    public List<Long> getSentences() {
        return sentences;
    }
}
