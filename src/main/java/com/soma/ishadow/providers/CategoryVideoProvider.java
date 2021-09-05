package com.soma.ishadow.providers;

import com.soma.ishadow.repository.category_video.CategoryVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryVideoProvider {

    private final CategoryVideoRepository categoryVideoRepository;

    @Autowired
    public CategoryVideoProvider(CategoryVideoRepository categoryVideoRepository) {
        this.categoryVideoRepository = categoryVideoRepository;
    }
}
