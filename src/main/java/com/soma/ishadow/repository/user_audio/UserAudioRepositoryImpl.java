package com.soma.ishadow.repository.user_audio;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.user_audio.UserAudio;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserAudioRepositoryImpl extends QuerydslRepositorySupport implements UserAudioRepositoryQuerydsl {


    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public UserAudioRepositoryImpl(JPAQueryFactory queryFactory, EntityManager em) {
        super(UserAudio.class);
        this.queryFactory = queryFactory;
        this.em = em;
    }

    @Override
    public UserAudio save(UserAudio userAudio) {
        em.persist(userAudio);
        return userAudio;
    }
}
