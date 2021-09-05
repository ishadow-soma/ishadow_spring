package com.soma.ishadow.providers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.domains.category.Category;
import com.soma.ishadow.repository.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.soma.ishadow.configures.BaseResponseStatus.FAILED_TO_GET_CATEGORY;

@Service
public class CategoryProvider {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryProvider(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findCategory(Long categoryId) throws BaseException {

        return categoryRepository.findCategoryByCategoryId(categoryId)
                .orElseThrow(() -> new BaseException(FAILED_TO_GET_CATEGORY));

    }
}
