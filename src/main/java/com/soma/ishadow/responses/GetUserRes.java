package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class GetUserRes {

    private final Long userId;
    private final String name;
    private final String email;
    private final String gender;
    private final int age;
    private final Long myPoint;
    private final String sns;

    @Builder
    @JsonCreator
    public GetUserRes(
            @JsonProperty("userId") Long userId,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("gender") String gender,
            @JsonProperty("age") int age,
            @JsonProperty("myPoint") Long myPoint,
            @JsonProperty("sns") String sns) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.myPoint = myPoint;
        this.sns = sns;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public Long getMyPoint() {
        return myPoint;
    }

    public String getSns() {
        return sns;
    }
}
