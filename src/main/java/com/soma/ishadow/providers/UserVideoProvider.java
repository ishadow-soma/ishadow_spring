package com.soma.ishadow.providers;

import com.soma.ishadow.repository.user_video.UserVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserVideoProvider {

    private final UserVideoRepository userVideoRepository;

    @Autowired
    public UserVideoProvider(UserVideoRepository userVideoRepository) {
        this.userVideoRepository = userVideoRepository;
    }

    public boolean findByUserVideo(Long userId, Long videoId) {
        if( userVideoRepository.findByUserIdAndVideoId(userId, videoId).isPresent()) {
            return true;
        }
        return false;
    }
}
