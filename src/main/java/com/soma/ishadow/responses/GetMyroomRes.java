package com.soma.ishadow.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class GetMyroomRes {

    @JsonProperty("youtubeVideos")
    private List<YoutubeVideo> youtubeVideos = new ArrayList<>();

    @JsonProperty("uploadVideos")
    private List<UploadVideo> uploadVideos = new ArrayList<>();

    @JsonProperty("uploadAudios")
    private List<UploadAudio> uploadAudios = new ArrayList<>();

    public List<YoutubeVideo> getYoutubeVideos() {
        return youtubeVideos;
    }

    public void addYoutubeVideos(List<YoutubeVideo> youtubeVideos) {
        this.youtubeVideos.addAll(youtubeVideos);
    }

    public List<UploadVideo> getUploadVideos() {
        return uploadVideos;
    }

    public void addUploadVideos(List<UploadVideo> uploadVideos) {
        this.uploadVideos.addAll(uploadVideos);
    }

    public List<UploadAudio> getUploadAudios() {
        return uploadAudios;
    }

    public void addUploadAudios(List<UploadAudio> uploadAudios) {
        this.uploadAudios.addAll(uploadAudios);
    }
}
