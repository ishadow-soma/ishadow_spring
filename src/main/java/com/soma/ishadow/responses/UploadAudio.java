package com.soma.ishadow.responses;

import lombok.Builder;

public class UploadAudio {

    private final Long videoId;
    private final String title;

    @Builder
    public UploadAudio(Long videoId, String title) {
        this.videoId = videoId;
        this.title = title;
    }

    public Long getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

}
