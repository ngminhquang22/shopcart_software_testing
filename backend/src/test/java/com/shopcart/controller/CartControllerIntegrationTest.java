package com.shopcart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.service.ICartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class CartControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @MockitoBean
        private ICartService cartService;

        @Test
        void testAddToCart_Success() throws Exception {
                CartItemRequest request = new CartItemRequest("user-1", "product-1", 2);
                CartItemResponse response = CartItemResponse.builder()
                                .cartItemId("cart-item-1")
                                .userId("user-1")
                                .productId("product-1")
                                .quantity(2)
                                .build();

                when(cartService.addToCart(any(CartItemRequest.class), eq("user-1"))).thenReturn(response);

                mockMvc.perform(post("/api/cart/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(jsonPath("$.cartItemId").value("cart-item-1"))
                                .andExpect(jsonPath("$.userId").value("user-1"))
                                .andExpect(jsonPath("$.productId").value("product-1"))
                                .andExpect(jsonPath("$.quantity").value(2));
        }

        @Test
        void testAddToCart_CorsAndHeaders() throws Exception {
                CartItemRequest request = new CartItemRequest("user-1", "product-1", 1);
                CartItemResponse response = CartItemResponse.builder()
                                .cartItemId("cart-item-2")
                                .userId("user-1")
                                .productId("product-1")
                                .quantity(1)
                                .build();

                when(cartService.addToCart(any(CartItemRequest.class), eq("user-1"))).thenReturn(response);

                mockMvc.perform(post("/api/cart/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.ORIGIN, "http://localhost:5173")
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
        }
}
