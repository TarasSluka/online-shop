package com.sluka.taras.repository;

import com.sluka.taras.common.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT * FROM Cart c WHERE c.product_id = :productID AND c.user_id = :userId", nativeQuery = true)
    Cart findByProductAndUser(@Param("productID") Long productID, @Param("userId") Long userId);

    @Query(value = "SELECT * FROM Cart c WHERE c.product_id = :productID", nativeQuery = true)
    List<Cart> getAllCartByProductId(@Param("productID") Long productID);
}
