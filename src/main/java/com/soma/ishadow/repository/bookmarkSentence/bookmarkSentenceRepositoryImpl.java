package com.soma.ishadow.repository.bookmarkSentence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.bookmark.BookmarkSentence;
import com.soma.ishadow.domains.bookmark.QBookmarkSentence;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
}
