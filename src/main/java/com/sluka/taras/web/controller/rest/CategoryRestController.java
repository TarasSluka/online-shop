package com.sluka.taras.web.controller.rest;

import com.sluka.taras.service.CategoryService;
import com.sluka.taras.common.dto.CategoryDto;
import com.sluka.taras.common.mapper.CategoryMapper;
import com.sluka.taras.common.model.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/category")
public class CategoryRestController {
    private final Logger Logger = LogManager.getLogger(CategoryRestController.class);
    private CategoryService categoryService;
    private CategoryMapper categoryMapper;

    @Autowired
    CategoryRestController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDto>> getCategory() {
        List<Category> list = categoryService.findAll();
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        list.forEach(item -> categoryDtoList.add(categoryMapper.toDto(item)));
        if (categoryDtoList.isEmpty())
            return new ResponseEntity<List<CategoryDto>>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<List<CategoryDto>>(categoryDtoList, HttpStatus.OK);
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}/hierarchy", method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDto>> getHierarchyCategory(@PathVariable Long id) {
        Category category = categoryService.findById((long) id);
        if (category == null)
            new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        List<CategoryDto> list = categoryService.getHierarchyCategory(category);
        return new ResponseEntity<List<CategoryDto>>(list, HttpStatus.OK);
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category == null)
            new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        CategoryDto categoryDto = categoryMapper.toDto(category);
        return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);
    }

    //    /* GET */ @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/parentCategory", method = RequestMethod.GET)
    public ResponseEntity<CategoryDto> getRootChildCategory() {
        Category category = categoryService.getParentCategory();
        if (category == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        CategoryDto categoryDto = categoryMapper.toDto(category);
//        Logger.info(categoryDto.toString());
        return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/allLowerLevelCategory", method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDto>> getAllLoverChildCategory() {
        List<CategoryDto> categoryDtos = categoryService.getAllLoverChildCategory();
        if (categoryDtos == null || categoryDtos.size() == 0)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        category = categoryService.save(category);
        categoryDto = categoryMapper.toDto(category);
        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Category> updateCategoty(@PathVariable("id") Long id, @RequestBody CategoryDto categoryDto) {
        Category carentCategory = categoryService.findById(id);
        if (carentCategory == null)
            return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        categoryService.delete(carentCategory);
        carentCategory = categoryMapper.toEntity(categoryDto);
        categoryService.update(carentCategory);
        return new ResponseEntity<Category>(carentCategory, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Category> deleteCategory(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        if (category == null)
            return new ResponseEntity<Category>(HttpStatus.NOT_FOUND);
        categoryService.deleteById(category.getId());
        return new ResponseEntity<Category>(HttpStatus.NO_CONTENT);
    }
}
