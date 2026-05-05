package com.shopcart.service;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.mapper.CartItemMapper;
import com.shopcart.repository.CartItemRepository;
import com.shopcart.repository.ProductRepository;
import com.shopcart.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;

    public CartService(CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository,
                       CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartItemMapper = cartItemMapper;
    }

    public CartItemResponse addCartItem(CartItemRequest request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public CartItemResponse updateQuantity(String cartItemId, int quantity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
