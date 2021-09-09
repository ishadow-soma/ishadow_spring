package com.soma.ishadow.repository.category;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.category.Category;
import com.soma.ishadow.domains.category.QCategory;
import com.soma.ishadow.domains.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CategoryRepositoryImpl extends QuerydslRepositorySupport implements CategoryRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public CategoryRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Category.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<Category> findCategoryByCategoryName(String categoryName) {
        QCategory category = QCategory.category;
        return Optional.ofNullable(queryFactory.selectFrom(category)
                .where(category.categoryName.eq(categoryName), category.status.eq(Status.YES))
                .fetchOne());
    }

    @Override
    public Optional<Category> findCategoryByCategoryId(Long categoryId) {
        QCategory category = QCategory.category;
        return Optional.ofNullable(queryFactory.selectFrom(category)
                .where(category.categoryId.eq(categoryId), category.status.eq(Status.YES))
                .fetchOne());
    }

}
