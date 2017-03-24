package com.sluka.taras.repository;

import com.sluka.taras.common.model.Category;
import com.sluka.taras.common.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor {

    List<Product> findByCategory(Category category);

}
