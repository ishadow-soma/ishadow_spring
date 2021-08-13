package com.soma.ishadow.repository.user_audio;

import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.domains.user_audio.UserAudio;
import com.soma.ishadow.domains.user_audio.UserAudioId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAudioRepository extends JpaRepository<UserAudio, UserAudioId>, UserAudioRepositoryQuerydsl {

    UserAudio save(UserAudio userAudio);
}
