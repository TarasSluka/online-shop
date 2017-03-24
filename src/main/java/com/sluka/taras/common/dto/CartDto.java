package com.sluka.taras.common.dto;


import lombok.Data;

@Data
public class CartDto extends BaseDto {
    private Long productId;
    private ProductDto productDto;
    private Integer quantity;
}
