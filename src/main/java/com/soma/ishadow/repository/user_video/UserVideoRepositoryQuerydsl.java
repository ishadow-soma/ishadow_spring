package com.soma.ishadow.repository.user_video;

import com.soma.ishadow.domains.user_video.UserVideo;

import java.util.Optional;

public interface UserVideoRepositoryQuerydsl {

    UserVideo save(UserVideo userVideo);

    Optional<UserVideo> findByUserIdAndVideoId(Long userId, Long videoId);
}
