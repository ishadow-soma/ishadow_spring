package com.soma.ishadow.responses;

import lombok.Builder;

public class YoutubeVideo {

    private final Long videoId;
    private final String title;
    private final String thumbNailURL;

    @Builder
    public YoutubeVideo(Long videoId, String title, String thumbNailURL) {
        this.videoId = videoId;
        this.title = title;
        this.thumbNailURL = thumbNailURL;
    }

    public Long getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbNailURL() {
        return thumbNailURL;
    }
}
