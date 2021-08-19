package com.soma.ishadow.repository.video;

import com.soma.ishadow.domains.video.Video;

import java.util.List;
import java.util.Optional;

public interface VideoRepositoryQuerydsl {

    Video save(Video video);

    Optional<Video> findById(Long videoId);

    List<Video> findYoutubeVideoByUserId(Long userId);

    List<Video> findUploadVideoByUserId(Long userId);
}
