package com.soma.ishadow.services;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.domains.audio.Audio;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.repository.audio.AudioRepository;
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

@Service
public class VideoService {

    private final S3Util s3Util;
    private final AudioRepository audioRepository;
    private final Logger logger = LoggerFactory.getLogger(VideoService.class);

    @Autowired
    public VideoService(S3Util s3Util, AudioRepository audioRepository) {
        this.s3Util = s3Util;
        this.audioRepository = audioRepository;
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

            WebClient webClient = WebClient.builder()
                    .baseUrl("https://ishadow.kr")
                    .build();
            String audioInfo = getInfo(webClient, url);
            logger.info("영상 변환 성공: " + url);

            //user_audio 저장
            //스크립트 문장 저장
            //썸네일 추출 및 URL 저장
            audioTranslateToText(audioInfo);

            return new PostVideoRes(createdAudio.getAudioId());
        }

        throw new BaseException(INVALID_AUDIO_TYPE);
    }

    private void audioTranslateToText(String audioInfo) {
        //user_audio 저장
        //스크립트 문장 저장
        //썸네일 추출 및 URL 저장

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

