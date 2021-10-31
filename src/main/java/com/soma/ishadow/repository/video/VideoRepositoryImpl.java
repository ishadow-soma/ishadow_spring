package com.soma.ishadow.repository.video;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.category_video.QCategoryVideo;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.user_video.QUserVideo;
import com.soma.ishadow.domains.video.QVideo;
import com.soma.ishadow.domains.video.Video;
import com.soma.ishadow.providers.VideoProvider;
import com.soma.ishadow.responses.GetVideoRes;
import com.soma.ishadow.responses.YoutubeVideo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class VideoRepositoryImpl extends QuerydslRepositorySupport implements VideoRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;
    private final Logger logger = LoggerFactory.getLogger(VideoRepositoryImpl.class);

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

    @Override
    public Page<Video> findByCategory(Long categoryId, Pageable pageable) {
        QVideo video = QVideo.video;
        QCategoryVideo categoryVideo = QCategoryVideo.categoryVideo;
        QueryResults<Video> result = queryFactory.selectFrom(video)
                .innerJoin(categoryVideo).on(video.videoId.eq(categoryVideo.categoryVideoId.videoId))
                .where(categoryVideo.category.categoryId.eq(categoryId),video.status.eq(Status.YES))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<Video> findByCategoryAndLevel(Long categoryId, float levelStart, float levelEnd, int videoType, Pageable pageable) {
        QVideo video = QVideo.video;
        QCategoryVideo categoryVideo = QCategoryVideo.categoryVideo;
        QueryResults<Video> result = queryFactory.selectFrom(video)
                .innerJoin(categoryVideo).on(video.videoId.eq(categoryVideo.categoryVideoId.videoId))
                .where(categoryVideo.category.categoryId.eq(categoryId)
                        ,video.videoChannel.eq(videoType)
                        ,video.status.eq(Status.YES)
                        ,video.videoLevel.between(levelStart, levelEnd))
                .orderBy(video.videoId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public int findVideoByCount(Long categoryId, float levelStart, float levelEnd, int videoType) {
        QVideo video = QVideo.video;
        QCategoryVideo categoryVideo = QCategoryVideo.categoryVideo;
        return (int) queryFactory.selectFrom(video)
                .innerJoin(categoryVideo).on(video.videoId.eq(categoryVideo.categoryVideoId.videoId))
                .where(categoryVideo.category.categoryId.eq(categoryId)
                        ,video.videoChannel.eq(videoType)
                        ,video.status.eq(Status.YES)
                        ,video.videoLevel.between(levelStart, levelEnd))
                .fetchCount();
    }

    @Override
    public List<GetVideoRes> findByCategoryAndLevelByRecommend(Long categoryId, float lowLevel, float highLevel) {
        QVideo video = QVideo.video;
        QCategoryVideo categoryVideo = QCategoryVideo.categoryVideo;
        return queryFactory.select(
                Projections.constructor(GetVideoRes.class,
                        video.videoId,
                        video.videoName,
                        video.videoURL,
                        video.thumbNailURL,
                        video.videoLevel,
                        categoryVideo.category.categoryId,
                        categoryVideo.category.categoryName))
                .from(video)
                .innerJoin(categoryVideo).on(video.videoId.eq(categoryVideo.categoryVideoId.videoId))
                .where(categoryVideo.category.categoryId.eq(categoryId),video.status.eq(Status.YES), video.videoLevel.between(lowLevel, highLevel))
                .orderBy(video.videoId.desc())
                .fetch();

    }
}
