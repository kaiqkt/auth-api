package com.kaiqkt.auth.application.web.dto.response;

import com.kaiqkt.auth.generated.application.dto.PageResponseV1;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class PageResponse {
    public static <T> PageResponseV1 toResponse(Page<T> page, Function<T, ?> mapper) {
        var responseV1 = new PageResponseV1();
        responseV1.setTotalElements((int) page.getTotalElements());
        responseV1.setTotalPages(page.getTotalPages());
        responseV1.setCurrentPage(page.getNumber());
        List<Object> elements = page.getContent().stream().map(mapper).map(Object.class::cast).toList();
        responseV1.setElements(elements);
        return responseV1;
    }
}
