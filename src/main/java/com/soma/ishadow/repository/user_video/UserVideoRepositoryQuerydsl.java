package com.soma.ishadow.repository.user_video;

import com.soma.ishadow.domains.user_video.UserVideo;
import com.soma.ishadow.domains.video.Video;
import com.soma.ishadow.responses.UploadVideo;
import com.soma.ishadow.responses.YoutubeVideo;

import java.util.List;
import java.util.Optional;

public interface UserVideoRepositoryQuerydsl {

    UserVideo save(UserVideo userVideo);

    Optional<UserVideo> findByUserIdAndVideoId(Long userId, Long videoId);

    List<YoutubeVideo> findYoutubeVideoByUserId(Long userId);

    List<UploadVideo> findUploadVideoByUserId(Long userId);
}
