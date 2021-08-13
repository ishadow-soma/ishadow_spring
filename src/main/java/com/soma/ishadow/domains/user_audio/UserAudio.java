package com.soma.ishadow.domains.user_audio;

import com.soma.ishadow.domains.audio.Audio;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.user.User;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "user_audio")
public class UserAudio implements Serializable {

    @EmbeddedId
    private UserAudioId userAudioId;

    @MapsId(value = "userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @MapsId(value = "audioId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audioId",nullable = false)
    private Audio audio;

    @Column(name = "createAt")
    private Timestamp createdAt;

    @Column(name = "updateAt")
    private Timestamp updateAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public UserAudio(UserAudioId userAudioId, User user, Audio audio, Timestamp createdAt, Timestamp updateAt, Status status) {
        this.userAudioId = userAudioId;
        this.user = user;
        this.audio = audio;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.status = status;
    }

    public void addUser() {

    }

    public void addAudio() {

    }

    public UserAudioId getUserAudioId() {
        return userAudioId;
    }

    public User getUser() {
        return user;
    }

    public Audio getAudio() {
        return audio;
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

    public UserAudio() {

    }
}
