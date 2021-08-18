package com.soma.ishadow.repository.bookmarkSentence;

import com.soma.ishadow.domains.bookmark.BookmarkId;
import com.soma.ishadow.domains.bookmark.BookmarkSentence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkSentenceRepository extends JpaRepository<BookmarkSentence, BookmarkId>, BookmarkSentenceRepositoryQuerydsl{

    BookmarkSentence save(BookmarkSentence bookmarkSentence);

    List<BookmarkSentence> findByVideoAndUser(Long videoId, Long userId);
}
