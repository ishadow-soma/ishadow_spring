package com.soma.ishadow.responses;


import com.fasterxml.jackson.annotation.JsonProperty;

public class PostSentenceRes {

    @JsonProperty("groupId")
    private final Long groupId;

    public PostSentenceRes(Long groupId) {
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }
}
