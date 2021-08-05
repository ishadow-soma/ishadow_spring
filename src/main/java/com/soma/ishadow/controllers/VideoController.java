package com.soma.ishadow.controllers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.BaseResponse;
import com.soma.ishadow.requests.PostVideoReq;
import com.soma.ishadow.responses.JwtRes;
import com.soma.ishadow.services.UserService;
import com.soma.ishadow.services.VideoService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class VideoController {

    private final Logger logger = LoggerFactory.getLogger(VideoController.class);
    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }


    //영상 업로드 -> 유저 카운트 조회 ->
    @ApiOperation(value = "영상 업로드")
    @PostMapping(path = "/media")
    public BaseResponse<JwtRes> uploadVideo(
            @RequestBody PostVideoReq postVideoReq
    ) throws BaseException, IOException {

        try {
            return BaseResponse.succeed(videoService.upload(postVideoReq));
        } catch (BaseException exception) {
            return BaseResponse.failed(exception.getStatus());
        }

    }
}
