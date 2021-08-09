package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import static org.springframework.beans.BeanUtils.copyProperties;

public class JwtRes {

    @JsonProperty("email")
    private final String email;

    @JsonProperty("jwt")
    private final String jwt;

    public String getJwt() {
        return jwt;
    }

    @Builder
    @JsonCreator
    public JwtRes(String email, String jwt) {
        this.email = email;
        this.jwt = jwt;
    }
}
