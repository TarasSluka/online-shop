package com.sluka.taras.common.mapper;


import com.sluka.taras.service.CategoryService;
import com.sluka.taras.common.dto.CategoryDto;
import com.sluka.taras.common.model.Category;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper {
    private final Logger logger = LogManager.getLogger(CategoryMapper.class);
    CategoryService categoryService;

    @Autowired
    public CategoryMapper(@Lazy CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public List<CategoryDto> toDto(List<Category> categoryList) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        categoryList.forEach(item -> {
            categoryDtos.add(toDto(item));
        });
        return categoryDtos;
    }

    public CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setCreatedDate(category.getCreatedDate());
        try {
            if (category.getParent().getId() != null)
                categoryDto.setParent(category.getParent().getId());
        } catch (NullPointerException e) {
            categoryDto.setParent(null);
        }
        categoryDto.setDescription(category.getDescription());
        categoryDto.setChildren(category.getChildren());
        categoryDto.setChildCategory(getIdCategory(category.getChildren()));
        return categoryDto;
    }

    public Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        if (categoryDto.getParent() != null)
            category.setParent(categoryService.findById(categoryDto.getParent()));
        category.setChildren(categoryDto.getChildren());
        return category;
    }

    public List<Long> getIdCategory(List<Category> categoryList) {
        List<Long> longList = new ArrayList<>();
        categoryList.forEach(item -> {
            longList.add(item.getId());
        });
        return longList;
    }
}
