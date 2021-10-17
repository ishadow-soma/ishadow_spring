package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class PatchSearchPasswordReq {

    @JsonProperty("name")
    private String name;

    @JsonProperty("age")
    private String email;

    @Builder
    @JsonCreator
    public PatchSearchPasswordReq(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
