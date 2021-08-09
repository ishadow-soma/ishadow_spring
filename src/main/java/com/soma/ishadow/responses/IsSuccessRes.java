package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soma.ishadow.domains.enums.IsSuccess;
import lombok.Builder;

public class IsSuccessRes {

    //TODO boolean으로 변경하기
    private final IsSuccess isSuccess;
    private String sns;

    @Builder
    @JsonCreator
    public IsSuccessRes(
            @JsonProperty("isSuccess") IsSuccess isSuccess,
            @JsonProperty("sns") String sns) {
        this.isSuccess = isSuccess;
        this.sns = sns;
    }

    @Builder
    @JsonCreator
    public IsSuccessRes(
            @JsonProperty("isSuccess") IsSuccess isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getSns() {
        return sns;
    }

    public IsSuccess getIsSuccess() {
        return isSuccess;
    }
}
