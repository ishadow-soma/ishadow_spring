package com.soma.ishadow.repository.sentense;

import com.soma.ishadow.domains.sentence_en.SentenceEn;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class SentenceEnRepositoryImpl extends QuerydslRepositorySupport implements SentenceEnRepositoryQuerydsl {

    private final EntityManager em;

    public SentenceEnRepositoryImpl(EntityManager em) {
        super(SentenceEn.class);
        this.em = em;
    }

    @Override
    public SentenceEn save(SentenceEn sentenceEn) {
        em.persist(sentenceEn);
        return sentenceEn;
    }
}
