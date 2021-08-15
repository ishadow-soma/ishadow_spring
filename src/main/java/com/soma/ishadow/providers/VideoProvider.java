package com.soma.ishadow.providers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.repository.video.VideoRepository;
import com.soma.ishadow.responses.GetSentenceEn;
import com.soma.ishadow.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoProvider {

    private final UserVideoProvider userVideoProvider;
    private final JwtService jwtService;
    private final VideoRepository videoRepository;

    @Autowired
    public VideoProvider(UserVideoProvider userVideoProvider, JwtService jwtService, VideoRepository videoRepository) {
        this.userVideoProvider = userVideoProvider;
        this.jwtService = jwtService;
        this.videoRepository = videoRepository;
    }

    public GetSentenceEn getShadowing(Long audioId, String type) throws BaseException {
        Long userId = jwtService.getUserInfo();

//        //해당 유저가 생성한 영상이 없다면 에러 처리
//       if(!userAudioProvider.findByUserAudio(userId, audioId)) {
//            throw new BaseException(FAILED_TO_GET_USERAUDIO);
//        }
//
//       //audioId를 이용해서 audioInformation 불러오기
//
//
//
       return null;
    }
}
