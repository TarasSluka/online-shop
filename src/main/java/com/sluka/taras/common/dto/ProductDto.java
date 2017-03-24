package com.sluka.taras.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto extends BaseDto {
    String avaName;
    List<DescriptionDto> descriptionList;
    private Long id;
    private Long id_category;
    private String name;
    private Long price;


}
