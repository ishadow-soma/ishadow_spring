package com.soma.ishadow.repository.bookmarkSentence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.bookmark.BookmarkId;
import com.soma.ishadow.domains.bookmark.BookmarkSentence;
import com.soma.ishadow.domains.bookmark.QBookmarkId;
import com.soma.ishadow.domains.bookmark.QBookmarkSentence;
import com.soma.ishadow.domains.enums.Status;
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
    public List<BookmarkSentence> findByVideoAndUser(Long videoId, Long userId) {
        QBookmarkSentence bookmarkSentence = QBookmarkSentence.bookmarkSentence;
        QBookmarkId bookmarkId = bookmarkSentence.bookmarkId;
        return queryFactory.selectFrom(bookmarkSentence)
                .where(bookmarkId.userId.eq(userId), bookmarkId.videoId.eq(videoId), bookmarkSentence.status.eq(Status.YES))
                .orderBy(bookmarkId.groupId.asc())
                .fetch();
    }
}
