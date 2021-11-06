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
import static com.soma.ishadow.configures.Constant.PAGE_SIZE;

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

        Video video = findVideoById(videoId);
        logger.info("getShadowing:  " + "videoId: " + videoId + "videotype" + video.getVideoChannel());
        if(video.getVideoChannel() == 0) {
            Long userId = jwtService.getUserInfo();

            logger.info("getShadowing:  " + "userId: " + userId + "videoId: " + videoId);
            //해당 유저가 생성한 영상이 없다면 에러 처리
            if (!userVideoProvider.findByUserVideo(userId, videoId)) {
                throw new BaseException(FAILED_TO_GET_USERVIDEO);
            }
        }

       //videoId를 이용해서 audioInformation 불러오기

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

    public List<GetVideoRes> getVideoByRecommend(Long categoryId, float level) {

        float highLevel = (float) (level + 1.0);
        highLevel = highLevel > 5 ? 5 : highLevel;
        float lowLevel = (float) (level - 1.0);
        lowLevel = lowLevel < 0 ? 0 : lowLevel;
        return findVideoByCategoryAndLevel(categoryId, lowLevel, highLevel);
    }

    private List<GetVideoRes> findVideoByCategoryAndLevel(Long categoryId, float lowLevel, float highLevel) {
        return videoRepository.findByCategoryAndLevelByRecommend(categoryId, lowLevel, highLevel);
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

    public GetVideosRes getVideos(Long categoryId, float levelStart, float levelEnd, int page, int videoType) throws BaseException {

        Sort.Order order = Sort.Order.desc("videoId");
        Sort sort = Sort.by(order);

        int endPage = findVideoByCount(categoryId, levelStart, levelEnd, videoType) / PAGE_SIZE + 1;
        page = Math.min(endPage, page);
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, sort);
        logger.info(pageable.getPageNumber() + " " + pageable.getPageSize());
        Page<Video> videoByPage = findVideoByCategoryAndLevel(categoryId, levelStart, levelEnd, videoType, pageable);
        List<Video> videos = videoByPage.getContent();
        logger.info(String.valueOf(videos.size()));
        logger.info("getVideos paramegers :" + categoryId + " " + levelStart + " " + levelEnd + " " + videoByPage.getTotalPages());

        parameterCheck(videos, levelStart, levelEnd);

        Category category = categoryProvider.findCategory(categoryId);
        List<GetVideoRes> getVideoRes = convertGetVideoRes(videos, category);
        return GetVideosRes.builder()
                .pageStartNumber(1)
                .pageEndNumber(videoByPage.getTotalPages())
                .currentPageNumber(page)
                .videoList(getVideoRes)
                .build();
        //throw new BaseException()
    }

    private int findVideoByCount(Long categoryId, float levelStart, float levelEnd, int videoType) {
        return videoRepository.findVideoByCount(categoryId, levelStart, levelEnd, videoType);
    }

    private List<GetVideoRes> convertGetVideoRes(List<Video> videos, Category category) {
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

    private Page<Video> findVideoByCategoryAndLevel(Long categoryId, float levelStart, float levelEnd, int videoType, Pageable pageable) {
        return videoRepository.findByCategoryAndLevel(categoryId, levelStart, levelEnd, videoType, pageable);
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

    private void parameterCheck(List<Video> videos, float levelStart, float levelEnd) throws BaseException {

        if(levelStart < 0 && levelStart > 5 && levelEnd < 0 && levelEnd > 5) {
            throw new BaseException(INVALID_LEVEL);
        }

        if(videos == null) {
            throw new BaseException(FAILED_TO_GET_VIDEO);
        }
    }

    public List<UploadAudio> findUploadAudioByUserId(Long userId) {
        return userVideoRepository.findUploadAudioByUserId(userId);
    }
}
