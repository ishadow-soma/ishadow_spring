package com.soma.ishadow.repository.video;

import com.soma.ishadow.domains.video.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> , VideoRepositoryQuerydsl {

    Video save(Video video);
}
