package com.soma.ishadow.repository.video;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.user_video.QUserVideo;
import com.soma.ishadow.domains.user_video.UserVideo;
import com.soma.ishadow.domains.video.QVideo;
import com.soma.ishadow.domains.video.Video;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class VideoRepositoryImpl extends QuerydslRepositorySupport implements VideoRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public VideoRepositoryImpl(JPAQueryFactory queryFactory, EntityManager em) {
        super(Video.class);
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Override
    public Video save(Video video) {
        em.persist(video);
        return video;
    }

    @Override
    public Optional<Video> findById(Long videoId) {
        QVideo video = QVideo.video;
        return Optional.ofNullable(queryFactory.selectFrom(video)
                .where(video.videoId.eq(videoId), video.status.eq(Status.YES))
                .fetchOne());
    }

    @Override
    public List<Video> findYoutubeVideoByUserId(Long userId) {
        QVideo video = QVideo.video;
        QUserVideo userVideo = QUserVideo.userVideo;
        return queryFactory.selectFrom(video)
                .innerJoin(userVideo).on(userVideo.video.videoId.eq(video.videoId))
                .having(userVideo.user.userId.eq(userId),video.videoType.eq("YOUTUBE"), video.status.eq(Status.YES))
                .fetch();
    }

    @Override
    public List<Video> findUploadVideoByUserId(Long userId) {
        QVideo video = QVideo.video;
        QUserVideo userVideo = QUserVideo.userVideo;
        return queryFactory.selectFrom(video)
                .innerJoin(userVideo).on(userVideo.video.videoId.eq(video.videoId))
                .having(userVideo.user.userId.eq(userId),video.videoType.eq("UPLOAD"), video.status.eq(Status.YES))
                .fetch();
    }
}
