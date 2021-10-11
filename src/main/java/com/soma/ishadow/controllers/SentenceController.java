package com.soma.ishadow.controllers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.BaseResponse;
import com.soma.ishadow.providers.SentenceProvider;
import com.soma.ishadow.requests.PostSentenceReq;
import com.soma.ishadow.responses.GetBookmarkRes;
import com.soma.ishadow.responses.PostSentenceRes;
import com.soma.ishadow.services.SentenceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SentenceController {

    private final SentenceService sentenceService;
    private final SentenceProvider sentenceProvider;

    @Autowired
    public SentenceController(SentenceService sentenceService, SentenceProvider sentenceProvider) {
        this.sentenceService = sentenceService;
        this.sentenceProvider = sentenceProvider;
    }

    @GetMapping("/check")
    @ApiOperation(value = "체크")
    public BaseResponse<String> healthCheck(
    ) {
        return BaseResponse.succeed("server connection success");
    }

    /**
     * 문장 즐겨찾기 저장
     * @param postSentenceReq
     * @return
     */
    @ApiOperation(value = "구간 반복 저장")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sentenceSaveType", value = "REPEAT 구간 반복 문장, FAVORITE 즐겨찾기 문장"),
            @ApiImplicitParam(name = "sentences", value = "sentences: [1, 2, 3] 이런식으로 저장, 숫자는 sentenceId"),
            @ApiImplicitParam(name = "videoId", value = "현재 VIDEO ID")
    })
    @PostMapping("/shadowing-player/sentence")
    public BaseResponse<PostSentenceRes> createBookmarkWithSentence(
            @RequestBody PostSentenceReq postSentenceReq
    ) {
        try {
            return BaseResponse.succeed(sentenceService.createBookmark(postSentenceReq));
        } catch (BaseException exception ) {
            return BaseResponse.failed(exception.getStatus());
        }
    }

    @ApiOperation(value = "구간 반복 조회")
    @GetMapping("/shadowing-player/bookmark")
    public BaseResponse<List<GetBookmarkRes>> createBookmarkWithSentence(
            @RequestParam("videoId") Long videoId
    ){
        try {
            return BaseResponse.succeed(sentenceProvider.getBookmark(videoId));
        } catch (BaseException exception ) {
            return BaseResponse.failed(exception.getStatus());
        }
    }
}
