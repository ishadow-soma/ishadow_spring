package com.soma.ishadow.domains.review;

import com.soma.ishadow.domains.enums.Status;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewId", nullable = false, updatable = false)
    private Long reviewId;

    @Column(name = "userId", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "videoId", nullable = false, updatable = false)
    private Long videoId;

    @Column(name = "level")
    private float level;

    @Column(name = "content")
    private String content;

    @Column(name = "createAt")
    private Timestamp createdAt;

    @Column(name = "updateAt")
    private Timestamp updateAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Review(Long reviewId, Long userId, Long videoId, float level, String content, Timestamp createdAt, Timestamp updateAt, Status status) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.videoId = videoId;
        this.level = level;
        this.content = content;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.status = status;
    }

    public Review() {

    }

    public Long getUserId() {
        return userId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public float getLevel() {
        return level;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public Status getStatus() {
        return status;
    }
}
