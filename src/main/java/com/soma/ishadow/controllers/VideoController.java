package com.soma.ishadow.controllers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.BaseResponse;
import com.soma.ishadow.providers.UserProvider;
import com.soma.ishadow.requests.PostVideoReq;
import com.soma.ishadow.responses.PostVideoRes;
import com.soma.ishadow.services.JwtService;
import com.soma.ishadow.services.VideoService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.soma.ishadow.configures.BaseResponseStatus.EXCEED_CONVERSION_COUNT;

@RestController
@RequestMapping("/api")
public class VideoController {

    private final Logger logger = LoggerFactory.getLogger(VideoController.class);
    private final VideoService videoService;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    @Autowired
    public VideoController(VideoService videoService, UserProvider userProvider, JwtService jwtService) {
        this.videoService = videoService;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }

    //영상 업로드 -> 유저 카운트 조회 ->
    @ApiOperation(value = "영상 업로드")
    @PostMapping(path = "/media")
    public BaseResponse<PostVideoRes> uploadVideo(
            @RequestPart(value = "file",required = false) MultipartFile video,
            @RequestParam(value = "youtubeURL",required = false) String youtubeURL,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "category") String category
    ) throws BaseException, IOException {

        Long userId = jwtService.getUserInfo();
        if(!userProvider.conversionCheck(userId)) {
            throw new BaseException(EXCEED_CONVERSION_COUNT);
        }
        PostVideoReq postVideoReq = new PostVideoReq(type,category,youtubeURL);
        try {
            return BaseResponse.succeed(videoService.upload(postVideoReq, video, userId));

        } catch (BaseException exception) {
            return BaseResponse.failed(exception.getStatus());
        }

    }

}
