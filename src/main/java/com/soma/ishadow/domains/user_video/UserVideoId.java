package com.soma.ishadow.domains.user_video;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserVideoId implements Serializable {


    @Column(name = "userId", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "videoId", nullable = false, updatable = false)
    private Long videoId;

    @Builder
    public UserVideoId(Long userId, Long videoId) {
        this.userId = userId;
        this.videoId = videoId;
    }

    public UserVideoId() {

    }

    @Override
    public String toString() {
        return "UserVideoId{" +
                "userId=" + userId +
                ", videoId=" + videoId +
                '}';
    }
}
