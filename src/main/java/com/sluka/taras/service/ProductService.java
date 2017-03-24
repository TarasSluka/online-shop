package com.sluka.taras.service;

import com.sluka.taras.common.dto.ProductDto;
import com.sluka.taras.common.model.Category;
import com.sluka.taras.common.model.Product;
import com.sluka.taras.web.model.ProductFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductService {
    void deleteCategoryFromProduct(Category category);

    ProductDto save(ProductDto product);

    Product save(Product product);

    Product update(Product product);

    ProductDto update(ProductDto product);

    void delete(Product product);

    void deleteById(Long id);

    List<Product> findByCategory(Category category);

    List<Product> findAll();

    Product findById(Long id);

    long count();

    boolean exists(Long id);

    Map<String, Long> getMaxAndMinPrice(ProductFilterRequest productFilterRequest);

    Page<ProductDto> filter(ProductFilterRequest productFilterRequest, Pageable pageable);
}
