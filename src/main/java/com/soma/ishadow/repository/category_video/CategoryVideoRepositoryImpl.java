package com.soma.ishadow.repository.category_video;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soma.ishadow.domains.category_video.CategoryVideo;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryVideoRepositoryImpl extends QuerydslRepositorySupport implements CategoryVideoRepositoryQuerydsl {


    private final JPAQueryFactory queryFactory;


    public CategoryVideoRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CategoryVideo.class);
        this.queryFactory = queryFactory;
    }


}
