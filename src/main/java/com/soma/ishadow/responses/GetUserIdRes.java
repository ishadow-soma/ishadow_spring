package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class GetUserIdRes {

    @JsonProperty
    private final Long userId;

    @Builder
    public GetUserIdRes(Long userId) {
        this.userId = userId;
    }
}
