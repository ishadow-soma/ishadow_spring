package com.soma.ishadow.domains.video;


import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.sentence_en.SentenceEn;
import com.soma.ishadow.domains.user_video.UserVideo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "videoId", nullable = false, updatable = false)
    private Long videoId;

    @Column(name = "videoName", length = 200)
    private String videoName;

    @Column(name = "videoLength", length = 200)
    private Long videoLength;

    @Column(name = "videoType")
    private String videoType;

    @Column(name = "videoURL", columnDefinition = "TEXT")
    private String videoURL;

    @Column(name = "videoChannel")
    private int videoChannel;

    @Column(name = "videoSampling")
    private int videoSampling;

    @Column(name = "videoCapacity")
    private Long videoCapacity;

    @Column(name = "videoSpeakerCount")
    private int videoSpeakerCount;

    @Column(name = "createAt")
    private Timestamp createdAt;

    @Column(name = "updateAt")
    private Timestamp updateAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserVideo> userVideos = new HashSet<>();

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SentenceEn> sentenceEns = new HashSet<>();


    public Video() {

    }

    public Video(Long videoId, String videoName, Long videoLength, String videoType, String videoURL, int videoChannel, int videoSampling, Long videoCapacity, int videoSpeakerCount, Timestamp createdAt, Timestamp updateAt, Status status) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoLength = videoLength;
        this.videoType = videoType;
        this.videoURL = videoURL;
        this.videoChannel = videoChannel;
        this.videoSampling = videoSampling;
        this.videoCapacity = videoCapacity;
        this.videoSpeakerCount = videoSpeakerCount;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.status = status;
    }


    static public class Builder {

        private Long videoId;
        private String videoName;
        private Long videoLength;
        private String videoType;
        private String videoURL;
        private int videoChannel;
        private int videoSampling;
        private Long videoCapacity;
        private int videoSpeakerCount;
        private Timestamp createdAt;
        private Timestamp updateAt;
        private Status status;

        public Builder() {
        }


        public Builder(Video video) {
            this.videoId = video.getVideoId();
            this.videoName = video.getVideoName();
            this.videoLength = video.getVideoLength();
            this.videoType = video.getVideoType();
            this.videoURL = video.getVideoURL();
            this.videoChannel = video.getVideoChannel();
            this.videoSampling = video.getVideoSampling();
            this.videoCapacity = video.getVideoCapacity();
            this.videoSpeakerCount = video.getVideoSpeakerCount();
            this.createdAt = video.getCreatedAt();
            this.updateAt = video.getUpdateAt();
            this.status = video.getStatus();
        }

        public Builder videoName(String videoName) {
            this.videoName = videoName;
            return this;
        }

        public Builder videoLength(Long videoLength) {
            this.videoLength = videoLength;
            return this;
        }

        public Builder videoType(String videoType) {
            this.videoType = videoType;
            return this;
        }

        public Builder videoURL(String videoURL) {
            this.videoURL = videoURL;
            return this;
        }

        public Builder videoChannel(int videoChannel) {
            this.videoChannel = videoChannel;
            return this;
        }

        public Builder videoCapacity(Long videoCapacity) {
            this.videoCapacity = videoCapacity;
            return this;
        }

        public Builder videoSpeakerCount(int videoSpeakerCount) {
            this.videoSpeakerCount = videoSpeakerCount;
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

        public Video build() {
            return new Video(videoId, videoName, videoLength, videoType, videoURL, videoChannel, videoSampling, videoCapacity, videoSpeakerCount, createdAt, updateAt, status);
        }
    }

    public Long getVideoId() {
        return videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public Long getVideoLength() {
        return videoLength;
    }

    public String getVideoType() {
        return videoType;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public int getVideoChannel() {
        return videoChannel;
    }

    public int getVideoSampling() {
        return videoSampling;
    }

    public Long getVideoCapacity() {
        return videoCapacity;
    }

    public int getVideoSpeakerCount() {
        return videoSpeakerCount;
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
