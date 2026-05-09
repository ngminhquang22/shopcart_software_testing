package com.shopcart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.dto.OrderRequest;
import com.shopcart.dto.OrderResponse;
import com.shopcart.service.IOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private IOrderService orderService;

    @Test
    void testCreateOrder_Success() throws Exception {
        CartItemRequest item1 = new CartItemRequest("user-1", "product-1", 2);
        CartItemRequest item2 = new CartItemRequest("user-1", "product-2", 1);

        OrderRequest request = new OrderRequest(
                "user-1",
                List.of(item1, item2),
                "SAVE10",
                50_000L,
                "123 Test Street",
                "COD");

        CartItemResponse cartItem1 = CartItemResponse.builder()
                .cartItemId("cart-item-1")
                .userId("user-1")
                .productId("product-1")
                .quantity(2)
                .build();

        CartItemResponse cartItem2 = CartItemResponse.builder()
                .cartItemId("cart-item-2")
                .userId("user-1")
                .productId("product-2")
                .quantity(1)
                .build();

        OrderResponse response = OrderResponse.builder()
                .orderId("ORD-001")
                .userId("user-1")
                .items(List.of(cartItem1, cartItem2))
                .subtotalPrice(30_500_000L)
                .shippingFee(50_000L)
                .totalPrice(27_500_000L)
                .status("PENDING")
                .build();

        when(orderService.createOrder(any(OrderRequest.class), eq("user-1"))).thenReturn(response);

        mockMvc.perform(post("/api/orders/user-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.orderId").value("ORD-001"))
                .andExpect(jsonPath("$.userId").value("user-1"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.subtotalPrice").value(30_500_000L))
                .andExpect(jsonPath("$.shippingFee").value(50_000L))
                .andExpect(jsonPath("$.totalPrice").value(27_500_000L));
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        CartItemResponse cartItem1 = CartItemResponse.builder()
                .cartItemId("cart-item-1")
                .userId("user-1")
                .productId("product-1")
                .quantity(2)
                .build();

        OrderResponse response = OrderResponse.builder()
                .orderId("ORD-001")
                .userId("user-1")
                .items(List.of(cartItem1))
                .subtotalPrice(30_000_000L)
                .shippingFee(50_000L)
                .totalPrice(30_050_000L)
                .status("PENDING")
                .build();

        when(orderService.getOrderById("ORD-001")).thenReturn(response);

        mockMvc.perform(get("/api/orders/detail/ORD-001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.orderId").value("ORD-001"))
                .andExpect(jsonPath("$.userId").value("user-1"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalPrice").value(30_050_000L));
    }

    @Test
    void testCancelOrder_Success() throws Exception {
        doNothing().when(orderService).cancelOrder("ORD-001");

        mockMvc.perform(delete("/api/orders/ORD-001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
