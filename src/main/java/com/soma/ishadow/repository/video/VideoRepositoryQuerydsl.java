package com.soma.ishadow.repository.video;

import com.soma.ishadow.domains.video.Video;

import java.util.Optional;

public interface VideoRepositoryQuerydsl {

    Video save(Video video);

    Optional<Video> findById(Long videoId);
}
