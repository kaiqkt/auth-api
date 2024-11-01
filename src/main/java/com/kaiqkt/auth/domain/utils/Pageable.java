package com.kaiqkt.auth.domain.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class Pageable {
    public static PageRequest getPageRequest(Integer page, Integer size, String direction) {
        return PageRequest.of(
                page == null ? Constants.DEFAULT_PAGE : page,
                size > Constants.DEFAULT_PAGE_SIZE ? Constants.DEFAULT_PAGE_SIZE : size,
                Sort.Direction.fromString(direction),
                Constants.DEFAULT_SORT_PROPERTY
        );
    }
}
