package com.soma.ishadow.repository.category_video;

import com.soma.ishadow.domains.category_video.CategoryVideo;
import com.soma.ishadow.domains.category_video.CategoryVideoId;
import com.soma.ishadow.repository.category.CategoryRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryVideoRepository extends JpaRepository<CategoryVideo, CategoryVideoId>, CategoryVideoRepositoryQuerydsl {
}
