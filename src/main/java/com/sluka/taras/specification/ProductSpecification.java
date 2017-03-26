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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductSpecification implements Specification<Product> {
    private final Logger logger = LogManager.getLogger(ProductSpecification.class);
    CategoryService categoryService;
    private ProductFilterRequest filterRequest;

    @Autowired
    public ProductSpecification(CategoryService categoryService) {
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
        if (filterRequest.getMinPrice() != null && filterRequest.getMaxPrice() != null) {
            predicateList.add(cb.between(root.get("price").as(Long.class), filterRequest.getMinPrice(), filterRequest.getMaxPrice()));
        } else if (filterRequest.getMinPrice() != null) {
            predicateList.add(cb.greaterThanOrEqualTo(root.get("price").as(Long.class), filterRequest.getMinPrice()));
        } else if (filterRequest.getMaxPrice() != null) {
            predicateList.add(cb.lessThanOrEqualTo(root.get("price").as(Long.class), filterRequest.getMaxPrice()));
        }
        List<Predicate> predicateListCategory = new ArrayList<>();
        if (filterRequest.getCategoryId() != null) {
            List<Category> list = categoryService.getAllChildCategoryById(filterRequest.getCategoryId());
            list.forEach(cat -> {
                predicateListCategory.add(cb.equal(root.get("category"), cat));
            });
            predicateList.add(cb.or(predicateListCategory.toArray(new Predicate[]{})));
        } else if (filterRequest.getSortParam() != null && filterRequest.getSortParam().equals("category")) {
            predicateList.add(cb.isNull(root.get("category")));
        }
        predicate = cb.and(predicateList.toArray(new Predicate[]{}));
        return predicate;
    }

    private String toLike(String s) {
        return "%" + s + "%";
    }
}
