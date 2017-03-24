package com.sluka.taras.service;

import org.springframework.data.domain.Pageable;

public interface PageableService {

    Pageable getPageable(Integer page, Integer size, String sortType, String sortParam);
}
