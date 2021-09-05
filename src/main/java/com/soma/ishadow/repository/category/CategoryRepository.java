package com.soma.ishadow.repository.category;

import com.soma.ishadow.domains.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryQuerydsl {

    Optional<Category> findCategoryByCategoryName(String categoryName);

    Optional<Category> findCategoryByCategoryId(Long categoryId);

}
