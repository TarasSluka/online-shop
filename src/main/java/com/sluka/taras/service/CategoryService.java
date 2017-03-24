package com.sluka.taras.service;


import com.sluka.taras.common.dto.CategoryDto;
import com.sluka.taras.common.model.Category;

import java.util.List;
import java.util.Stack;

public interface CategoryService {
    Stack<Long> getCategoryHieraclyById(long id, Stack<Long> set);

    void deleteFromParent(Long id);

    List<CategoryDto> getHierarchyCategory(Category category);

    Category save(Category category);

    Category update(Category category);

    void delete(Category category);

    List<Category> findAll();

    Category findById(Long id);

    long count();

    void deleteById(long id);

    Category getParentCategory();

    boolean exists(Category category);

    boolean isRootCategory(Category category);

    List<CategoryDto> categotyToDto(List<Category> list);

    List<Category> getAllChildCategoryById(Long id);

    List<CategoryDto> getAllLoverChildCategory();
}
