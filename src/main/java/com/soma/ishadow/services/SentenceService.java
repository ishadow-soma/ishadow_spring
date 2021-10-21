package com.soma.ishadow.services;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.domains.bookmark.BookmarkId;
import com.soma.ishadow.domains.bookmark.BookmarkSentence;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.sentence_en.SentenceEn;
import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.domains.video.Video;
import com.soma.ishadow.providers.SentenceEnProvider;
import com.soma.ishadow.providers.UserProvider;
import com.soma.ishadow.providers.VideoProvider;
import com.soma.ishadow.repository.bookmarkSentence.BookmarkSentenceRepository;
import com.soma.ishadow.requests.PostSentenceReq;
import com.soma.ishadow.responses.PostSentenceRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.soma.ishadow.configures.BaseResponseStatus.FAILED_TO_GET_BOOKMARK;
import static com.soma.ishadow.configures.BaseResponseStatus.FAILED_TO_POST_BOOKMARK;
import static com.soma.ishadow.configures.Constant.bookmarkCount;

@Service
@Transactional
public class SentenceService {

    private final Logger logger = LoggerFactory.getLogger(SentenceService.class);
    private final VideoProvider videoProvider;
    private final UserProvider userProvider;
    private final JwtService jwtService;
    private final SentenceEnProvider sentenceEnProvider;
    private final BookmarkSentenceRepository bookmarkSentenceRepository;

    @Autowired
    public SentenceService(VideoProvider videoProvider, UserProvider userProvider, JwtService jwtService, SentenceEnProvider sentenceEnProvider, BookmarkSentenceRepository bookmarkSentenceRepository) {
        this.videoProvider = videoProvider;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
        this.sentenceEnProvider = sentenceEnProvider;
        this.bookmarkSentenceRepository = bookmarkSentenceRepository;
    }

    public synchronized PostSentenceRes createBookmark(PostSentenceReq postSentenceReq) throws BaseException {

        Long groupId = getGroupId();
        logger.info("groupId: " + groupId);
        Video video = videoProvider.findVideoById(postSentenceReq.getVideoId());
        User user = userProvider.findById(jwtService.getUserInfo());
        List<Long> sentences = postSentenceReq.getSentences();
        String type = postSentenceReq.getSentenceSaveType();

        for(Long sentenceId : sentences) {

            SentenceEn sentence = sentenceEnProvider.findById(sentenceId);
            BookmarkId bookmarkId = createBookmarkId(groupId, video.getVideoId(), user.getUserId(), sentence.getSentenceId());
            BookmarkSentence bookmarkSentence = createBookmarkSentence(bookmarkId, video, user, sentence, type);

            try {
                bookmarkSentenceRepository.save(bookmarkSentence);
            } catch (Exception exception) {
                throw new BaseException(FAILED_TO_POST_BOOKMARK);
            }
        }

        return new PostSentenceRes(groupId);
    }

    private BookmarkId createBookmarkId(Long groupId, Long videoId, Long userId, Long sentenceId) {
        return new BookmarkId(groupId, videoId, userId, sentenceId);
    }


    private BookmarkSentence createBookmarkSentence(BookmarkId bookmarkId, Video video, User user, SentenceEn sentenceEn, String sentenceSaveType) {
        return new BookmarkSentence.Builder()
                .bookmarkId(bookmarkId)
                .video(video)
                .user(user)
                .sentenceEn(sentenceEn)
                .sentenceSaveType(sentenceSaveType)
                .createAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(Status.YES)
                .build();
    }

    @Transactional
    public void deleteBookmark(Long groupId) throws BaseException {

        List<BookmarkSentence> bookmarkSentences = bookmarkSentenceRepository.findByGroupId(groupId);
        if(bookmarkSentences.size() == 0) {
            throw new BaseException(FAILED_TO_GET_BOOKMARK);
        }
        for(BookmarkSentence bookmarkSentence : bookmarkSentences) {
            bookmarkSentence.updateStatus();
        }
    }

    private Long getGroupId() {
        return bookmarkSentenceRepository.findGroupId() + 1;
    }
}
