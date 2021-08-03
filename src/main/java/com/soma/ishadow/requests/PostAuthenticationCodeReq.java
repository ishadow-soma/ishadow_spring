package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class PostAuthenticationCodeReq {

    private final String authenticationCode;

    public String getAuthenticationCode() {
        return authenticationCode;
    }

    @Builder
    @JsonCreator
    public PostAuthenticationCodeReq(
            @JsonProperty("authenticationCode") String authenticationCode) {
        this.authenticationCode = authenticationCode;
    }
}
