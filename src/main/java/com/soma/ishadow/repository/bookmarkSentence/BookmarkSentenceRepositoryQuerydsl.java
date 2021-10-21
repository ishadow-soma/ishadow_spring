package com.soma.ishadow.repository.bookmarkSentence;

import com.soma.ishadow.domains.bookmark.BookmarkId;
import com.soma.ishadow.domains.bookmark.BookmarkSentence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkSentenceRepositoryQuerydsl {

    BookmarkSentence save(BookmarkSentence bookmarkSentence);

    List<BookmarkSentence> findByVideoAndUserByFavorite(Long videoId, Long userId);

    List<BookmarkSentence> findByVideoAndUserByBookmark(Long videoId, Long userId);
}
