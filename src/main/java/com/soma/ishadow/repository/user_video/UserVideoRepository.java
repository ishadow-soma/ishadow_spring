package com.soma.ishadow.repository.user_video;

import com.soma.ishadow.domains.user_video.UserVideo;
import com.soma.ishadow.domains.user_video.UserVideoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVideoRepository extends JpaRepository<UserVideo, UserVideoId>, UserVideoRepositoryQuerydsl {

    UserVideo save(UserVideo userVideo);

    Optional<UserVideo> findByUserIdAndVideoId(Long userId, Long videoId);
}
