package com.soma.ishadow.services;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.repository.VideoRepository;
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

import static com.soma.ishadow.configures.BaseResponseStatus.EMPTY_VIDEO;

@Service
public class VideoService {

    private final S3Util s3Util;
    private final Logger logger = LoggerFactory.getLogger(VideoService.class);

    @Autowired
    public VideoService(S3Util s3Util) {
        this.s3Util = s3Util;
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
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://ishadow.kr")
                    .build();
            String info = getInfo(webClient, url);
            System.out.println(info);

        }
        System.out.println(url);
        //URL과 type을 다른 서버로 전송

        //받은 정보를 DB에 저장
        return new PostVideoRes(1L);
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
}

