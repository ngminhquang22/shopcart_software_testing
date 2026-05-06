package com.shopcart.repository;

import com.shopcart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
    List<CartItem> findByUserUserId(String userId);

    Optional<CartItem> findByUserUserIdAndProductProductId(String userId, String productId);
}
