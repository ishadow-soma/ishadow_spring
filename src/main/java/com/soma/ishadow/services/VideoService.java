package com.soma.ishadow.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.domains.category.Category;
import com.soma.ishadow.domains.category_video.CategoryVideo;
import com.soma.ishadow.domains.category_video.CategoryVideoId;
import com.soma.ishadow.domains.video.Video;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.sentence_en.SentenceEn;
import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.domains.user_video.UserVideo;
import com.soma.ishadow.domains.user_video.UserVideoId;
import com.soma.ishadow.providers.CategoryProvider;
import com.soma.ishadow.providers.UserVideoProvider;
import com.soma.ishadow.repository.category_video.CategoryVideoRepository;
import com.soma.ishadow.repository.video.VideoRepository;
import com.soma.ishadow.repository.sentense.SentenceEnRepository;
import com.soma.ishadow.repository.user_video.UserVideoRepository;
import com.soma.ishadow.requests.PostVideoConvertorReq;
import com.soma.ishadow.requests.PostVideoReq;
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
    private final CategoryProvider categoryProvider;
    private final CategoryVideoRepository categoryVideoRepository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(VideoService.class);

    @Qualifier("URLRepository")
    private final HashMap<String, Long> URLRepository;

    @Autowired
    public VideoService(S3Util s3Util, VideoRepository videoRepository, UserVideoRepository userVideoRepository, UserVideoProvider userVideoProvider, SentenceEnRepository sentenceEnRepository, CategoryProvider categoryProvider, CategoryVideoRepository categoryVideoRepository, UserService userService, HashMap<String, Long> urlRepository) {
        this.s3Util = s3Util;
        this.videoRepository = videoRepository;
        this.userVideoRepository = userVideoRepository;
        this.userVideoProvider = userVideoProvider;
        this.sentenceEnRepository = sentenceEnRepository;
        this.categoryProvider = categoryProvider;
        this.categoryVideoRepository = categoryVideoRepository;
        this.userService = userService;
        this.URLRepository = urlRepository;
    }

    public PostVideoRes upload(PostVideoReq postVideoReq, MultipartFile video, Long userId) throws BaseException, IOException {

        String type = postVideoReq.getType();
        List<Long> categoryId = postVideoReq.getCategoryId();
        checkCategoryId(categoryId);
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
                    UserVideo userVideo = saveUserVideo(userId, exitedVideo);
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

            //audio Id를 이용해서 user_audio에 저장하기
            UserVideo userVideo = saveUserVideo(userId, updatedVideo);
            logger.info("영상 유저 조인 테이블 저장 성공: " + userVideo.getUserVideoId().toString());

            URLRepository.put(url, updatedVideo.getVideoId());
            return new PostVideoRes(updatedVideo.getVideoId(), title, url);
        }

        throw new BaseException(INVALID_AUDIO_TYPE);
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

    private UserVideo saveUserVideo(Long userId, Video video) throws BaseException {

        User user = userService.findById(userId);
        UserVideo userVideo = createUserAudio(user, video);
        UserVideo newUserVideo;
        try {
            newUserVideo = userVideoRepository.save(userVideo);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_USERAUDIO);
        }
        return newUserVideo;
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

    private void checkCategoryId(List<Long> categoryId) {

        if(categoryId.isEmpty()) {
            categoryId.add(20L);
        }
    }
}

