package com.sluka.taras.common.mapper;

import com.sluka.taras.service.CategoryService;
import com.sluka.taras.service.PhotoService;
import com.sluka.taras.common.dto.ProductDto;
import com.sluka.taras.common.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {
    private final CategoryService categoryService;
    private final PhotoService photoService;
    private final DescriptionMapper descriptionMapper;

    @Autowired
    public ProductMapper(CategoryService categoryService, PhotoService photoService, DescriptionMapper descriptionMapper) {
        this.categoryService = categoryService;
        this.photoService = photoService;
        this.descriptionMapper = descriptionMapper;
    }

    List<ProductDto> toDto(List<Product> productList) {
        List<ProductDto> productDtoList = new ArrayList<>();
        productList.forEach(item -> {
            productDtoList.add(toDto(item));
        });
        return productDtoList;
    }

    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        try {
            if (product.getCategory().getId() != null)
                productDto.setId_category(product.getCategory().getId());
        } catch (NullPointerException e) {
        }

        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setCreatedDate(product.getCreatedDate());
        productDto.setDescriptionList(descriptionMapper.toDto(product.getDescriptionsArrayList()));

        try {
            productDto.setAvaName(product.getAvaName());
        } catch (NullPointerException e) {
            productDto.setAvaName(null);
            e.printStackTrace();
        }
        return productDto;
    }

    public Product toEntity(ProductDto productDto) {
        Product product = new Product();
        product.setAvaName(productDto.getAvaName());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescriptionsArrayList(descriptionMapper.toEntity(productDto.getDescriptionList()));
        try {
            if (productDto.getId() != null)
                product.setId(productDto.getId());
            if (productDto.getId_category() != null)
                product.setCategory(categoryService.findById(productDto.getId_category()));
            if (productDto.getAvaName() != null)
                product.setAvaName(productDto.getAvaName());

        } catch (NullPointerException e) {
        }
        return product;
    }

    public Page<ProductDto> toDto(Pageable page, Page<Product> source) {
        List<ProductDto> dtos = toDto(source.getContent());
        return new PageImpl<>(dtos, page, source.getTotalElements());
    }
}
