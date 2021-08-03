package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import static org.springframework.beans.BeanUtils.copyProperties;

public class JwtRes {

    private final String email;
    private final String jwt;

    public String getJwt() {
        return jwt;
    }

    @Builder
    @JsonCreator
    public JwtRes(

            @JsonProperty("email") String email,
            @JsonProperty("jwt") String jwt) {
        this.email = email;
        this.jwt = jwt;
    }
}
