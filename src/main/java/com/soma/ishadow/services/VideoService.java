package com.soma.ishadow.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.domains.category.Category;
import com.soma.ishadow.domains.category_video.CategoryVideo;
import com.soma.ishadow.domains.category_video.CategoryVideoId;
import com.soma.ishadow.domains.review.Review;
import com.soma.ishadow.domains.user_review.UserReview;
import com.soma.ishadow.domains.user_review.UserReviewId;
import com.soma.ishadow.domains.video.Video;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.sentence_en.SentenceEn;
import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.domains.user_video.UserVideo;
import com.soma.ishadow.domains.user_video.UserVideoId;
import com.soma.ishadow.providers.CategoryProvider;
import com.soma.ishadow.providers.ReviewProvider;
import com.soma.ishadow.providers.UserVideoProvider;
import com.soma.ishadow.providers.VideoProvider;
import com.soma.ishadow.repository.category_video.CategoryVideoRepository;
import com.soma.ishadow.repository.review.ReviewRepository;
import com.soma.ishadow.repository.user_review.UserReviewRepository;
import com.soma.ishadow.repository.video.VideoRepository;
import com.soma.ishadow.repository.sentense.SentenceEnRepository;
import com.soma.ishadow.repository.user_video.UserVideoRepository;
import com.soma.ishadow.requests.PostVideoConvertorReq;
import com.soma.ishadow.requests.PostVideoLevelReq;
import com.soma.ishadow.requests.PostVideoReq;
import com.soma.ishadow.responses.PostVideoLevelRes;
import com.soma.ishadow.responses.PostVideoRes;
import com.soma.ishadow.utils.S3Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static com.soma.ishadow.configures.BaseResponseStatus.*;
import static com.soma.ishadow.configures.Constant.baseUrl;

@Service
@Transactional
public class VideoService {

    private final S3Util s3Util;
    private final VideoRepository videoRepository;
    private final UserVideoRepository userVideoRepository;
    private final UserVideoProvider userVideoProvider;
    private final SentenceEnRepository sentenceEnRepository;
    private final ReviewRepository reviewRepository;
    private final UserReviewRepository userReviewRepository;
    private final CategoryProvider categoryProvider;
    private final VideoProvider videoProvider;
    private final ReviewProvider reviewProvider;
    private final CategoryVideoRepository categoryVideoRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(VideoService.class);

    @Qualifier("URLRepository")
    private final HashMap<String, Long> URLRepository;

    @Autowired
    public VideoService(S3Util s3Util, VideoRepository videoRepository, UserVideoRepository userVideoRepository, UserVideoProvider userVideoProvider, SentenceEnRepository sentenceEnRepository, ReviewRepository reviewRepository, UserReviewRepository userReviewRepository, CategoryProvider categoryProvider, VideoProvider videoProvider, ReviewProvider reviewProvider, CategoryVideoRepository categoryVideoRepository, UserService userService, JwtService jwtService, HashMap<String, Long> urlRepository) {
        this.s3Util = s3Util;
        this.videoRepository = videoRepository;
        this.userVideoRepository = userVideoRepository;
        this.userVideoProvider = userVideoProvider;
        this.sentenceEnRepository = sentenceEnRepository;
        this.reviewRepository = reviewRepository;
        this.userReviewRepository = userReviewRepository;
        this.categoryProvider = categoryProvider;
        this.videoProvider = videoProvider;
        this.reviewProvider = reviewProvider;
        this.categoryVideoRepository = categoryVideoRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.URLRepository = urlRepository;
    }

    /**
     * 영상 업로드
     * @param postVideoReq
     * @param video
     * @param userId
     * @return
     * @throws BaseException
     * @throws IOException
     */
    //TODO manager 테이블 만들어서 오류가 나면 해당 오류 부터 확인 할 수 있게 한다.
    public PostVideoRes upload(PostVideoReq postVideoReq, MultipartFile video, Long userId) throws BaseException, IOException {

        String type = postVideoReq.getType();
        List<Long> categoryId = postVideoReq.getCategoryId();
        if(!categoryId.contains(20L)) categoryId.add(20L);
        logger.info(String.valueOf(categoryId));
        String url = postVideoReq.getYoutubeURL();

        //S3에 저장하고 URL 반환하기
        if(type.equals("LOCAL")) {
            if(video.isEmpty()) {
                logger.info("영상이 비어있습니다.");
                throw new BaseException(EMPTY_VIDEO);
            }
            url = s3Util.upload(video, userId);
        }

        if(type.equals("YOUTUBE")) {

            if(URLRepository.get(url) != null) {
                logger.info("해당 "+ url + " 영상이 존재합니다.");
                Video exitedVideo = videoRepository.findById(URLRepository.get(url)).orElse(null);
                if(exitedVideo == null) {
                    logger.info("URLRepository에는 값이 존재하는데 DB에는 video가 없음");
                    URLRepository.put(url,null);
                    throw new BaseException(FAILED_TO_GET_VIDEO_YOUTUBE);
                }

                if(!userVideoProvider.findByUserVideo(userId, exitedVideo.getVideoId())) {
                    User user = userService.findById(userId);
                    UserVideo userVideo = saveUserVideo(user, exitedVideo);
                    logger.info("영상 유저 조인 테이블 저장 성공: " + userVideo.getUserVideoId().toString());
                }

                return new PostVideoRes(exitedVideo.getVideoId(), exitedVideo.getVideoName(), url);
            }

            Video newVideo = createVideo(postVideoReq);

            //video DB에 저장
            Video createdVideo = saveVideo(newVideo);
            logger.info("영상 저장 성공: " + createdVideo.getVideoId());

            //categoryId 저장하기
            saveCategoryVideo(categoryId, createdVideo);
            logger.info("카테고리 영상 조인 테이블 저장 성공");

            WebClient webClient = createWebClient();

            String videoInfo = getInfo(webClient, url);
            logger.info("영상 변환 성공: " + url);

            String title = audioTranslateToText(createdVideo, videoInfo);
            createdVideo.setVideoName(title);
            Video updatedVideo = saveVideo(createdVideo);
            logger.info("영상 제목 저장 성공: " + updatedVideo.getVideoId());

            //videoId를 이용해서 user_video에 저장하기
            User user = userService.findById(userId);
            UserVideo userVideo = saveUserVideo(user, updatedVideo);
            logger.info("영상 유저 조인 테이블 저장 성공: " + userVideo.getUserVideoId().toString());

            URLRepository.put(url, updatedVideo.getVideoId());
            return new PostVideoRes(updatedVideo.getVideoId(), title, url);
        }

        throw new BaseException(INVALID_AUDIO_TYPE);
    }


    /**
     * 영상 난이도 추가
     * @param videoId
     * @param postVideoLevelReq
     * @return
     */
    public PostVideoLevelRes createVideo(Long videoId, PostVideoLevelReq postVideoLevelReq) throws BaseException {

        Long userId = jwtService.getUserInfo();
        User user = userService.findById(userId);
        Video video = videoProvider.findVideoById(videoId);

        reviewIsExisted(user, video);
        Review review = createReview(user, video, postVideoLevelReq);
        Review newReview = saveReview(review);
        UserReview userReview = saveUserReview(user, newReview);
        logger.info("리뷰 유저 조인 테이블 저장 성공: " + userReview.getUserReviewId().toString());

        video.setVideoEvaluation(true);

        updateReviewVideo(review, video);
        Video newVideo = saveVideo(video);

        logger.info("영상 저장 성공: " + newVideo.getVideoId());
        return PostVideoLevelRes.builder()
                .videoId(newVideo.getVideoId())
                .videoEvaluation(newVideo.getVideoEvaluation())
                .reviewId(newReview.getReviewId())
                .build();
    }

    private void updateReviewVideo(Review review, Video video) {

        float level = review.getLevel();
        int videoCount = video.getVideoLevelCount();
        float videoLevel = video.getVideoLevel();
        float result = (level + videoLevel) / (videoCount + 1);
        video.updateVideoLevel(result,videoCount + 1);

    }

    private void reviewIsExisted(User user, Video video) throws BaseException {

        Boolean checkResult = reviewProvider.reviewCheck(user.getUserId(), video.getVideoId());
        if(checkResult) {
            throw new BaseException(ALREADY_EXISTED_REVIEW);
        }

    }


    private void saveCategoryVideo(List<Long> categoryId, Video video) throws BaseException {

        for(Long id : categoryId) {
            Category category = categoryProvider.findCategory(id);
            CategoryVideo categoryVideo = createCategoryAudio(category, video);
            try {
                categoryVideoRepository.save(categoryVideo);
            } catch (Exception exception) {
                throw new BaseException(FAILED_TO_POST_USERAUDIO);
            }
        }
    }

    private CategoryVideo createCategoryAudio(Category category, Video video) {
        return CategoryVideo.builder()
                .categoryVideoId(new CategoryVideoId(category.getCategoryId(), video.getVideoId()))
                .category(category)
                .video(video)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(Status.YES)
                .build();
    }

    private WebClient createWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    private UserVideo saveUserVideo(User user, Video video) throws BaseException {

        UserVideo userVideo = createUserAudio(user, video);
        UserVideo newUserVideo;
        try {
            newUserVideo = userVideoRepository.save(userVideo);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_USERAUDIO);
        }
        return newUserVideo;
    }

    private UserReview saveUserReview(User user, Review newReview) throws BaseException {

        UserReview userReview = createUserReview(user, newReview);
        UserReview newUserReview;
        try {
            newUserReview = userReviewRepository.save(userReview);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_USER_REVIEW);
        }

        return newUserReview;
    }

    private Review saveReview(Review review) throws BaseException {

        Review savedReview;

        try {
            savedReview = reviewRepository.save(review);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_REVIEW);
        }

        return savedReview;
    }

    private UserReview createUserReview(User user, Review review) {

        return UserReview.builder()
                .userReviewId(new UserReviewId(user.getUserId(), review.getReviewId()))
                .user(user)
                .review(review)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(Status.YES)
                .build();
    }

    private Review createReview(User user, Video video, PostVideoLevelReq postVideoLevelReq) {
        return Review.builder()
                .level(postVideoLevelReq.getLevel())
                .userId(user.getUserId())
                .videoId(video.getVideoId())
                .content(postVideoLevelReq.getContent())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(Status.YES)
                .build();
    }

    private UserVideo createUserAudio(User user, Video video) {

        return UserVideo.builder()
                .userVideoId(new UserVideoId(user.getUserId(), video.getVideoId()))
                .user(user)
                .video(video)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(Status.YES)
                .build();
    }


    private String audioTranslateToText(Video video, String audioInfo) throws BaseException {

        JsonElement element = JsonParser.parseString(audioInfo);
        String title = "";
        try {
            JsonArray contexts = element.getAsJsonObject().get("results").getAsJsonArray();
            title = contexts.get(0).getAsJsonObject().get("title").getAsString();
            int jsonSize = element.getAsJsonObject().get("results").getAsJsonArray().size();
            for (int index = 1; index < jsonSize; index++) {
                JsonElement context = contexts.get(index);
                String transcript = context.getAsJsonObject().get("transcript").getAsString();
                String confidence = context.getAsJsonObject().get("confidence").getAsString();
                String speakerTag = context.getAsJsonObject().get("speaker_tag").getAsString();
                JsonArray words = context.getAsJsonObject().get("words").getAsJsonArray();
                int wordSize = context.getAsJsonObject().get("words").getAsJsonArray().size();
                if(words.size() == 0 ) {
                    throw new BaseException(FAILED_TO_GET_WORDS);
                }
                String startTime = words.get(0).getAsJsonObject().get("start_time").getAsString();
                if (startTime.length() < 8) {
                    startTime += ".000000";
                }
                String endTime = words.get(wordSize - 1).getAsJsonObject().get("start_time").getAsString();
                if (endTime.length() < 8) {
                    endTime += ".000000";
                }

                SentenceEn sentenceEn = createSentenceEn(video, transcript, startTime, endTime, speakerTag, confidence);
                try {
                    sentenceEnRepository.save(sentenceEn);
                } catch (Exception exception) {
                    throw new BaseException(FAILED_TO_POST_SENTENCE);
                }
            }
        } catch (JsonIOException jsonIOException) {
            throw new BaseException(FAILED_TO_PARSING_SCRIPT);
        }


        //user_audio 저장
        //스크립트 문장 저장
        //썸네일 추출 및 URL 저장
        return title;
    }

    private SentenceEn createSentenceEn(Video video, String transcript, String startTime, String endTime, String speakerTag, String confidence) {
        return new SentenceEn.Builder()
                .video(video)
                .content(transcript)
                .startTime(startTime)
                .endTime(endTime)
                .speaker(speakerTag)
                .confidence(confidence)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(Status.YES)
                .build();
    }

    private Video saveVideo(Video video) throws BaseException {
        Video createVideo;
        try {
            createVideo = videoRepository.save(video);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_AUDIO);
        }
        return createVideo;
    }

    public String getInfo(WebClient webClient,String url) {
        PostVideoConvertorReq postVideoConvertorReq = new PostVideoConvertorReq(url);

        return  webClient.post()         // POST method
                .uri("/api2/youtube")    // baseUrl 이후 uri
                .bodyValue(postVideoConvertorReq)     // set body value
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()                 // client message 전송
                .bodyToMono(String.class)  // body type : EmpInfo
                .block();                   // await
    }

    private Video createVideo(PostVideoReq postVideoReq) {
        String url = postVideoReq.getYoutubeURL();
        String thumbnailURL = getThumbNailURL(url);
        String type = postVideoReq.getType().toLowerCase();
        return new Video.Builder()
                .videoType(type)
                .videoURL(url)
                .videoLevel(0F)
                .videoLevelCount(0)
                .videoEvaluation(false)
                .thumbNailURL(thumbnailURL)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(Status.YES)
                .build();
    }

    //TODO 예외 처리 하기

    private String getThumbNailURL(String url) {
        int index = url.indexOf("v=");
        if( index == -1 ) {
            return "NONE";
        }
        String videoCode = url.substring(url.lastIndexOf("v=")).substring(2);
        return "https://img.youtube.com/vi/" + videoCode + "/0.jpg";
    }


}

