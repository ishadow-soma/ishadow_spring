package com.soma.ishadow.repository.sentense;

import com.soma.ishadow.domains.sentence_en.SentenceEn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentenceEnRepository extends JpaRepository<SentenceEn, Long> , SentenceEnRepositoryQuerydsl{

    SentenceEn save(SentenceEn sentenceEn);
}
