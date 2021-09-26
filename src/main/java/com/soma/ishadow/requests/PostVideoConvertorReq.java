package com.soma.ishadow.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

public class PostVideoConvertorReq {

    @JsonProperty(value = "info")
    private String info;

    @Builder
    public PostVideoConvertorReq(String info) {
        this.info = info;
    }


    public String getInfo() {
        return info;
    }
}
