package com.sluka.taras.service.serviceImpl;

import com.sluka.taras.service.CartService;
import com.sluka.taras.service.ProductService;
import com.sluka.taras.service.UserService;
import com.sluka.taras.common.dto.CartDto;
import com.sluka.taras.common.mapper.CartMapper;
import com.sluka.taras.common.model.Cart;
import com.sluka.taras.common.model.User;
import com.sluka.taras.repository.CartRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserService userService;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductService productService;

    @Override
    public List<CartDto> synchronizeCart(List<CartDto> cartDtoListFromUI, User user) {
        logger.info("................................synchronizeCart ");
        List<Cart> userCart = user.getCardList();
        for (int i = 0; i < cartDtoListFromUI.size(); i++) {
            Cart cart = IsExistProductInCartUser(userCart, cartDtoListFromUI.get(i));

            if (cart != null) {
                if (cart.getQuantity() < cartDtoListFromUI.get(i).getQuantity()) {
                    cart.setQuantity(cartDtoListFromUI.get(i).getQuantity());
                    cartRepository.save(cart);
                }
            } else {
                Cart newCart = new Cart();
                newCart.setQuantity(cartDtoListFromUI.get(i).getQuantity());
                newCart.setProduct(productService.findById(cartDtoListFromUI.get(i).getProductId()));
                newCart = save(newCart);
                user.getCardList().add(newCart);
                userService.save(user);
            }
        }
        return cartMapper.toDto(userService.findById(user.getId()).getCardList());
    }

    public Cart IsExistProductInCartUser(List<Cart> cartList, CartDto cart) {
        for (int i = 0; i < cartList.size(); i++) {
            Long a = cartList.get(i).getProduct().getId();
            Long b = cart.getProductId();
            if (a == b)
                return cartList.get(i);
        }
        return null;
    }

    @Override

    public Cart save(final Cart cart) {
        return cartRepository.save(cart);
    }

    @Override

    public Cart findById(Long id) {
        return cartRepository.getOne(id);
    }

    @Override
    public Cart save(CartDto cartDto, User user) {
        if (cartDto != null && user != null) {
            Cart cart = cartMapper.toEntity(cartDto);
            cart = cartRepository.save(cart);
            user.getCardList().add(cart);
            userService.save(user);
            return cart;
        } else
            return null;
    }

    @Override
    public CartDto update(CartDto cartDto, User user) {
        Cart cart = findByProductAndUser(cartDto.getProductId(), user.getId());
        cart.setQuantity(cartDto.getQuantity());
        cart = cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    @Override
    public Cart findByProductAndUser(Long productId, Long userId) {
        return cartRepository.findByProductAndUser(productId, userId);
    }

    @Override
    public void delete(User user) {
        List<Cart> cartList = user.getCardList();
        user = userService.save(user);
        for (int i = 0; i < cartList.size(); i++)
            cartRepository.delete(cartList.get(i).getId());
        user.getCardList().clear();
    }

    @Override
    public void deleteById(Long id) {
        Cart cart = cartRepository.getOne(id);
        cart.setProduct(null);
        cartRepository.delete(cart);
    }

    @Override
    public List<Cart> getAllCartByProductId(Long productId) {
        return cartRepository.getAllCartByProductId(productId);
    }

    @Override
    public void delete(Long productID, User user) {
        Cart cart = cartRepository.findByProductAndUser(productID, user.getId());
        List<Cart> cartList = user.getCardList();
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getId() == cart.getId()) {
                cartList.remove(i);
                break;
            }
        }
        user.setCardList(cartList);
        user = userService.save(user);
        cartRepository.delete(cart.getId());
    }

    @Override
    public List<CartDto> findAll(User user) {
        return cartMapper.toDto(user.getCardList());
    }
}
