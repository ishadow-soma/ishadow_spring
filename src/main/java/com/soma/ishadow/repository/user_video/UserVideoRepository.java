package com.soma.ishadow.repository.user_video;

import com.soma.ishadow.domains.user_video.UserVideo;
import com.soma.ishadow.domains.user_video.UserVideoId;
import com.soma.ishadow.domains.video.Video;
import com.soma.ishadow.responses.UploadAudio;
import com.soma.ishadow.responses.UploadVideo;
import com.soma.ishadow.responses.YoutubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVideoRepository extends JpaRepository<UserVideo, UserVideoId>, UserVideoRepositoryQuerydsl {

    UserVideo save(UserVideo userVideo);

    Optional<UserVideo> findByUserIdAndVideoId(Long userId, Long videoId);

    List<YoutubeVideo> findYoutubeVideoByUserId(Long userId);

    List<UploadVideo> findUploadVideoByUserId(Long userId);

    List<UploadAudio> findUploadAudioByUserId(Long userId);
}
