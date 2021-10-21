package com.soma.ishadow.repository.bookmarkSentence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.bookmark.BookmarkId;
import com.soma.ishadow.domains.bookmark.BookmarkSentence;
import com.soma.ishadow.domains.bookmark.QBookmarkId;
import com.soma.ishadow.domains.bookmark.QBookmarkSentence;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.sentence_en.QSentenceEn;
import com.soma.ishadow.responses.GetBookmarkRes;
import com.soma.ishadow.responses.YoutubeVideo;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class bookmarkSentenceRepositoryImpl extends QuerydslRepositorySupport implements BookmarkSentenceRepositoryQuerydsl {


    private final JPAQueryFactory queryFactory;
    private final EntityManager em;


    public bookmarkSentenceRepositoryImpl(JPAQueryFactory queryFactory, EntityManager em) {
        super(BookmarkSentence.class);
        this.queryFactory = queryFactory;
        this.em = em;
    }


    @Override
    public BookmarkSentence save(BookmarkSentence bookmarkSentence) {
        em.persist(bookmarkSentence);
        return bookmarkSentence;
    }

    @Override
    public List<BookmarkSentence> findByVideoAndUserByBookmark(Long videoId, Long userId) {
        QBookmarkSentence bookmarkSentence = QBookmarkSentence.bookmarkSentence;
        QBookmarkId bookmarkId = bookmarkSentence.bookmarkId;
        return queryFactory.selectFrom(bookmarkSentence)
                .where(bookmarkId.userId.eq(userId), bookmarkId.videoId.eq(videoId),  bookmarkSentence.sentenceSaveType.eq("REPEAT") ,bookmarkSentence.status.eq(Status.YES))
                .orderBy(bookmarkId.groupId.asc())
                .fetch();
    }

    @Override
    public Long findGroupId() {
        QBookmarkSentence bookmarkSentence = QBookmarkSentence.bookmarkSentence;
        return queryFactory.select(bookmarkSentence.bookmarkId.groupId)
                .from(bookmarkSentence)
                .orderBy(bookmarkSentence.bookmarkId.groupId.desc())
                .limit(1L)
                .fetchOne();
    }


    @Override
    public List<GetBookmarkRes> findByVideoAndUserByFavorite(Long videoId, Long userId) {
        QBookmarkSentence bookmarkSentence = QBookmarkSentence.bookmarkSentence;
        QSentenceEn sentenceEn = QSentenceEn.sentenceEn;
        QBookmarkId bookmarkId = bookmarkSentence.bookmarkId;
        return queryFactory.select(Projections.constructor(GetBookmarkRes.class, bookmarkSentence.bookmarkId.groupId,bookmarkSentence.bookmarkId.sentenceId,sentenceEn.content,sentenceEn.startTime,sentenceEn.endTime))
                .from(bookmarkSentence)
                .innerJoin(sentenceEn).on(bookmarkId.sentenceId.eq(sentenceEn.sentenceId))
                .where(bookmarkId.userId.eq(userId), bookmarkId.videoId.eq(videoId), bookmarkSentence.sentenceSaveType.eq("FAVORITE"), bookmarkSentence.status.eq(Status.YES))
                .orderBy(bookmarkId.groupId.asc())
                .fetch();
    }
}
