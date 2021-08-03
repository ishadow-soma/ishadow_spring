package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class PostEmailReq {

    private final String email;

    public String getEmail() {
        return email;
    }

    @Builder
    @JsonCreator
    public PostEmailReq(@JsonProperty("email") String email) {
        this.email = email;
    }

}
