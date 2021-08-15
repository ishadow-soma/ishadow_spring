package com.soma.ishadow.repository.video;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.video.Video;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
}
