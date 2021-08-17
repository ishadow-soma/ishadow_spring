package com.soma.ishadow.controllers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.BaseResponse;
import com.soma.ishadow.requests.PostSentenceReq;
import com.soma.ishadow.responses.PostSentenceRes;
import com.soma.ishadow.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SentenceController {

    private final VideoService videoService;

    @Autowired
    public SentenceController(VideoService videoService) {
        this.videoService = videoService;
    }

    /**
     * 문장 즐겨찾기 저장
     * @param postSentenceReq
     * @return
     */
    @PostMapping("/shadowing-player/sentence")
    public BaseResponse<PostSentenceRes> createBookmarkWithSentence(
            @RequestBody PostSentenceReq postSentenceReq
    ) {
        //
        try {
            return BaseResponse.succeed(videoService.createBookmark(postSentenceReq));
        } catch (BaseException exception ) {
            return BaseResponse.failed(exception.getStatus());
        }
    }

}
