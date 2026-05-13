package com.shopcart.controller;

import com.shopcart.dto.OrderRequest;
import com.shopcart.dto.OrderResponse;
import com.shopcart.service.IOrderService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderControllerUnitTest {

    @Test
    void testAllOrderControllerMethods() {
        IOrderService orderService = mock(IOrderService.class);
        OrderController controller = new OrderController(orderService);

        OrderRequest request = new OrderRequest("u1", List.of(), null, 0L, "addr", "COD");
        OrderResponse response = OrderResponse.builder()
                .orderId("o1")
                .userId("u1")
                .subtotalPrice(100L)
                .shippingFee(10L)
                .totalPrice(110L)
                .status("PENDING")
                .build();

        when(orderService.createOrder(request, "u1")).thenReturn(response);
        when(orderService.updateOrderStatus("o1", "DONE")).thenReturn(response);
        when(orderService.getOrdersByUserId("u1")).thenReturn(List.of(response));
        when(orderService.getOrderById("o1")).thenReturn(response);

        assertEquals(response, controller.createOrder("u1", request));
        assertEquals(response, controller.updateOrderStatus("o1", "DONE"));

        controller.cancelOrder("o1");
        verify(orderService).cancelOrder("o1");

        List<OrderResponse> list = controller.getOrdersByUser("u1");
        assertEquals(1, list.size());
        assertEquals("o1", list.get(0).getOrderId());

        assertEquals(response, controller.getOrderById("o1"));

        verify(orderService).createOrder(request, "u1");
        verify(orderService).updateOrderStatus("o1", "DONE");
        verify(orderService).getOrdersByUserId("u1");
        verify(orderService).getOrderById("o1");
    }
}
