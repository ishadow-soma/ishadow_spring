package com.soma.ishadow.domains.bookmark;

import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.sentence_en.SentenceEn;
import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.domains.user_video.UserVideoId;
import com.soma.ishadow.domains.video.Video;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "bookmark_sentence")
public class BookmarkSentence implements Serializable {

    @EmbeddedId
    private BookmarkId bookmarkId;

    @MapsId(value = "userId")
    @ManyToOne
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    @MapsId(value = "videoId")
    @ManyToOne
    @JoinColumn(name = "videoId",nullable = false)
    private Video video;

    @MapsId(value = "sentenceId")
    @ManyToOne
    @JoinColumn(name = "sentenceId",nullable = false)
    private SentenceEn sentenceEn;

    //REPEAT 반복 OR FAVORITE 즐겨찾기
    @Column(name = "sentenceSaveType")
    private String sentenceSaveType;

    @Column(name = "createAt")
    private Timestamp createAt;

    @Column(name = "updateAt")
    private Timestamp updateAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public BookmarkSentence(BookmarkId bookmarkId, User user, Video video, String sentenceSaveType, SentenceEn sentenceEn, Timestamp createAt, Timestamp updateAt, Status status) {
        this.bookmarkId = bookmarkId;
        this.user = user;
        this.video = video;
        this.sentenceSaveType = sentenceSaveType;
        this.sentenceEn = sentenceEn;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.status = status;
    }

    public BookmarkSentence() {

    }

    public String getSentenceSaveType() {
        return sentenceSaveType;
    }

    public BookmarkId getBookmarkId() {
        return bookmarkId;
    }

    public void createBookmarkId(Long groupId, Long videoId, Long userId, Long sentenceId) {
        this.bookmarkId  = new BookmarkId(groupId, videoId, userId, sentenceId);
    }

    public User getUser() {
        return user;
    }

    public Video getVideo() {
        return video;
    }

    public SentenceEn getSentenceEn() {
        return sentenceEn;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public Status getStatus() {
        return status;
    }

    static public class Builder {
        private BookmarkId bookmarkId;
        private User user;
        private Video video;
        private SentenceEn sentenceEn;
        private String sentenceSaveType;
        private Timestamp createAt;
        private Timestamp updateAt;
        @Enumerated(EnumType.STRING)
        private Status status;

        public Builder bookmarkId(BookmarkId bookmarkId) {
            this.bookmarkId = bookmarkId;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder video(Video video) {
            this.video = video;
            return this;
        }

        public Builder sentenceSaveType(String sentenceSaveType) {
            this.sentenceSaveType = sentenceSaveType;
            return this;
        }

        public Builder sentenceEn(SentenceEn sentenceEn) {
            this.sentenceEn = sentenceEn;
            return this;
        }

        public Builder createAt(Timestamp createAt) {
            this.createAt = createAt;
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

        public BookmarkSentence build() {
            return new BookmarkSentence(bookmarkId, user, video, sentenceSaveType,sentenceEn, createAt, updateAt, status);
        }


    }
}
