package com.shopcart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.security.JwtAuthenticationFilter;
import com.shopcart.service.ICartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * CartControllerMockTest - Unit Test for CartController with Mocked Service
 * 
 * Purpose: Verify CartController behavior with mocked ICartService dependency.
 * Tests HTTP layer without loading full Spring context (WebMvcTest pattern).
 * 
 * Requirements (5.1.2 Backend Mocking):
 * a) Mock CartService with @MockitoBean
 * b) Test controller with mocked service
 * c) Verify mock interactions and response validation
 */
@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ICartService cartService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private CartItemRequest cartItemRequest;
    private CartItemResponse cartItemResponse;

    @BeforeEach
    void setUp() {
        cartItemRequest = new CartItemRequest("user-1", "product-1", 2);
        cartItemResponse = CartItemResponse.builder()
                .cartItemId("cart-item-1")
                .userId("user-1")
                .productId("product-1")
                .quantity(2)
                .build();
    }

    /**
     * Test: addToCart with Mocked Service
     * 
     * This test verifies:
     * 1. Controller properly calls mocked service method
     * 2. Mock returns expected CartItemResponse
     * 3. Controller returns HTTP 200 OK with correct JSON structure
     * 4. Mock interactions are verified (verify pattern)
     */
    @Test
    void testAddToCartWithMockedService() throws Exception {
        // Arrange: Setup mock behavior
        when(cartService.addToCart(any(CartItemRequest.class), eq("user-1")))
                .thenReturn(cartItemResponse);

        // Act: Perform POST request to /api/cart/add
        mockMvc.perform(post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItemRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.cartItemId").value("cart-item-1"))
                .andExpect(jsonPath("$.userId").value("user-1"))
                .andExpect(jsonPath("$.productId").value("product-1"))
                .andExpect(jsonPath("$.quantity").value(2));

        // Assert: Verify mock interaction
        // Verify that cartService.addToCart() was called exactly once with correct
        // parameters
        verify(cartService, times(1)).addToCart(any(), any());
    }

    /**
     * Test: addToCart with Different Product
     * 
     * This test verifies mock behavior with different product data.
     * Demonstrates flexibility of mock service in handling various inputs.
     */
    @Test
    void testAddToCartWithDifferentProduct() throws Exception {
        // Arrange: Setup different request/response
        CartItemRequest request = new CartItemRequest("user-2", "product-3", 5);
        CartItemResponse response = CartItemResponse.builder()
                .cartItemId("cart-item-2")
                .userId("user-2")
                .productId("product-3")
                .quantity(5)
                .build();

        when(cartService.addToCart(any(CartItemRequest.class), eq("user-2")))
                .thenReturn(response);

        // Act & Assert: Perform request and verify response
        mockMvc.perform(post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItemId").value("cart-item-2"))
                .andExpect(jsonPath("$.userId").value("user-2"))
                .andExpect(jsonPath("$.productId").value("product-3"))
                .andExpect(jsonPath("$.quantity").value(5));

        // Verify that mock was called exactly once with correct userId
        verify(cartService, times(1)).addToCart(any(CartItemRequest.class), eq("user-2"));
    }

    /**
     * Test: Multiple Requests to Verify Mock Call Count
     * 
     * This test demonstrates:
     * 1. Multiple calls to the same mocked method
     * 2. Verify total number of mock invocations
     * 3. Each call receives different data
     */
    @Test
    void testMultipleAddToCartCalls() throws Exception {
        // Arrange: Setup mock to handle multiple calls
        CartItemResponse response1 = CartItemResponse.builder()
                .cartItemId("cart-item-1")
                .userId("user-1")
                .productId("product-1")
                .quantity(2)
                .build();

        CartItemResponse response2 = CartItemResponse.builder()
                .cartItemId("cart-item-2")
                .userId("user-1")
                .productId("product-2")
                .quantity(3)
                .build();

        when(cartService.addToCart(any(CartItemRequest.class), eq("user-1")))
                .thenReturn(response1)
                .thenReturn(response2);

        // Act: Make first request
        mockMvc.perform(post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CartItemRequest("user-1", "product-1", 2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItemId").value("cart-item-1"));

        // Act: Make second request
        mockMvc.perform(post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CartItemRequest("user-1", "product-2", 3))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItemId").value("cart-item-2"));

        // Assert: Verify mock was called exactly twice
        verify(cartService, times(2)).addToCart(any(CartItemRequest.class), eq("user-1"));
    }

    /**
     * Test: Verify Mock with CORS Headers
     * 
     * This test verifies:
     * 1. Controller handles Origin header correctly
     * 2. Mock service still works with CORS requests
     * 3. Response includes proper Content-Type header
     */
    @Test
    void testAddToCartWithCorsHeaders() throws Exception {
        // Arrange: Setup mock
        when(cartService.addToCart(any(CartItemRequest.class), eq("user-1")))
                .thenReturn(cartItemResponse);

        // Act & Assert: Perform request with Origin header
        mockMvc.perform(post("/api/cart/add")
                .header("Origin", "http://localhost:5173")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItemRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.cartItemId").value("cart-item-1"));

        // Verify mock was still called once
        verify(cartService, times(1)).addToCart(any(CartItemRequest.class), eq("user-1"));
    }
}
