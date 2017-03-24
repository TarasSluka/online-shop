package com.sluka.taras.service;

import com.sluka.taras.common.dto.CartDto;
import com.sluka.taras.common.model.Cart;
import com.sluka.taras.common.model.User;

import java.util.List;

public interface CartService {
    List<CartDto> synchronizeCart(List<CartDto> cartDtoListFromUI, User user);

    Cart save(final Cart cart);

    Cart findById(Long id);

    Cart findByProductAndUser(Long productId, Long userId);

    Cart save(CartDto cartDto, User user);

    CartDto update(CartDto cartDto, User user);

//    List<CartDto> add(List<CartDto> cartDtoList, User user);

    void delete(User user);

    void deleteById(Long id);

    List<Cart> getAllCartByProductId(Long productId);

    void delete(Long id, User user);

    List<CartDto> findAll(User user);

}
