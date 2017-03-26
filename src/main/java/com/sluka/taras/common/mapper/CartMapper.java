package com.sluka.taras.common.mapper;

import com.sluka.taras.common.dto.CartDto;
import com.sluka.taras.common.model.Cart;
import com.sluka.taras.service.ProductService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartMapper {
    private final Logger logger = LogManager.getLogger(CartMapper.class);
    @Autowired
    ProductMapper productMapper;
    @Lazy
    @Autowired
    ProductService productService;

    public CartDto toDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setProductId(cart.getProduct().getId());
        cartDto.setProductDto(productMapper.toDto(cart.getProduct()));
        cartDto.setQuantity(cart.getQuantity());
        cartDto.setCreatedDate(cart.getCreatedDate());
        cartDto.setModifiedDate(cart.getModifiedDate());
        return cartDto;
    }

    public List<CartDto> toDto(List<Cart> cartList) {
        List<CartDto> result = new ArrayList<>();
        if (cartList != null || cartList.size() != 0)
            cartList.forEach(item -> {
                result.add(toDto(item));
            });
        return result;
    }

    public List<Cart> toEntity(List<CartDto> cartDtoList) {
        List<Cart> result = new ArrayList<>();
        if (cartDtoList != null || cartDtoList.size() != 0)
            cartDtoList.forEach(item -> {
                result.add(toEntity(item));
            });
        return result;
    }

    public Cart toEntity(CartDto cartDto) {
        Cart cart = new Cart();
        cart.setQuantity(cartDto.getQuantity());
        if (cartDto.getProductId() != null) {
            cart.setProduct(productService.findById(cartDto.getProductId()));
        } else {
            cart.setProduct(productMapper.toEntity(cartDto.getProductDto()));
        }
        return cart;
    }


}
