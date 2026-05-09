package com.shopcart.service;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.entity.CartItem;
import com.shopcart.entity.Inventory;
import com.shopcart.entity.Product;
import com.shopcart.entity.User;
import com.shopcart.exception.OutOfStockException;
import com.shopcart.exception.ResourceNotFoundException;
import com.shopcart.mapper.CartItemMapper;
import com.shopcart.repository.CartItemRepository;
import com.shopcart.repository.InventoryRepository;
import com.shopcart.repository.ProductRepository;
import com.shopcart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemResponse addToCart(CartItemRequest request, String userId) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        Inventory inventory = inventoryRepository.findByProductProductId(product.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for product id: " + product.getProductId()));

        if (request.getQuantity() > inventory.getQuantity()) {
            throw new OutOfStockException("Requested quantity exceeds available stock");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        CartItem cartItem = cartItemRepository.findByUserUserIdAndProductProductId(userId, product.getProductId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + request.getQuantity());
                    return existing;
                })
                .orElseGet(() -> CartItem.builder()
                        .cartItemId(UUID.randomUUID().toString())
                        .user(user)
                        .product(product)
                        .quantity(request.getQuantity())
                        .build());

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toResponse(savedCartItem);
    }

    @Override
    public CartItemResponse updateQuantity(CartItemRequest request, String userId) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        Inventory inventory = inventoryRepository.findByProductProductId(product.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for product id: " + product.getProductId()));

        if (request.getQuantity() > inventory.getQuantity()) {
            throw new OutOfStockException("Requested quantity exceeds available stock");
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        CartItem cartItem = cartItemRepository.findByUserUserIdAndProductProductId(userId, product.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart item not found for user id: " + userId + " and product id: " + product.getProductId()));

        cartItem.setQuantity(request.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toResponse(savedCartItem);
    }

    @Override
    public void removeFromCart(String userId, String productId) {
        CartItem cartItem = cartItemRepository.findByUserUserIdAndProductProductId(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart item not found for user id: " + userId + " and product id: " + productId));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public List<CartItemResponse> getCartItemsByUserId(String userId) {
        return cartItemRepository.findByUserUserId(userId)
                .stream()
                .map(cartItemMapper::toResponse)
                .toList();
    }
}
