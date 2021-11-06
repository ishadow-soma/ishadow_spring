package com.soma.ishadow.services;

import com.google.gson.*;
import org.apache.commons.io.FileUtils;
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
import org.apache.commons.io.IOUtils;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.soma.ishadow.configures.BaseResponseStatus.*;
import static com.soma.ishadow.configures.Constant.*;

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
    @Qualifier("convertorRepository")
    private final HashMap<Long, Date> convertorRepository;

    @Autowired
    public VideoService(S3Util s3Util, VideoRepository videoRepository, UserVideoRepository userVideoRepository, UserVideoProvider userVideoProvider, SentenceEnRepository sentenceEnRepository, ReviewRepository reviewRepository, UserReviewRepository userReviewRepository, CategoryProvider categoryProvider, VideoProvider videoProvider, ReviewProvider reviewProvider, CategoryVideoRepository categoryVideoRepository, UserService userService, JwtService jwtService, HashMap<String, Long> urlRepository, HashMap<Long, Date> convertorRepository) {
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
        this.convertorRepository = convertorRepository;
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
    //TODO map 사용해서 중복 URL 초기화 안돼서 redis로 바꾸기.
    //TODO 영상 변환하고 있는데 또 영상 변환하기 누르면 에러 발생하게 하기
    @Transactional(rollbackFor = BaseException.class)
    public PostVideoRes upload(PostVideoReq postVideoReq, MultipartFile video, Long userId) throws Exception {

        //TODO 영상 변환 여부 확인 로직 수정
        if(convertorRepository.containsKey(userId)){
            logger.info("해당 유저 아이디 " + userId + "가 존재합니다.");
            if(timeDifferenceLessThanTenSecond(userId)) {
                logger.info("이미 영상 변환 중입니다.");
                throw new BaseException(ALREADY_EXISTED_CONVERTOR);
            }
            convertorRepository.remove(userId);
        }
        convertorRepository.put(userId, new Date());
        String type = postVideoReq.getType();
        List<Long> categoryId = postVideoReq.getCategoryId();
        if(!categoryId.contains(20L)) categoryId.add(20L);
        logger.info(String.valueOf(categoryId));
        String url = postVideoReq.getYoutubeURL();
        String thumbNail = "";
        String fileFormat = "NONE";
        File videoFile = null;

        if (type == null || !(type.equals("UPLOAD") || type.equals("YOUTUBE"))) {
            throw new BaseException(INVALID_AUDIO_TYPE);
        }

        //S3에 저장하고 URL 반환하기
        if (type.equals("UPLOAD")) {
            if (video.isEmpty()) {
                logger.info("영상이 비어있습니다.");
                convertorRepository.remove(userId);
                throw new BaseException(EMPTY_VIDEO);
            }

            logger.info(userId + ": " + video.getOriginalFilename());
            fileFormat = Objects.requireNonNull(video.getOriginalFilename()).toLowerCase();
            if(fileFormat.endsWith(".mp4")) {
                fileFormat = "mp4";
            }
            if(fileFormat.endsWith(".mp3")) {
                fileFormat = "mp3";
            }
            if(fileFormat.endsWith(".wav")) {
                fileFormat = "wav";
            }
            if( !checkFileFormat(fileFormat) ) {
                throw new BaseException(UNSUPPORTED_FORMAT);
            }


            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String videoPath = "/home/ubuntu/video/" + today + "/";
            String videoName = video.getOriginalFilename();
            String fileName = today + "-" + userId + "-" + videoName;

            videoFile = new File(videoPath + fileName);

            if (!videoFile.exists()) {
                videoFile.mkdirs();
            }

            video.transferTo(videoFile);
            String makeFile = mkdirPath + " \"mkdir -p " + videoPath + "\"";
            shellCmd(makeFile);
            String command = startFilePath + videoPath + fileName + endFilePath + "video/" + today;
            logger.info("command : " + command);
            shellCmd(command);
            url = videoBasePath + today + "/" + fileName;
            logger.info("upload url: " + url);
            postVideoReq.setYoutubeURL(url);

            if(fileFormat.equals("mp4")) {
                logger.info("getThumbNail");
                makeFile = "mkdir -p " + "/home/ubuntu/image/" + today + "/";
                shellCmd(makeFile);
                makeFile = mkdirPath + " \"" + makeFile + "\"";
                logger.info("mkidrPath : " + makeFile);
                shellCmd(makeFile);

                String thumNailName = today + "-" + userId + "-" + video.getOriginalFilename().substring(0, video.getOriginalFilename().length() - 4) + ".png";
                String thumbNailPath = "/home/ubuntu/image/" + today + "/" + thumNailName;
                File thumbNailFile = new File(thumbNailPath);
                createThumbNail(videoFile, thumbNailFile);
                thumbNail = imageBasePath + today + "/" + thumNailName;
                logger.info("thumbNail URL: " + thumbNail);

                command = startFilePath + thumbNailPath + endFilePath + "image/" + today;
                logger.info("command : " + command);
                shellCmd(command);
            }
        }

        if (type.equals("YOUTUBE")) {

            if (URLRepository.get(url) != null) {
                logger.info("해당 " + url + " 영상이 존재합니다.");
                Video exitedVideo = videoRepository.findById(URLRepository.get(url)).orElse(null);
                if (exitedVideo == null) {
                    logger.info("URLRepository에는 값이 존재하는데 DB에는 video가 없음");
                    URLRepository.remove(url);
                    throw new BaseException(FAILED_TO_GET_VIDEO_YOUTUBE);
                }

                if (!userVideoProvider.findByUserVideo(userId, exitedVideo.getVideoId())) {
                    User user = userService.findById(userId);
                    UserVideo userVideo = saveUserVideo(user, exitedVideo);
                    logger.info("영상 유저 조인 테이블 저장 성공: " + userVideo.getUserVideoId().toString());
                }

                convertorRepository.remove(userId);
                return new PostVideoRes(exitedVideo.getVideoId(), exitedVideo.getVideoName(), url);
            }
        }

        Video newVideo = createVideo(postVideoReq, thumbNail, fileFormat);
        //video DB에 저장
        Video createdVideo = saveVideo(newVideo);
        logger.info("영상 저장 성공: " + createdVideo.getVideoId());

        //categoryId 저장하기
        saveCategoryVideo(categoryId, createdVideo);
        logger.info("카테고리 영상 조인 테이블 저장 성공");

        WebClient webClient = createWebClient();


        String videoInfo = getInfo(webClient, url, videoFile, type);
        logger.info("영상 변환 성공: " + url);

        String title = audioTranslateToText(createdVideo, videoInfo);
        if (type.equals("UPLOAD")) {
            title = video.getOriginalFilename().substring(0, video.getOriginalFilename().length() - 4);
        }
        createdVideo.setVideoName(title);
        Video updatedVideo = saveVideo(createdVideo);
        logger.info("영상 제목 저장 성공: " + updatedVideo.getVideoId());

        //videoId를 이용해서 user_video에 저장하기
        User user = userService.findById(userId);
        UserVideo userVideo = saveUserVideo(user, updatedVideo);
        logger.info("영상 유저 조인 테이블 저장 성공: " + userVideo.getUserVideoId().toString());

        URLRepository.put(url, updatedVideo.getVideoId());
        convertorRepository.remove(userId);
        return new PostVideoRes(updatedVideo.getVideoId(), title, url);

    }

    public PostVideoRes createContent(PostVideoReq postVideoReq, MultipartFile file,String title) throws BaseException, IOException {

        Long userId = jwtService.getUserInfo();
        User user = userService.findById(userId);
        if(!user.getEmail().equals("admin")) {
            throw new BaseException(INVALID_ACCESS);
        }
        List<Long> categoryId = postVideoReq.getCategoryId();
        if(!categoryId.contains(20L)) categoryId.add(20L);
        logger.info(String.valueOf(categoryId));
        String url = postVideoReq.getYoutubeURL();
        String thumbNail = "";
        ByteArrayInputStream stream = new   ByteArrayInputStream(file.getBytes());
        String sentences = IOUtils.toString(stream, StandardCharsets.UTF_8);
        sentences = sentences.replaceAll(",",".").replaceAll(" --> ","-->");
        String[] tempSentences = sentences.split("\r\n\r\n");

        Video newVideo = createVideo(postVideoReq, thumbNail, "NONE");
        newVideo.setVideoName(title);
        //video DB에 저장
        Video createdVideo = saveVideo(newVideo);
        logger.info("영상 저장 성공: " + createdVideo.getVideoId());

        //categoryId 저장하기
        saveCategoryVideo(categoryId, createdVideo);
        logger.info("카테고리 영상 조인 테이블 저장 성공");

        for(int i = 0; i < tempSentences.length; i++) {


            logger.info(tempSentences[i]);
            String[] tempSentence = tempSentences[i].split("\n");
            String[] times = tempSentence[1].split("-->");
            String startTime = times[0];
            String endTime = times[1];
            StringBuilder content = new StringBuilder();
            for(int j = 2; j < tempSentence.length; j++) {
                content.append(tempSentence[j]);
            }

            SentenceEn sentenceEn = createSentenceEn(createdVideo, content.toString(), startTime, endTime);
            try {
                sentenceEnRepository.save(sentenceEn);
            } catch (Exception exception) {
                throw new BaseException(FAILED_TO_POST_SENTENCE);
            }
        }


        Video updatedVideo = saveVideo(createdVideo);
        logger.info("영상 제목 저장 성공: " + updatedVideo.getVideoId());

        //videoId를 이용해서 user_video에 저장하기
        UserVideo userVideo = saveUserVideo(user, updatedVideo);
        logger.info("영상 유저 조인 테이블 저장 성공: " + userVideo.getUserVideoId().toString());

        return new PostVideoRes(updatedVideo.getVideoId(), title, url);
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

        JsonElement contexts = JsonParser.parseString(audioInfo);
        String title = "";
        try {
            //JsonElement contexts = element.getAsJsonObject().get("results");
            logger.info(video.getVideoId() + "번 영상 변환 성공");
            title = contexts.getAsJsonObject().get("title").getAsString();
            JsonArray jsonArray = contexts.getAsJsonObject().get("transcript").getAsJsonArray();
            for (int index = 0; index < jsonArray.size(); index++) {
                JsonElement jsonElement = jsonArray.get(index);
                String script = jsonElement.getAsJsonObject().get("script").getAsString();
                String startTime = jsonElement.getAsJsonObject().get("start_time").getAsString();
                String endTime = jsonElement.getAsJsonObject().get("end_time").getAsString();
                if (startTime.length() < 8) {
                    startTime += ".000000";
                }
                if (endTime.length() < 8) {
                    endTime += ".000000";
                }

                SentenceEn sentenceEn = createSentenceEn(video, script, startTime, endTime);
                try {
                    sentenceEnRepository.save(sentenceEn);
                    logger.info(video.getVideoId() + "번 영상 및 문장 저장 성공");
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

    private SentenceEn createSentenceEn(Video video, String transcript, String startTime, String endTime) {
        return new SentenceEn.Builder()
                .video(video)
                .content(transcript)
                .startTime(startTime)
                .endTime(endTime)
                .speaker("NONE")
                .confidence("NONE")
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

    public String getInfo(WebClient webClient,String url, File file, String type) throws BaseException {

        PostVideoConvertorReq postVideoConvertorReq;

        try {
            String subURL = "";
            if(type.equals("UPLOAD")) {
                subURL = "/api2/local";

                logger.info(file.getAbsolutePath());
                postVideoConvertorReq = new PostVideoConvertorReq(file.getAbsolutePath());
                return webClient.post()         // POST method
                        .uri(subURL)    // baseUrl 이후 uri
                        //.contentType(MediaType.MULTIPART_FORM_DATA)
                        //.body(BodyInserters.fromMultipartData(fromFile(file)))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .bodyValue(postVideoConvertorReq)     // set body value
                        .retrieve()                 // client message 전송
                        .bodyToMono(String.class)  // body type : EmpInfo
                        .block();                   // await

            }
            else if(type.equals("YOUTUBE")) {
                subURL = "/api2/youtube";
                postVideoConvertorReq = new PostVideoConvertorReq(url);

                return webClient.post()         // POST method
                        .uri(subURL)    // baseUrl 이후 uri
                        .bodyValue(postVideoConvertorReq)     // set body value
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .retrieve()                 // client message 전송
                        .bodyToMono(String.class)  // body type : EmpInfo
                        .block();                   // await
            }
            else {
                throw new BaseException(INVALID_CONVERTOR_TYPE);
            }
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_CONVERTOR_VIDEO);
        }
    }

    private File convertFile(MultipartFile file) throws IOException {
        File newFile = new File(file.getOriginalFilename());
        file.transferTo(newFile);
        return newFile;
    }

    private Video createVideo(PostVideoReq postVideoReq, String thumbNail, String fileFormat) {
        String url = postVideoReq.getYoutubeURL();
        String thumbnailURL = postVideoReq.getType().equals("YOUTUBE") ? getThumbNailURL(url) : thumbNail;
        int videoFormat = fileFormat.equals("NONE") ? 0 : 1;
        String type = postVideoReq.getType().toLowerCase();
        logger.info(type);
        return new Video.Builder()
                .videoType(type)
                .videoURL(url)
                .videoLevel(0F)
                .videoLevelCount(0)
                .videoSampling(videoFormat)
                .videoChannel(1)
                .videoEvaluation(false)
                .thumbNailURL(thumbnailURL)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(Status.YES)
                .build();
    }


    private boolean checkFileFormat(String fileFormat) {
        return fileFormat.equals("mp4") || fileFormat.equals("mp3") || fileFormat.equals("wav");
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

    public MultiValueMap<String, HttpEntity<?>> fromFile(File file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("info", file);
        return builder.build();
    }

    private static void shellCmd(String command) throws Exception {

        String s;
        Process p;
        try {
            //이 변수에 명령어를 넣어주면 된다.
            String[] cmd = {"/bin/sh","-c", command};
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println(s);
            p.waitFor();
            System.out.println("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
            throw new BaseException(FAILED_RUNNING_SHELL_SCRIPT);
        }

    }

    public String createThumbNail(File source, File thumbnail) throws IOException, JCodecException {
        logger.info("extracting thumbnail from video");
        int frameNumber = 5;

        Picture picture = FrameGrab.getFrameFromFile(source, frameNumber);

        BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
        ImageIO.write(bufferedImage, IMAGE_PNG_FORMAT, thumbnail);
        return thumbnail.getAbsolutePath();
    }

    private boolean timeDifferenceLessThanTenSecond(Long userId) {

        Date current = new Date();
        Date time = convertorRepository.get(userId);
        return current.getTime() - time.getTime() <= 30000;
    }

    /**
     * 비디오 삭제
     * @param videoId
     * @return
     */
    @Transactional
    public void deleteVideo(Long videoId) throws BaseException {

        Video video = videoProvider.findVideoById(videoId);
        video.setStatus(Status.NO);
    }
}

