package com.shopcart.service;

import com.shopcart.dto.OrderRequest;
import com.shopcart.dto.OrderResponse;
import com.shopcart.mapper.OrderMapper;
import com.shopcart.repository.CouponRepository;
import com.shopcart.repository.OrderItemRepository;
import com.shopcart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CouponRepository couponRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        CouponRepository couponRepository,
                        OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.couponRepository = couponRepository;
        this.orderMapper = orderMapper;
    }

    public OrderResponse createOrder(OrderRequest request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public BigDecimal calculateTotal(String orderId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
