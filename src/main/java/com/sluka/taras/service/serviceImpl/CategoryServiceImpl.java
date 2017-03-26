package com.sluka.taras.service.serviceImpl;


import com.sluka.taras.service.CategoryService;
import com.sluka.taras.service.ProductService;
import com.sluka.taras.common.dto.CategoryDto;
import com.sluka.taras.common.mapper.CategoryMapper;
import com.sluka.taras.common.model.Category;
import com.sluka.taras.common.model.Description;
import com.sluka.taras.repository.CategoryRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final Logger logger = LogManager.getLogger(CategoryServiceImpl.class);

    CategoryRepository categoryRepository;

    CategoryMapper categoryMapper;

    ProductService productService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, @Lazy ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productService = productService;
    }

    public final Category save(Category category) {
        findById(category.getParent().getId()).addToChildList(category);
        return categoryRepository.save(category);
    }

    @Override
    public List<CategoryDto> getAllLoverChildCategory() {
        return categotyToDto(categoryRepository.findAllLoverChildCategory());
    }

    public final Category update(Category category) {
        return em.merge(category);
    }

    public final void delete(Category category) {
        category = findById(category.getId());
        categoryRepository.delete(category);
    }

    public final List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public final Category findById(Long id) {
        return categoryRepository.findOne(id);
    }

    public final long count() {
        return categoryRepository.count();
    }

    public final void deleteById(final long id) {
        if (categoryRepository.findOne(id).getChildren().size() != 0) {
            try {
                Stack<Long> longs = getCategoryHieraclyById(id, new Stack<Long>());
                while (true) {
                    long l = longs.pop();
                    productService.deleteCategoryFromProduct(findById(l));
                    deleteFromParent(l);
                    categoryRepository.delete(l);
                }
            } catch (EmptyStackException e) {
                System.out.println("empty stack");
            }
        }
        productService.deleteCategoryFromProduct(findById(id));
        deleteFromParent(id);
        categoryRepository.delete(id);
    }

    @Override
    public Stack<Long> getCategoryHieraclyById(long id, Stack<Long> set) {
        if (categoryRepository.findOne(id).getChildren().size() != 0) {
            Category category = categoryRepository.findOne(id);
            for (Category cat : category.getChildren()) {
                set.push(cat.getId());
                getCategoryHieraclyById(cat.getId(), set);
            }
        }
        return set;
    }

    public void deleteFromParent(Long id) {
        logger.info("deleteFromParent + " + id);
        Category parent = categoryRepository.findOne(categoryRepository.findOne(id).getParent().getId());
        parent.getChildren().removeIf(item -> item.getId() == id);
        categoryRepository.save(parent);
    }

    public final void deleteChildrenAll(Long id) {
        logger.info("deleteChildrenAll + " + id);
        Category category = categoryRepository.findOne(id);
        if (category.getChildren().size() != 0)
            category.getChildren().removeAll(category.getChildren());
        categoryRepository.save(category);
    }

    public final Description findByType(String types) {
        return null;
    }


    public List<Category> findByParent(Category category) {
        return categoryRepository.findByParent(category);
    }


    public final Category getParentCategory() {
        return categoryRepository.findByName("Category");
    }

    public boolean exists(Category category) {
        return categoryRepository.exists(category.getId());
    }

    public List<CategoryDto> getHierarchyCategory(Category category) {
        List<Category> list = new ArrayList<>();
        if (isRootCategory(category))
            list.add(category);
        else {
            while (!isRootCategory(category)) {
                list.add(category);
                category = findById(category.getParent().getId());
            }
            list.add(category);
        }
        Collections.reverse(list);
        return categotyToDto(list);
    }

    @Override
    public List<CategoryDto> categotyToDto(List<Category> list) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        list.forEach(item -> categoryDtos.add(categoryMapper.toDto(item)));
        return categoryDtos;

    }

    public List<Category> getAllChildCategoryById(Long id) {
        Category category = findById(id);
        List<Category> list = new ArrayList<>();
        if (category.getChildren().size() == 0) {
            list.add(category);
            return list;
        }
        Stack<Long> longs = getCategoryHieraclyById(id, new Stack<Long>());
        longs.forEach(idL -> {
            if (findById(idL).getChildren().size() == 0)
                list.add(findById(idL));
        });
        return list;
    }

    public boolean isRootCategory(Category category) {
        if (category.getParent() == null)
            return true;
        return false;
    }
}

