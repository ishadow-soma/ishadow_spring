package com.soma.ishadow.providers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.domains.bookmark.BookmarkId;
import com.soma.ishadow.domains.bookmark.BookmarkSentence;
import com.soma.ishadow.repository.bookmarkSentence.BookmarkSentenceRepository;
import com.soma.ishadow.responses.GetBookmarkRes;
import com.soma.ishadow.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.soma.ishadow.configures.BaseResponseStatus.FAILED_TO_GET_BOOKMARK;

@Service
public class SentenceProvider {

    private final BookmarkSentenceRepository bookmarkSentenceRepository;
    private final JwtService jwtService;

    @Autowired
    public SentenceProvider(BookmarkSentenceRepository bookmarkSentenceRepository, JwtService jwtService) {
        this.bookmarkSentenceRepository = bookmarkSentenceRepository;
        this.jwtService = jwtService;
    }

    public List<GetBookmarkRes> getSentences(Long videoId, String type) throws BaseException {

        Long userId = jwtService.getUserInfo();
        List<BookmarkSentence> bookmarkSentences = new ArrayList<>();
        if(type.equals("REPEAT")) {
            bookmarkSentences.addAll(getBookmarkSentences(videoId, userId));
        }
        if(type.equals("FAVORITE")) {
            bookmarkSentences.addAll(getFavoriteSentences(videoId, userId));
        }
        List<GetBookmarkRes> bookmarks = new ArrayList<>();
        if(bookmarkSentences.size() == 0) {
            return bookmarks;
        }
        for(BookmarkSentence bookmarkSentence : bookmarkSentences) { //데이터베이스에 있는 구간 반복 문장들
            BookmarkId bookmarkId = bookmarkSentence.getBookmarkId();
            Long groupId = bookmarkId.getGroupId();
            Long sentenceId = bookmarkId.getSentenceId();
            boolean check = false;
            for( GetBookmarkRes getBookmarkRes : bookmarks) {
                if( getBookmarkRes.getGroupId().equals(groupId) ) {
                    getBookmarkRes.addSentences(sentenceId);
                    check = true;
                }
            }
            if(!check) {
                List<Long> list = new ArrayList<>();
                list.add(sentenceId);
                bookmarks.add(new GetBookmarkRes(groupId,list));
            }
        }

        return bookmarks;

//        Long preGroupId = -1L;
//        int indexCount = 0;
//        for(BookmarkSentence bookmarkSentence : bookmarkSentences) {
//            BookmarkId bookmarkId = bookmarkSentence.getBookmarkId();
//            Long groupId = bookmarkId.getGroupId();
//            Long sentenceId = bookmarkId.getSentenceId();
//            if(preGroupId.equals(groupId)) {
//                bookmarks.get(indexCount).addSentences(sentenceId);
//                continue;
//            }
//            preGroupId = groupId;
//            indexCount++;
//            List<Long> list = new ArrayList<>();
//            list.add(sentenceId);
//            bookmarks.add(new GetBookmarkRes(groupId,list));
//        }

    }

    private List<BookmarkSentence> getFavoriteSentences(Long videoId, Long userId) {
        return bookmarkSentenceRepository.findByVideoAndUserByFavorite(videoId, userId);
    }


    public List<BookmarkSentence> getBookmarkSentences(Long videoId, Long userId) throws BaseException {
        return bookmarkSentenceRepository.findByVideoAndUserByBookmark(videoId, userId);
    }

}
