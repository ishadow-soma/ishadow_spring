package com.soma.ishadow.domains.category_video;

import com.soma.ishadow.domains.category.Category;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.domains.user_video.UserVideoId;
import com.soma.ishadow.domains.video.Video;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "category_video")
public class CategoryVideo implements Serializable {

    @EmbeddedId
    private CategoryVideoId categoryVideoId;

    @MapsId(value = "videoId")
    @ManyToOne
    @JoinColumn(name = "videoId",nullable = false)
    private Video video;

    @MapsId(value = "categoryId")
    @ManyToOne
    @JoinColumn(name = "categoryId",nullable = false)
    private Category category;

    @Column(name = "createAt")
    private Timestamp createdAt;

    @Column(name = "updateAt")
    private Timestamp updateAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public CategoryVideo(CategoryVideoId categoryVideoId, Video video, Category category, Timestamp createdAt, Timestamp updateAt, Status status) {
        this.categoryVideoId = categoryVideoId;
        this.video = video;
        this.category = category;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.status = status;
    }

    public CategoryVideo() {

    }

    public CategoryVideoId getCategoryVideoId() {
        return categoryVideoId;
    }

    public Video getVideo() {
        return video;
    }

    public Category getCategory() {
        return category;
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
