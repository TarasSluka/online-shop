package com.sluka.taras.specification;

import com.sluka.taras.service.CategoryService;
import com.sluka.taras.common.model.Category;
import com.sluka.taras.common.model.Product;
import com.sluka.taras.web.model.ProductFilterRequest;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMaxPriceSpecification implements Specification<Product> {
    private final Logger logger = LogManager.getLogger(ProductSpecification.class);
    CategoryService categoryService;
    private ProductFilterRequest filterRequest;

    @Autowired
    public ProductMaxPriceSpecification(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void setFilter(ProductFilterRequest filterRequest) {
        this.filterRequest = filterRequest;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        assert filterRequest != null;
        Predicate predicate = null;
        List<Predicate> predicateList = new ArrayList<>();
        if (filterRequest.getSearch() != null) {
            String[] requestSearch = filterRequest.getSearch().split(" ");
            for (int i = 0; i < requestSearch.length; i++)
                predicateList.add(cb.and(cb.like(root.get("searchString"), toLike(requestSearch[i]))));
        }
        List<Predicate> predicateListCategory = new ArrayList<>();
        if (filterRequest.getCategoryId() != null) {
            List<Category> list = categoryService.getAllChildCategoryById(filterRequest.getCategoryId());
            list.forEach(cat -> {
                predicateListCategory.add(cb.equal(root.get("category"), cat));
            });
            predicateList.add(cb.or(predicateListCategory.toArray(new Predicate[]{})));
        }
        Subquery<Long> subquery = query.subquery(Long.class);
        subquery.select(cb.max(root.get("price")));

        predicate = cb.max(root.<Long>get("price")).in(cb.and(predicateList.toArray(new Predicate[]{})));
        return predicate;
    }

    private String toLike(String s) {
        return "%" + s + "%";
    }
}
