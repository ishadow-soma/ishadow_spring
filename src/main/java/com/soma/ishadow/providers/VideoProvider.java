package com.soma.ishadow.providers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.domains.video.Video;
import com.soma.ishadow.repository.user_video.UserVideoRepository;
import com.soma.ishadow.repository.video.VideoRepository;
import com.soma.ishadow.responses.GetSentenceEnRes;
import com.soma.ishadow.responses.GetShadowingRes;
import com.soma.ishadow.responses.UploadVideo;
import com.soma.ishadow.responses.YoutubeVideo;
import com.soma.ishadow.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.soma.ishadow.configures.BaseResponseStatus.FAILED_TO_GET_USERVIDEO;
import static com.soma.ishadow.configures.BaseResponseStatus.FAILED_TO_GET_VIDEO;

@Service
@Transactional(readOnly = true)
public class VideoProvider {

    private final SentenceEnProvider sentenceEnProvider;
    private final UserVideoProvider userVideoProvider;
    private final JwtService jwtService;
    private final VideoRepository videoRepository;
    private final UserVideoRepository userVideoRepository;

    @Autowired
    public VideoProvider(SentenceEnProvider sentenceEnProvider, UserVideoProvider userVideoProvider, JwtService jwtService, VideoRepository videoRepository, UserVideoRepository userVideoRepository) {
        this.sentenceEnProvider = sentenceEnProvider;
        this.userVideoProvider = userVideoProvider;
        this.jwtService = jwtService;
        this.videoRepository = videoRepository;
        this.userVideoRepository = userVideoRepository;
    }

    @Transactional(readOnly = true)
    public GetShadowingRes getShadowing(Long videoId) throws BaseException {
        Long userId = jwtService.getUserInfo();

        //해당 유저가 생성한 영상이 없다면 에러 처리
       if(!userVideoProvider.findByUserVideo(userId, videoId)) {
            throw new BaseException(FAILED_TO_GET_USERVIDEO);
        }

       //videoId를 이용해서 audioInformation 불러오기
        Video video = findVideoById(videoId);
        List<GetSentenceEnRes> sentenceEns = findSentenceEnByVideoId(video.getVideoId());

        GetShadowingRes getShadowingRes = createShadowing(video);
        getShadowingRes.addSentence(sentenceEns);
        return getShadowingRes;


    }

    private GetShadowingRes createShadowing(Video video) {
        return GetShadowingRes.builder()
                .videoId(video.getVideoId())
                .videoName(video.getVideoName())
                .videoURL(video.getVideoURL())
                .thumbNailURL(video.getThumbNailURL())
                .build();
    }

    private List<GetSentenceEnRes> findSentenceEnByVideoId(Long videoId) throws BaseException {
        return sentenceEnProvider.findSentenceEnByVideoId(videoId).stream()
                .map(sentenceEn -> GetSentenceEnRes.builder()
                        .sentenceId(sentenceEn.getSentenceId())
                        .content(sentenceEn.getContent())
                        .startTime(sentenceEn.getStartTime())
                        .endTime(sentenceEn.getEndTime())
                        .speaker(sentenceEn.getSpeaker())
                        .build())
                .collect(Collectors.toList());
    }

    public Video findVideoById(Long videoId) throws BaseException {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new BaseException(FAILED_TO_GET_VIDEO));
    }


    public List<YoutubeVideo> findYoutubeVideoByUserId(Long userId) {
          return userVideoRepository.findYoutubeVideoByUserId(userId);

    }

    public List<UploadVideo> findUploadVideoByUserId(Long userId) {
        return videoRepository.findUploadVideoByUserId(userId).stream()
                .map(video -> UploadVideo.builder()
                        .videoId(video.getVideoId())
                        .title(video.getVideoName())
                        .thumbNailURL(video.getThumbNailURL())
                        .build())
                .collect(Collectors.toList());
    }
}
