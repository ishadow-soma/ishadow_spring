package com.soma.ishadow.domains.user_video;

import com.soma.ishadow.domains.video.Video;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.user.User;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "user_audio")
public class UserVideo implements Serializable {

    @EmbeddedId
    private UserVideoId userVideoId;

    @MapsId(value = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @MapsId(value = "videoId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "videoId",nullable = false)
    private Video video;

    @Column(name = "createAt")
    private Timestamp createdAt;

    @Column(name = "updateAt")
    private Timestamp updateAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public UserVideo(UserVideoId userVideoId, User user, Video video, Timestamp createdAt, Timestamp updateAt, Status status) {
        this.userVideoId = userVideoId;
        this.user = user;
        this.video = video;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.status = status;
    }

    public void addUser() {

    }

    public void addAudio() {

    }

    public UserVideoId getUserVideoId() {
        return userVideoId;
    }

    public User getUser() {
        return user;
    }

    public Video getVideo() {
        return video;
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

    public UserVideo() {

    }
}
