package com.soma.ishadow.repository.sentense;

import com.soma.ishadow.domains.sentence_en.SentenceEn;

import java.util.List;

public interface SentenceEnRepositoryQuerydsl {

    SentenceEn save(SentenceEn sentenceEn);

    List<SentenceEn> findAllByVideoId(Long videoId);
}
