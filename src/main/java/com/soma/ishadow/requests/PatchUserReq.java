package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public class PatchUserReq {

    @JsonProperty("name")
    private String name;

    @JsonProperty("age")
    private int age;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("purposeOfUse")
    private String purposeOfUse;

    @Builder
    @JsonCreator
    public PatchUserReq(
            String name,
            int age,
            String gender,
            String purposeOfUse) {
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
