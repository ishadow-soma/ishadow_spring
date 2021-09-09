package com.soma.ishadow.controllers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.BaseResponse;
import com.soma.ishadow.providers.UserProvider;
import com.soma.ishadow.providers.VideoProvider;
import com.soma.ishadow.requests.PostVideoLevelReq;
import com.soma.ishadow.requests.PostVideoReq;
import com.soma.ishadow.responses.GetShadowingRes;
import com.soma.ishadow.responses.PostVideoLevelRes;
import com.soma.ishadow.responses.PostVideoRes;
import com.soma.ishadow.services.JwtService;
import com.soma.ishadow.services.VideoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.soma.ishadow.configures.BaseResponseStatus.EXCEED_CONVERSION_COUNT;

@RestController
@RequestMapping("/api")
public class VideoController {

    private final Logger logger = LoggerFactory.getLogger(VideoController.class);
    private final VideoService videoService;
    private final VideoProvider videoProvider;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    @Autowired
    public VideoController(VideoService videoService, VideoProvider videoProvider, UserProvider userProvider, JwtService jwtService) {
        this.videoService = videoService;
        this.videoProvider = videoProvider;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }

    //영상 업로드 -> 유저 카운트 조회 ->
    @ApiOperation(value = "영상 업로드, \"Content-Type: multipart/form-data\"", notes =
            "1, '라이프 스타일'\n" +
            "2, '음악/댄스'\n" +
            "3, '뷰티/패션'\n" +
            "4, '영화/애니메이션'\n" +
            "5, '키즈'\n" +
            "6, '게임'\n" +
            "7, '여행/아웃도어'\n" +
            "8, '스포츠/건강'\n" +
            "9, '뉴스/정치/이슈'\n" +
            "10, '기관/단체/정부'\n" +
            "11, '엔터테인먼트'\n" +
            "12, '푸드/쿠킹'\n" +
            "13, '인물/유명인'\n" +
            "14, 'IT/기술/과학'\n" +
            "15, '동물/펫'\n" +
            "16, '취미'\n" +
            "17, '차/배/바이크'\n" +
            "18, '경제/금융/제테크'\n" +
            "19, '교육/강의'\n" +
            "20, 'ALL'\n" +
            "\n")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "YOUTUBE, UPLOAD -> 현재는 YOUTUBE만 가능"),
            @ApiImplicitParam(name = "categoryId", value = "categoryId 배열 입력 -> 1, 2, 3"),
            @ApiImplicitParam(name = "youtubeURL", value = "youtube영상 url")
    })
    @PostMapping(value = "/media", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public BaseResponse<PostVideoRes> uploadVideo(
            @ModelAttribute PostVideoReq postVideoReq,
            @RequestPart(value = "file",required = false) MultipartFile video
    ) throws BaseException, IOException {

        Long userId = jwtService.getUserInfo();
        if(!userProvider.conversionCheck(userId)) {
            throw new BaseException(EXCEED_CONVERSION_COUNT);
        }
        try {
            return BaseResponse.succeed(videoService.upload(postVideoReq, video, userId));
        } catch (BaseException exception) {
            return BaseResponse.failed(exception.getStatus());
        }

    }

    @ApiOperation(value = "쉐도잉 영상 난이도 평가 하기", notes = "5점 만점 소수점 한 자리까지 가능")
    @PostMapping("/media/{videoId}/level")
    public BaseResponse<PostVideoLevelRes> updateShadowingVideoLevel(
            @PathVariable("videoId") Long videoId,
            @RequestBody PostVideoLevelReq postVideoLevelReq
    ) {
        try {
            return BaseResponse.succeed(videoService.createVideo(videoId, postVideoLevelReq));
        } catch (BaseException exception ) {
            return BaseResponse.failed(exception.getStatus());
        }
    }


    @ApiOperation(value = "쉐도잉 영상 정보 가져오기")
    @GetMapping("/shadowing-player")
    public BaseResponse<GetShadowingRes> getShadowingInformation(
            @RequestParam(value = "videoId", required = true) Long videoId
    ) {
        try {
            return BaseResponse.succeed(videoProvider.getShadowing(videoId));
        } catch (BaseException exception ) {
            return BaseResponse.failed(exception.getStatus());
        }
    }


}
