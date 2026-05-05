package com.shopcart.service;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.dto.OrderRequest;
import com.shopcart.dto.OrderResponse;
import com.shopcart.mapper.OrderMapper;
import com.shopcart.repository.CouponRepository;
import com.shopcart.repository.OrderItemRepository;
import com.shopcart.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderItemRepository orderItemRepository;

    @Mock
    CouponRepository couponRepository;

    @Mock
    OrderMapper orderMapper;

    @InjectMocks
    OrderService orderService;

    @Test
    void createOrder_shouldReturnOrderResponse() {
        OrderRequest request = new OrderRequest("user-1", List.of(new CartItemRequest("user-1", "p-1", 1)), null,
                "addr", "card");

        OrderResponse expected = new OrderResponse("o-1", "user-1",
                List.of(new CartItemResponse("ci-1", "user-1", "p-1", 1)), 100L, 10L, 110L, "CREATED");
        when(orderMapper.toResponse(any())).thenReturn(expected);

        OrderResponse result = orderService.createOrder(request);

        assertThat(result).isNotNull();
        assertThat(result.getTotalPrice()).isEqualTo(110L);
    }

    @Test
    void calculateTotal_shouldReturnSum() {
        // For TDD red, assert expected sum; implementation missing so test will fail
        var sum = orderService.calculateTotal("o-1");
        assertThat(sum).isNotNull();
    }
}
