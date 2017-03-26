package com.sluka.taras.web.controller.rest;

import com.sluka.taras.service.CartService;
import com.sluka.taras.service.UserService;
import com.sluka.taras.common.dto.CartDto;
import com.sluka.taras.common.model.Cart;
import com.sluka.taras.common.model.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartRestController {

    private final Logger logger = LogManager.getLogger(CartRestController.class);
    private CartService cartService;
    private UserService userService;

    @Autowired
    public CartRestController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CartDto>> getCart() {
        User user = userService.getCurrentUser();
        List<CartDto> cartDtoList = cartService.findAll(user);
        if (cartDtoList == null || cartDtoList.size() == 0)
            return new ResponseEntity<>(cartDtoList, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(cartDtoList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/synchronizeCartWithServer", method = RequestMethod.PUT)
    public ResponseEntity<List<CartDto>> synchronizeCartWithServer(@RequestBody List<CartDto> cartDtoList) {
        logger.info("............synchronizeCartWithServer");
        User user = userService.getCurrentUser();
        List<CartDto> newCartDtoList = new ArrayList<>();
        logger.info("cartDtoList" + cartDtoList);
        if (user.getCardList().size() == 0 && cartDtoList.size() == 0)
            return new ResponseEntity<>(newCartDtoList, HttpStatus.NO_CONTENT);
        else {
            newCartDtoList = cartService.synchronizeCart(cartDtoList, user);
            return new ResponseEntity<>(newCartDtoList, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CartDto> addProduct(@RequestBody CartDto cartDto) {
        User user = userService.getCurrentUser();
        Cart cart = cartService.save(cartDto, user);
        if (cartDto != null)
            return new ResponseEntity<>(cartDto, HttpStatus.OK);
        else
            return new ResponseEntity<>(cartDto, HttpStatus.CONFLICT);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> clearCart() {
        User user = userService.getCurrentUser();
        cartService.delete(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{productID}", method = RequestMethod.DELETE)
    public ResponseEntity<List<CartDto>> deleteProductFromCart(@PathVariable("productID") Long productID) {
        User user = userService.getCurrentUser();
        cartService.delete(productID, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<CartDto> updeteCartItem(@RequestBody CartDto cartDto) {
        User user = userService.getCurrentUser();
        CartDto cart = cartService.update(cartDto, user);
        if (cart == null)
            return new ResponseEntity<>(cart, HttpStatus.CONFLICT);
        else
            return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}

