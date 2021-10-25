package com.soma.ishadow.repository.video;

import com.soma.ishadow.domains.video.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> , VideoRepositoryQuerydsl {

    Video save(Video video);

    Optional<Video> findById(Long videoId);

    List<Video> findYoutubeVideoByUserId(Long userId);

    List<Video> findUploadVideoByUserId(Long userId);

    Page<Video> findByCategory(Long categoryId, Pageable pageable);

    Page<Video> findByCategoryAndLevel(Long categoryId, float levelStart, float levelEnd, Pageable pageable);

    int findVideoByCount(Long categoryId, float levelStart, float levelEnd);
}
