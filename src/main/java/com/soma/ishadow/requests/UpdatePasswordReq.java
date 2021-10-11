package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class UpdatePasswordReq {

    private final String password;
    private final String confirmPassword;

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    @Builder
    @JsonCreator
    public UpdatePasswordReq(
            @JsonProperty("password") String password,
            @JsonProperty("confirmPassword") String confirmPassword) {
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
