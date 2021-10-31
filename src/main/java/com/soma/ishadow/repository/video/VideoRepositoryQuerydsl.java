package com.soma.ishadow.repository.video;

import com.querydsl.core.QueryResults;
import com.soma.ishadow.domains.video.Video;
import com.soma.ishadow.responses.GetVideoRes;
import com.soma.ishadow.utils.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VideoRepositoryQuerydsl {

    Video save(Video video);

    Optional<Video> findById(Long videoId);

    List<Video> findYoutubeVideoByUserId(Long userId);

    List<Video> findUploadVideoByUserId(Long userId);

    Page<Video> findByCategory(Long categoryId, Pageable pageable);

    Page<Video> findByCategoryAndLevel(Long categoryId, float levelStart, float levelEnd, int videoType, Pageable pageable);

    int findVideoByCount(Long categoryId, float levelStart, float levelEnd, int videoType);

    List<GetVideoRes> findByCategoryAndLevelByRecommend(Long categoryId, float lowLevel, float highLevel);
}
