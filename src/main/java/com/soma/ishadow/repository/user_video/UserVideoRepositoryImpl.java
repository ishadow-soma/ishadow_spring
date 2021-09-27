package com.soma.ishadow.repository.user_video;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.user_video.QUserVideo;
import com.soma.ishadow.domains.user_video.UserVideo;
import com.soma.ishadow.domains.video.QVideo;
import com.soma.ishadow.responses.UploadVideo;
import com.soma.ishadow.responses.YoutubeVideo;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class UserVideoRepositoryImpl extends QuerydslRepositorySupport implements UserVideoRepositoryQuerydsl {


    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public UserVideoRepositoryImpl(JPAQueryFactory queryFactory, EntityManager em) {
        super(UserVideo.class);
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Override
    public UserVideo save(UserVideo userVideo) {
        em.persist(userVideo);
        return userVideo;
    }

    @Override
    public Optional<UserVideo> findByUserIdAndVideoId(Long userId, Long videoId) {
        QUserVideo userVideo = QUserVideo.userVideo;
        return Optional.ofNullable(queryFactory.selectFrom(userVideo)
                .where(userVideo.user.userId.eq(userId), userVideo.video.videoId.eq(videoId), userVideo.status.eq(Status.YES))
                .fetchOne());
    }

    @Override
    public List<YoutubeVideo> findYoutubeVideoByUserId(Long userId) {
        QUserVideo userVideo = QUserVideo.userVideo;
        QVideo video = QVideo.video;
        return queryFactory.select(Projections.constructor(YoutubeVideo.class, video.videoId, video.videoName, video.thumbNailURL))
                .from(userVideo)
                .where(userVideo.user.userId.eq(userId))
                .join(video)
                .on(userVideo.video.videoId.eq(video.videoId))
                .groupBy(userVideo.video.videoId)
                .having(userVideo.video.videoType.eq("YOUTUBE"))
                .orderBy(userVideo.video.createdAt.desc())
                .fetch();
    }

    @Override
    public List<UploadVideo> findUploadVideoByUserId(Long userId) {
        QUserVideo userVideo = QUserVideo.userVideo;
        QVideo video = QVideo.video;
        return queryFactory.select(Projections.constructor(UploadVideo.class, video.videoId, video.videoName, video.thumbNailURL))
                .from(userVideo)
                .where(userVideo.user.userId.eq(userId))
                .join(video)
                .on(userVideo.video.videoId.eq(video.videoId))
                .groupBy(userVideo.video.videoId)
                .having(video.videoType.eq("UPLOAD"))
                .orderBy(userVideo.video.createdAt.desc())
                .fetch();
    }
}
