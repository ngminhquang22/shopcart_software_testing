package com.shopcart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    private String inventoryId;

    @OneToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;
}
