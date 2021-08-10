package com.soma.ishadow.domains.user_audio;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserAudioId implements Serializable {


    @Column(name = "userId", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "audioId", nullable = false, updatable = false)
    private Long audioId;

    @Builder
    public UserAudioId(Long userId, Long audioId) {
        this.userId = userId;
        this.audioId = audioId;
    }

    public UserAudioId() {

    }


}
