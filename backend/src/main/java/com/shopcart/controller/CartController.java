package com.shopcart.controller;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.service.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;

    @PostMapping("/{userId}/items")
    public CartItemResponse addToCart(@PathVariable String userId, @Valid @RequestBody CartItemRequest request) {
        return cartService.addToCart(request, userId);
    }

    @PostMapping("/add")
    public CartItemResponse addToCartByBody(@Valid @RequestBody CartItemRequest request) {
        return cartService.addToCart(request, request.getUserId());
    }

    @PutMapping("/{userId}/items")
    public CartItemResponse updateQuantity(@PathVariable String userId, @Valid @RequestBody CartItemRequest request) {
        return cartService.updateQuantity(request, userId);
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public void removeFromCart(@PathVariable String userId, @PathVariable String productId) {
        cartService.removeFromCart(userId, productId);
    }

    @GetMapping("/{userId}/items")
    public List<CartItemResponse> getCartItems(@PathVariable String userId) {
        return cartService.getCartItemsByUserId(userId);
    }
}
