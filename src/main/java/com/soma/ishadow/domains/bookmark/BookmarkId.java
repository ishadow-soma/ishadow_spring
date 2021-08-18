package com.soma.ishadow.domains.bookmark;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BookmarkId implements Serializable {

    @Column(name = "groupId", nullable = false, updatable = false)
    private Long groupId;

    @Column(name = "videoId", nullable = false, updatable = false)
    private Long videoId;

    @Column(name = "sentenceId", nullable = false, updatable = false)
    private Long sentenceId;

    @Column(name = "userId", nullable = false, updatable = false)
    private Long userId;

    public Long getGroupId() {
        return groupId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public Long getSentenceId() {
        return sentenceId;
    }

    public Long getUserId() {
        return userId;
    }

    @Builder
    public BookmarkId(Long groupId, Long videoId, Long sentenceId, Long userId) {
        this.groupId = groupId;
        this.videoId = videoId;
        this.sentenceId = sentenceId;
        this.userId = userId;
    }

    public BookmarkId() {

    }
}
