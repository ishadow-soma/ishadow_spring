package com.soma.ishadow.repository.category;

import com.soma.ishadow.domains.category.Category;

import java.util.Optional;

public interface CategoryRepositoryQuerydsl {

    Optional<Category> findCategoryByCategoryName(String categoryName);

    Optional<Category> findCategoryByCategoryId(Long categoryId);
}
