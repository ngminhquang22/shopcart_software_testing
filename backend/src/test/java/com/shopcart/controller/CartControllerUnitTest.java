package com.shopcart.controller;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.service.ICartService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartControllerUnitTest {

    @Test
    void testAllCartControllerMethods() {
        ICartService cartService = mock(ICartService.class);
        CartController controller = new CartController(cartService);

        CartItemRequest request = new CartItemRequest("u1", "p1", 2);
        CartItemResponse response = CartItemResponse.builder()
                .cartItemId("c1")
                .userId("u1")
                .productId("p1")
                .quantity(2)
                .build();

        when(cartService.addToCart(request, "u1")).thenReturn(response);
        when(cartService.updateQuantity(request, "u1")).thenReturn(response);
        when(cartService.getCartItemsByUserId("u1")).thenReturn(List.of(response));

        assertEquals(response, controller.addToCart("u1", request));
        assertEquals(response, controller.addToCartByBody(request));
        assertEquals(response, controller.updateQuantity("u1", request));

        controller.removeFromCart("u1", "p1");
        verify(cartService).removeFromCart("u1", "p1");

        List<CartItemResponse> items = controller.getCartItems("u1");
        assertEquals(1, items.size());
        assertEquals("c1", items.get(0).getCartItemId());

        verify(cartService, times(2)).addToCart(request, "u1");
        verify(cartService).updateQuantity(request, "u1");
        verify(cartService).getCartItemsByUserId("u1");
    }
}
