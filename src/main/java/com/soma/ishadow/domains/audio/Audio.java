package com.soma.ishadow.domains.audio;


import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.sentence_en.SentenceEn;
import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.domains.user_audio.UserAudio;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "audio")
public class Audio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audioId", nullable = false, updatable = false)
    private Long audioId;

    @Column(name = "audioName",length = 200)
    private String audioName;

    @Column(name = "audioLength",length = 200)
    private Long audioLength;

    @Column(name = "audioType")
    private String audioType;

    @Column(name = "audioURL", columnDefinition = "TEXT")
    private String audioURL;

    @Column(name = "audioChannel")
    private int audioChannel;

    @Column(name = "audioSampling")
    private int audioSampling;

    @Column(name = "audioCapacity")
    private Long audioCapacity;

    @Column(name = "audioSpeakerCount")
    private int audioSpeakerCount;

    @Column(name = "createAt")
    private Timestamp createdAt;

    @Column(name = "updateAt")
    private Timestamp updateAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "audio",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserAudio> userAudios = new HashSet<>();

    @OneToMany(mappedBy = "audio", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SentenceEn> sentenceEns = new HashSet<>();


    public Audio() {

    }

    public Audio(Long audioId, String audioName, Long audioLength, String audioType, String audioURL, int audioChannel, int audioSampling, Long audioCapacity, int audioSpeakerCount, Timestamp createdAt, Timestamp updateAt, Status status) {
        this.audioId = audioId;
        this.audioName = audioName;
        this.audioLength = audioLength;
        this.audioType = audioType;
        this.audioURL = audioURL;
        this.audioChannel = audioChannel;
        this.audioSampling = audioSampling;
        this.audioCapacity = audioCapacity;
        this.audioSpeakerCount = audioSpeakerCount;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.status = status;
    }

    static public class Builder {

        private Long audioId;
        private String audioName;
        private Long audioLength;
        private String audioType;
        private String audioURL;
        private int audioChannel;
        private int audioSampling;
        private Long audioCapacity;
        private int audioSpeakerCount;
        private Timestamp createdAt;
        private Timestamp updateAt;
        private Status status;

        public Builder() {}

        public Builder(Audio audio) {
            this.audioId = audio.getAudioId();
            this.audioName = audio.getAudioName();
            this.audioLength = audio.getAudioLength();
            this.audioType = audio.getAudioType();
            this.audioURL = audio.getAudioURL();
            this.audioChannel = audio.getAudioChannel();
            this.audioSampling = audio.getAudioSampling();
            this.audioCapacity = audio.getAudioCapacity();
            this.audioSpeakerCount = audio.getAudioSpeakerCount();
            this.createdAt = audio.getCreatedAt();
            this.updateAt = audio.getUpdateAt();
            this.status = audio.getStatus();
        }

        public Builder audioName(String audioName) {
            this.audioName = audioName;
            return this;
        }

        public Builder audioLength(Long audioLength) {
            this.audioLength = audioLength;
            return this;
        }

        public Builder audioType(String audioType) {
            this.audioType = audioType;
            return this;
        }

        public Builder audioURL(String audioURL) {
            this.audioURL = audioURL;
            return this;
        }

        public Builder audioChannel(int audioChannel) {
            this.audioChannel = audioChannel;
            return this;
        }

        public Builder audioCapacity(Long audioCapacity) {
            this.audioCapacity = audioCapacity;
            return this;
        }

        public Builder audioSpeakerCount(int audioSpeakerCount) {
            this.audioSpeakerCount = audioSpeakerCount;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }


        public Builder updateAt(Timestamp updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Audio build() {
            return new Audio(audioId,audioName,audioLength,audioType,audioURL,audioChannel,audioSampling,audioCapacity,audioSpeakerCount,createdAt,updateAt,status);
        }
    }

    public Long getAudioId() {
        return audioId;
    }

    public String getAudioName() {
        return audioName;
    }

    public Long getAudioLength() {
        return audioLength;
    }

    public String getAudioType() {
        return audioType;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public int getAudioChannel() {
        return audioChannel;
    }

    public int getAudioSampling() {
        return audioSampling;
    }

    public Long getAudioCapacity() {
        return audioCapacity;
    }

    public int getAudioSpeakerCount() {
        return audioSpeakerCount;
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
