package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soma.ishadow.domains.enums.Status;
import lombok.Builder;

public class DeleteUserRes {

    private final Long userId;
    private final Status status;

    @Builder
    @JsonCreator
    public DeleteUserRes(
            @JsonProperty("userId") Long userId,
            @JsonProperty("status") Status status) {
        this.userId = userId;
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public Status getStatus() {
        return status;
    }
}
