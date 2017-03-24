package com.sluka.taras.web.model;

import lombok.Data;

@Data
public class ProductFilterRequest {
    Integer page;
    Integer size;
    String sortParam;
    String sortType;
    String search;
    Long minPrice;
    Long maxPrice;
    Long categoryId;
}
