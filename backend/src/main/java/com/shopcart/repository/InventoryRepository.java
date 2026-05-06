package com.shopcart.repository;

import com.shopcart.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
	Optional<Inventory> findByProductProductId(String productId);
}
