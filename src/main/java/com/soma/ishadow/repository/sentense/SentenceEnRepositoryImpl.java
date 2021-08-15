package com.soma.ishadow.repository.sentense;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.sentence_en.QSentenceEn;
import com.soma.ishadow.domains.sentence_en.SentenceEn;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class SentenceEnRepositoryImpl extends QuerydslRepositorySupport implements SentenceEnRepositoryQuerydsl {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public SentenceEnRepositoryImpl(EntityManager em, JPAQueryFactory queryFactory) {
        super(SentenceEn.class);
        this.em = em;
        this.queryFactory = queryFactory;
    }

    @Override
    public SentenceEn save(SentenceEn sentenceEn) {
        em.persist(sentenceEn);
        return sentenceEn;
    }

    @Override
    public List<SentenceEn> findAllByVideoId(Long videoId) {
        QSentenceEn sentenceEn = QSentenceEn.sentenceEn;
        return queryFactory.selectFrom(sentenceEn)
                .where(sentenceEn.video.videoId.eq(videoId))
                .fetch();
    }
}
