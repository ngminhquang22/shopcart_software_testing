package com.shopcart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    private String userId;
    private List<CartItemResponse> items;
    private Long subtotalPrice;
    private Long shippingFee;
    private Long totalPrice;
    private String status;
}
