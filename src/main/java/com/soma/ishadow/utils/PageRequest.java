package com.soma.ishadow.utils;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequest {

    private int page;
    private int size;
    private Sort.Direction direction;

    public PageRequest(int page, int size, Sort.Direction direction) {
        this.page = page;
        this.size = size;
        this.direction = direction;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page, size, direction );
    }
}
