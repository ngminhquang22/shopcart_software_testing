package com.shopcart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    private String couponCode;

    @Column(nullable = false)
    private String discountType;

    @Column(nullable = false)
    private Long discountValue;

    @Column(nullable = false)
    private String status;

    private LocalDateTime validUntil;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;
}
