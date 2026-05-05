package com.shopcart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String userId;
    private List<CartItemRequest> items;
    private String couponCode;
    private String shippingAddress;
    private String paymentMethod;
}
