package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class PostLoginReq {

    private final String email;
    private final String password;
    private final String sns;
    private final String userToken;

    @Builder
    @JsonCreator
    public PostLoginReq(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("sns") String sns,
            @JsonProperty("userToken") String userToken) {
        this.email = email;
        this.password = password;
        this.sns = sns;
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getSns() {
        return sns;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
