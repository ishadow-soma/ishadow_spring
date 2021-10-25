package com.soma.ishadow.providers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.domains.category.Category;
import com.soma.ishadow.domains.video.Video;
import com.soma.ishadow.repository.user_video.UserVideoRepository;
import com.soma.ishadow.repository.video.VideoRepository;
import com.soma.ishadow.responses.*;
import com.soma.ishadow.services.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.soma.ishadow.configures.BaseResponseStatus.*;

@Service
@Transactional(readOnly = true)
public class VideoProvider {

    private final SentenceEnProvider sentenceEnProvider;
    private final UserVideoProvider userVideoProvider;
    private final JwtService jwtService;
    private final VideoRepository videoRepository;
    private final UserVideoRepository userVideoRepository;
    private final CategoryProvider categoryProvider;
    private final Logger logger = LoggerFactory.getLogger(VideoProvider.class);

    @Autowired
    public VideoProvider(SentenceEnProvider sentenceEnProvider, UserVideoProvider userVideoProvider, JwtService jwtService, VideoRepository videoRepository, UserVideoRepository userVideoRepository, CategoryProvider categoryProvider) {
        this.sentenceEnProvider = sentenceEnProvider;
        this.userVideoProvider = userVideoProvider;
        this.jwtService = jwtService;
        this.videoRepository = videoRepository;
        this.userVideoRepository = userVideoRepository;
        this.categoryProvider = categoryProvider;
    }


    public GetShadowingRes getShadowing(Long videoId) throws BaseException {
        Long userId = jwtService.getUserInfo();

        logger.info("getShadowing:  " + "userId: " + userId + "videoId: " + videoId);
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

        boolean evaluation = video.getVideoType().equals("upload");

        if(video.getVideoType().equals("youtube")) {
            evaluation = video.getVideoEvaluation();
        }
        return GetShadowingRes.builder()
                .videoId(video.getVideoId())
                .videoName(video.getVideoName())
                .videoURL(video.getVideoURL())
                .videoEvaluation(evaluation)
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
                        .build())
                .collect(Collectors.toList());
    }

    public GetVideosRes getVideos(Long categoryId, float levelStart, float levelEnd, int page) throws BaseException {

        Sort.Order order = Sort.Order.desc("videoId");
        Sort sort = Sort.by(order);

        Pageable pageable = PageRequest.of(page - 1, 12, sort);
        logger.info(pageable.getPageNumber() + " " + pageable.getPageSize());
        Page<Video> videos = findVideoByCategoryAndLevel(categoryId, levelStart, levelEnd, pageable);

        logger.info("getVideos paramegers :" + categoryId + " " + levelStart + " " + levelEnd);

        parameterCheck(videos, levelStart, levelEnd);

        Category category = categoryProvider.findCategory(categoryId);
        List<GetVideoRes> getVideoRes = convertGetVideoRes(videos, category);
        return GetVideosRes.builder()
                .pageStartNumber(1)
                .pageEndNumber(videos.getTotalPages())
                .currentPageNumber(page)
                .videoList(getVideoRes)
                .build();
        //throw new BaseException()
    }

    private List<GetVideoRes> convertGetVideoRes(Page<Video> videos, Category category) {
        return videos.stream().map(video -> GetVideoRes.builder()
                .videoId(video.getVideoId())
                .videoName(video.getVideoName())
                .videoLevel(video.getVideoLevel())
                .videoURL(video.getVideoURL())
                .thumbNailURL(video.getThumbNailURL())
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build())
                .collect(Collectors.toList());
    }

    private Page<Video> findVideoByCategoryAndLevel(Long categoryId, float levelStart, float levelEnd, Pageable pageable) {
        return videoRepository.findByCategoryAndLevel(categoryId, levelStart, levelEnd, pageable);
    }

    private Page<Video> findVideoByCategory(Long categoryId, Pageable pageable) {

        return videoRepository.findByCategory(categoryId, pageable);
    }

    public Video findVideoById(Long videoId) throws BaseException {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new BaseException(FAILED_TO_GET_VIDEO));
    }


    public List<YoutubeVideo> findYoutubeVideoByUserId(Long userId) {
          return userVideoRepository.findYoutubeVideoByUserId(userId);

    }

    public List<UploadVideo> findUploadVideoByUserId(Long userId) {
        return userVideoRepository.findUploadVideoByUserId(userId);
    }

    private void parameterCheck(Page<Video> videos, float levelStart, float levelEnd) throws BaseException {

        if(levelStart < 0 && levelStart > 5 && levelEnd < 0 && levelEnd > 5) {
            throw new BaseException(INVALID_LEVEL);
        }

        if(videos == null) {
            throw new BaseException(FAILED_TO_GET_VIDEO);
        }
    }

}
