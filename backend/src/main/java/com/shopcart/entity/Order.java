package com.shopcart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    private String orderId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "coupon_code", referencedColumnName = "couponCode")
    private Coupon coupon;

    @Column(nullable = false)
    private Long subtotalPrice;

    @Column(nullable = false)
    private Long shippingFee;

    @Column(nullable = false)
    private Long totalPrice;

    private String shippingAddress;

    private String paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
}
