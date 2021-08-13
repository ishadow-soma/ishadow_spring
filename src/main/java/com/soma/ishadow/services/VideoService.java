package com.soma.ishadow.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.domains.audio.Audio;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.sentence_en.SentenceEn;
import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.domains.user_audio.UserAudio;
import com.soma.ishadow.domains.user_audio.UserAudioId;
import com.soma.ishadow.repository.audio.AudioRepository;
import com.soma.ishadow.repository.sentense.SentenceEnRepository;
import com.soma.ishadow.repository.user_audio.UserAudioRepository;
import com.soma.ishadow.requests.PostVideoConvertorReq;
import com.soma.ishadow.requests.PostVideoReq;
import com.soma.ishadow.responses.PostVideoRes;
import com.soma.ishadow.utils.S3Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.soma.ishadow.configures.BaseResponseStatus.*;
import static com.soma.ishadow.configures.Constant.baseUrl;

@Service
public class VideoService {

    private final S3Util s3Util;
    private final AudioRepository audioRepository;
    private final UserAudioRepository userAudioRepository;
    private final SentenceEnRepository sentenceEnRepository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(VideoService.class);

    @Autowired
    public VideoService(S3Util s3Util, AudioRepository audioRepository, UserAudioRepository userAudioRepository, SentenceEnRepository sentenceEnRepository, UserService userService) {
        this.s3Util = s3Util;
        this.audioRepository = audioRepository;
        this.userAudioRepository = userAudioRepository;
        this.sentenceEnRepository = sentenceEnRepository;
        this.userService = userService;
    }

    @Transactional
    public PostVideoRes upload(PostVideoReq postVideoReq, MultipartFile video, Long userId) throws BaseException, IOException {

        String type = postVideoReq.getType();
        String category = postVideoReq.getCategory();
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

            Audio audio = createAudio(postVideoReq);
            //audio DB에 저장
            Audio createdAudio = saveAudio(audio);
            logger.info("영상 저장 성공: " + audio.getAudioId());

            //audio Id를 이용해서 user_audio에 저장하기
            UserAudio userAudio = saveUserAudio(userId, createdAudio);
            logger.info("영상 유저 조인 테이블 저장 성공: " + userAudio.getUserAudioId().toString());

            WebClient webClient = createWebClient();

            String audioInfo = getInfo(webClient, url);
            logger.info("영상 변환 성공: " + url);

            audioTranslateToText(audio, audioInfo);

            return new PostVideoRes(createdAudio.getAudioId());
        }

        throw new BaseException(INVALID_AUDIO_TYPE);
    }

    private WebClient createWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    private UserAudio saveUserAudio(Long userId, Audio audio) throws BaseException {

        User user = userService.findById(userId);
        UserAudio userAudio = createUserAudio(user, audio);
        UserAudio newUserAudio;
        try {
            newUserAudio = userAudioRepository.save(userAudio);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_USERAUDIO);
        }
        return newUserAudio;
    }

    private UserAudio createUserAudio(User user, Audio audio) {

        return UserAudio.builder()
                .userAudioId(new UserAudioId(user.getUserId(), audio.getAudioId()))
                .user(user)
                .audio(audio)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(Status.YES)
                .build();
    }


    private void audioTranslateToText(Audio audio, String audioInfo) throws BaseException {

        JsonElement element = JsonParser.parseString(audioInfo);
        try {
            JsonArray contexts = element.getAsJsonObject().get("results").getAsJsonArray();
            int jsonSize = element.getAsJsonObject().get("results").getAsJsonArray().size();
            for (int index = 0; index < jsonSize; index++) {
                JsonElement context = contexts.get(index);
                String transcript = context.getAsJsonObject().get("transcript").getAsString();
                String confidence = context.getAsJsonObject().get("confidence").getAsString();
                String speakerTag = context.getAsJsonObject().get("speaker_tag").getAsString();
                JsonArray words = context.getAsJsonObject().get("words").getAsJsonArray();
                int wordSize = context.getAsJsonObject().get("words").getAsJsonArray().size();
                String startTime = words.get(0).getAsJsonObject().get("start_time").getAsString();
                if (startTime.length() < 8) {
                    startTime += ".000000";
                }
                String endTime = words.get(wordSize - 1).getAsJsonObject().get("start_time").getAsString();
                if (endTime.length() < 8) {
                    endTime += ".000000";
                }

                SentenceEn sentenceEn = createSentenceEn(audio, transcript, startTime, endTime, speakerTag, confidence);
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

    }

    private SentenceEn createSentenceEn(Audio audio, String transcript, String startTime, String endTime, String speakerTag, String confidence) {
        return new SentenceEn.Builder()
                .audio(audio)
                .content(transcript)
                .startTime(startTime)
                .endTime(endTime)
                .speaker(speakerTag)
                .confidence(confidence)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(Status.YES)
                .build();
    }

    private Audio saveAudio(Audio audio) throws BaseException {
        Audio createAudio;
        try {
            createAudio = audioRepository.save(audio);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_AUDIO);
        }
        return createAudio;
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

    private Audio createAudio(PostVideoReq postVideoReq) {
        String url = postVideoReq.getYoutubeURL();
        String type = postVideoReq.getType().toLowerCase();
        return new Audio.Builder()
                .audioType(type)
                .audioURL(url)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(Status.YES)
                .build();
    }
}

