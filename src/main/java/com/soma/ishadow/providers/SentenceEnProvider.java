package com.soma.ishadow.providers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.BaseResponseStatus;
import com.soma.ishadow.domains.sentence_en.SentenceEn;
import com.soma.ishadow.repository.sentense.SentenceEnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.soma.ishadow.configures.BaseResponseStatus.FAILED_TO_GET_SENTENCE_EN;

@Service
@Transactional(readOnly = true)
public class SentenceEnProvider {

    private final SentenceEnRepository sentenceEnRepository;

    @Autowired
    public SentenceEnProvider(SentenceEnRepository sentenceEnRepository) {
        this.sentenceEnRepository = sentenceEnRepository;
    }

    public List<SentenceEn> findSentenceEnByVideoId(Long videoId) throws BaseException {
        List<SentenceEn> sentenceEns = sentenceEnRepository.findAllByVideoId(videoId);
        if(sentenceEns.size() == 0) {
            throw new BaseException(FAILED_TO_GET_SENTENCE_EN);
        }
        return sentenceEns;
    }

    public SentenceEn findById(Long sentenceId) throws BaseException {
        return sentenceEnRepository.findById(sentenceId)
                .orElseThrow(() -> new BaseException(FAILED_TO_GET_SENTENCE_EN));
    }
}
