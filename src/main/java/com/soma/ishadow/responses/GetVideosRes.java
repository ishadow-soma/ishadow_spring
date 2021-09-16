package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

public class GetVideosRes {


    @JsonProperty("videoList")
    private List<GetVideoRes> videoList = new ArrayList<>();


    @JsonProperty("pageStartNumber")
    private int pageStartNumber;

    @JsonProperty("currentPageNumber")
    private int currentPageNumber;

    @JsonProperty("pageEndNumber")
    private int pageEndNumber;

    @Builder
    public GetVideosRes(List<GetVideoRes> videoList, int pageStartNumber, int currentPageNumber, int pageEndNumber) {
        this.videoList = videoList;
        this.pageStartNumber = pageStartNumber;
        this.currentPageNumber = currentPageNumber;
        this.pageEndNumber = pageEndNumber;
    }

    public List<GetVideoRes> getVideoList() {
        return videoList;
    }

    public int getPageStartNumber() {
        return pageStartNumber;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public int getPageEndNumber() {
        return pageEndNumber;
    }

    public void addVideoList(List<GetVideoRes> videoList) {
        this.videoList = videoList;
    }
}
