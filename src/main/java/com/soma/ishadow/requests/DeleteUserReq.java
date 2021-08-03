package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class DeleteUserReq {
    private final String purposeOfUse;

    @Builder
    @JsonCreator
    public DeleteUserReq(
            @JsonProperty("purposeOfUse") String purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
    }

    public String getPurposeOfUse() {
        return purposeOfUse;
    }
}
