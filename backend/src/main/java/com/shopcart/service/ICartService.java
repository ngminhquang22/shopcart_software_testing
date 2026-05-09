package com.shopcart.service;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;

import java.util.List;

public interface ICartService {
    CartItemResponse addToCart(CartItemRequest request, String userId);

    CartItemResponse updateQuantity(CartItemRequest request, String userId);

    void removeFromCart(String userId, String productId);

    List<CartItemResponse> getCartItemsByUserId(String userId);
}
