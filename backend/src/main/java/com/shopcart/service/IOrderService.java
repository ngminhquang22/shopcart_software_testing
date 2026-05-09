package com.shopcart.service;

import com.shopcart.dto.OrderRequest;
import com.shopcart.dto.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderRequest request, String userId);

    OrderResponse updateOrderStatus(String orderId, String status);

    void cancelOrder(String orderId);

    List<OrderResponse> getOrdersByUserId(String userId);

    OrderResponse getOrderById(String orderId);
}
