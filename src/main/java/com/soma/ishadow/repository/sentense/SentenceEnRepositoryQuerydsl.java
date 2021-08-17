package com.soma.ishadow.repository.sentense;

import com.soma.ishadow.domains.sentence_en.SentenceEn;

import java.util.List;
import java.util.Optional;

public interface SentenceEnRepositoryQuerydsl {

    SentenceEn save(SentenceEn sentenceEn);

    List<SentenceEn> findAllByVideoId(Long videoId);

    Optional<SentenceEn> findById(Long sentenceId);
}
