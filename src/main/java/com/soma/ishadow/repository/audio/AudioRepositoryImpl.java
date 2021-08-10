package com.soma.ishadow.repository.audio;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.audio.Audio;
import com.soma.ishadow.domains.user.User;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class AudioRepositoryImpl extends QuerydslRepositorySupport implements AudioRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public AudioRepositoryImpl(JPAQueryFactory queryFactory, EntityManager em) {
        super(Audio.class);
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Override
    public Audio save(Audio audio) {
        em.persist(audio);
        return audio;
    }
}
