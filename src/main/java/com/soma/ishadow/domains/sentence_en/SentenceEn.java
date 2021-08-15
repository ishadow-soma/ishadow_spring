package com.soma.ishadow.domains.sentence_en;

import com.soma.ishadow.domains.video.Video;
import com.soma.ishadow.domains.enums.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "sentence_en")
public class SentenceEn implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sentenceId", nullable = false, updatable = false)
    private Long sentenceId;

    @ManyToOne
    @JoinColumn(name = "videoId",nullable = false)
    private Video video;

    @Column(name = "content", columnDefinition = "TEXT")
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

    public SentenceEn(Long sentenceId, Video video, String content, String startTime, String endTime, String speaker, String confidence, Timestamp createdAt, Timestamp updateAt, Status status) {
        this.sentenceId = sentenceId;
        this.video = video;
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

    public Video getVideo() {
        return video;
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
        private Video video;
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

        public Builder video(Video video) {
            this.video = video;
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
            return new SentenceEn(sentenceId, video, content, startTime, endTime, speaker, confidence, createdAt, updateAt, status);
        }
    }
}
