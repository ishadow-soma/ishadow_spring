package com.soma.ishadow.controllers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.BaseResponse;
import com.soma.ishadow.providers.UserProvider;
import com.soma.ishadow.providers.VideoProvider;
import com.soma.ishadow.requests.PostVideoLevelReq;
import com.soma.ishadow.requests.PostVideoReq;
import com.soma.ishadow.responses.*;
import com.soma.ishadow.services.JwtService;
import com.soma.ishadow.services.VideoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import static com.soma.ishadow.configures.BaseResponseStatus.EXCEED_CONVERSION_COUNT;

@RestController
@RequestMapping("/api")
public class VideoController {

    private final Logger logger = LoggerFactory.getLogger(VideoController.class);
    private final VideoService videoService;
    private final VideoProvider videoProvider;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    @Qualifier("convertorRepository")
    private final HashMap<Long, Date> convertorRepository;

    @Autowired
    public VideoController(VideoService videoService, VideoProvider videoProvider, UserProvider userProvider, JwtService jwtService, HashMap<Long, Date> convertorRepository) {
        this.videoService = videoService;
        this.videoProvider = videoProvider;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
        this.convertorRepository = convertorRepository;
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
            @ApiImplicitParam(name = "youtubeURL", value = "youtube영상 url EX) https://www.youtube.com/watch?v=ez7NA6X3x6s")
    })
    @PostMapping(value = "/media", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public BaseResponse<PostVideoRes> uploadVideo(
            @ModelAttribute PostVideoReq postVideoReq,
            @RequestPart(value = "file",required = false) MultipartFile file
    ) throws Exception {

        logger.info(postVideoReq.getType());
        logger.info(file.getName());
        logger.info(postVideoReq.getYoutubeURL());
        logger.info(postVideoReq.getCategoryId().toString());
        Long userId = jwtService.getUserInfo();
        if(!userProvider.conversionCheck(userId)) {
            throw new BaseException(EXCEED_CONVERSION_COUNT);
        }
        try {
            return BaseResponse.succeed(videoService.upload(postVideoReq, file, userId));
        } catch (BaseException exception) {
            if(exception.getStatus().getCode() == 1018) {
                return BaseResponse.failed(exception.getStatus());
            }
            //TODO 추후 Redis로 대체
            convertorRepository.remove(userId);
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

    //TODO 유사한 난이도 영상 조회 (카테고리 별, 난이도 별) -> 10개씩
    @ApiOperation(value = "영상 추천 서비스 (카테고리 별, 난이도 별) 12개씩 페이징 처리")
    @GetMapping("/media")
    public BaseResponse<GetVideosRes> getVideos(
            @RequestParam(value = "categoryId",required = false, defaultValue = "20") Long categoryId,
            @RequestParam(value = "levelStart",required = false, defaultValue = "0.0") float levelStart,
            @RequestParam(value = "levelEnd",required = false, defaultValue = "5.0") float levelEnd,
            @RequestParam(value = "page",required = false, defaultValue = "1") int page
    ) {

        try {
            return BaseResponse.succeed(videoProvider.getVideos(categoryId, levelStart, levelEnd, page));
        } catch (BaseException exception ) {
            return BaseResponse.failed(exception.getStatus());
        }

    }


    @ApiOperation(value = "쉐도잉 영상 정보 가져오기",notes = "videoEvaluation가 true라면 영상의 난이도가 평가된 것" +
            "-> true면 난이도 평가 X, false라면 90% 이상일 경우 난이도 평가하는 팝업 띄우기")
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
