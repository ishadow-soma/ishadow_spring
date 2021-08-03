package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class PatchUserReq {

    private final String name;
    private final int age;
    private final String gender;
    private final String purposeOfUse;

    @Builder
    @JsonCreator
    public PatchUserReq(
            @JsonProperty("name") String name,
            @JsonProperty("age") int age,
            @JsonProperty("gender") String gender,
            @JsonProperty("purposeOfUse") String purposeOfUse) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.purposeOfUse = purposeOfUse;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPurposeOfUse() {
        return purposeOfUse;
    }
}
