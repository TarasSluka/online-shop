package com.sluka.taras.repository;

import com.sluka.taras.common.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    List<Category> findByParent(Category category);

    @Query(value = "SELECT * from category c left join category_children cc on c.id = cc.category_id  where cc.children_id is null", nativeQuery = true)
    List<Category> findAllLoverChildCategory();

}
