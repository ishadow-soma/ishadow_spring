package com.soma.ishadow.domains.sentence_en;

import com.soma.ishadow.domains.audio.Audio;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.user.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sentence_en")
public class SentenceEn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sentenceId", nullable = false, updatable = false)
    private Long sentenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audioId",nullable = false)
    private Audio audio;

    @Column(name = "content")
    private String content;

    @Column(name = "startTime")
    private String startTime;

    @Column(name = "endTime")
    private String endTime;

    @Column(name = "speaker")
    private String speaker;

    @Column(name = "confidence")
    private String confidence;

    @Column(name = "createAt")
    private Timestamp createdAt;

    @Column(name = "updateAt")
    private Timestamp updateAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public SentenceEn(Long sentenceId, Audio audio, String content, String startTime, String endTime, String speaker, String confidence, Timestamp createdAt, Timestamp updateAt, Status status) {
        this.sentenceId = sentenceId;
        this.audio = audio;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.speaker = speaker;
        this.confidence = confidence;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.status = status;
    }

    public SentenceEn() {

    }

    public Long getSentenceId() {
        return sentenceId;
    }

    public Audio getAudio() {
        return audio;
    }

    public String getContent() {
        return content;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getConfidence() {
        return confidence;
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

    static public class Builder {
        private Long sentenceId;
        private Audio audio;
        private String content;
        private String startTime;
        private String endTime;
        private String speaker;
        private String confidence;
        private Timestamp createdAt;
        private Timestamp updateAt;
        @Enumerated(EnumType.STRING)
        private Status status;

        public Builder sentenceId(Long sentenceId) {
            this.sentenceId = sentenceId;
            return this;
        }

        public Builder audio(Audio audio) {
            this.audio = audio;
            return this;
        }
        public Builder content(String content) {
            this.content = content;
            return this;
        }
        public Builder startTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder speaker(String speaker) {
            this.speaker = speaker;
            return this;
        }

        public Builder confidence(String confidence) {
            this.confidence = confidence;
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

        public SentenceEn build() {
            return new SentenceEn(sentenceId, audio, content, startTime, endTime, speaker, confidence, createdAt, updateAt, status);
        }
    }
}
