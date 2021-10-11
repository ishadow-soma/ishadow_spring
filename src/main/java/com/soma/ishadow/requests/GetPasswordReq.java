package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class GetPasswordReq {

    @JsonProperty(value = "email")
    private final String email;

    @JsonProperty(value = "password")
    private final String password;

    @Builder
    @JsonCreator
    public GetPasswordReq(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
