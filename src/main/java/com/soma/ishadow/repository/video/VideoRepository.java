package com.soma.ishadow.repository.video;

import com.soma.ishadow.domains.video.Video;
import com.soma.ishadow.responses.YoutubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> , VideoRepositoryQuerydsl {

    Video save(Video video);

    Optional<Video> findById(Long videoId);

    List<Video> findYoutubeVideoByUserId(Long userId);

    List<Video> findUploadVideoByUserId(Long userId);
}
