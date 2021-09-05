package com.soma.ishadow.domains.category_video;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CategoryVideoId implements Serializable {

    @Column(name = "videoId", nullable = false, updatable = false)
    private Long videoId;

    @Column(name = "categoryId", nullable = false, updatable = false)
    private Long categoryId;

    @Builder
    public CategoryVideoId(Long videoId, Long categoryId) {
        this.videoId = videoId;
        this.categoryId = categoryId;
    }

    public CategoryVideoId() {

    }

    @Override
    public String toString() {
        return "CategoryVideoId{" +
                "videoId=" + videoId +
                ", categoryId=" + categoryId +
                '}';
    }
}
