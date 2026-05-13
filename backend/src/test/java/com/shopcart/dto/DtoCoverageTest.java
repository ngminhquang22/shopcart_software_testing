package com.shopcart.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DtoCoverageTest {

    @Test
    void testCartItemRequestCoverage() {
        CartItemRequest req = new CartItemRequest();
        req.setUserId("u1");
        req.setProductId("p1");
        req.setQuantity(2);

        assertEquals("u1", req.getUserId());
        assertEquals("p1", req.getProductId());
        assertEquals(2, req.getQuantity());
        assertNotNull(req.toString());

        CartItemRequest same = new CartItemRequest("u1", "p1", 2);
        assertEquals(req, same);
        assertEquals(req.hashCode(), same.hashCode());
        assertNotEquals(req, null);
        assertNotEquals(req, new Object());
        assertTrue(req.canEqual(same));
        assertFalse(req.canEqual("x"));

        CartItemRequest different = new CartItemRequest("u2", "p1", 2);
        assertNotEquals(req, different);

        CartItemRequest nullFieldVariant = new CartItemRequest(null, "p1", 2);
        assertNotEquals(req, nullFieldVariant);
    }

    @Test
    void testCartItemResponseCoverage() {
        CartItemResponse response = new CartItemResponse();
        response.setCartItemId("c1");
        response.setUserId("u1");
        response.setProductId("p1");
        response.setQuantity(2);

        assertEquals("c1", response.getCartItemId());
        assertEquals("u1", response.getUserId());
        assertEquals("p1", response.getProductId());
        assertEquals(2, response.getQuantity());
        assertNotNull(response.toString());

        CartItemResponse same = CartItemResponse.builder()
                .cartItemId("c1")
                .userId("u1")
                .productId("p1")
                .quantity(2)
                .build();

        assertEquals(response, same);
        assertEquals(response.hashCode(), same.hashCode());
        assertNotEquals(response, null);
        assertNotEquals(response, new Object());
        assertTrue(response.canEqual(same));
        assertFalse(response.canEqual("x"));
        assertNotNull(CartItemResponse.builder().cartItemId("b").userId("u").productId("p").quantity(1).toString());

        CartItemResponse allArgs = new CartItemResponse("c2", "u2", "p2", 3);
        assertEquals("c2", allArgs.getCartItemId());
    }

    @Test
    void testOrderRequestCoverage() {
        CartItemRequest item = new CartItemRequest("u1", "p1", 1);

        OrderRequest request = new OrderRequest();
        request.setUserId("u1");
        request.setItems(List.of(item));
        request.setCouponCode("SAVE10");
        request.setShippingFee(10L);
        request.setShippingAddress("addr");
        request.setPaymentMethod("COD");

        assertEquals("u1", request.getUserId());
        assertEquals(1, request.getItems().size());
        assertEquals("SAVE10", request.getCouponCode());
        assertEquals(10L, request.getShippingFee());
        assertEquals("addr", request.getShippingAddress());
        assertEquals("COD", request.getPaymentMethod());
        assertNotNull(request.toString());

        OrderRequest same = new OrderRequest("u1", List.of(item), "SAVE10", 10L, "addr", "COD");
        assertEquals(request, same);
        assertEquals(request.hashCode(), same.hashCode());
        assertNotEquals(request, null);
        assertNotEquals(request, new Object());
        assertTrue(request.canEqual(same));
        assertFalse(request.canEqual("x"));

        OrderRequest different = new OrderRequest("u2", List.of(item), "SAVE10", 10L, "addr", "COD");
        assertNotEquals(request, different);

        OrderRequest nullItemsVariant = new OrderRequest("u1", null, "SAVE10", 10L, "addr", "COD");
        assertNotEquals(request, nullItemsVariant);
    }

    @Test
    void testOrderResponseCoverage() {
        CartItemResponse item = CartItemResponse.builder()
                .cartItemId("c1")
                .userId("u1")
                .productId("p1")
                .quantity(2)
                .build();

        OrderResponse response = new OrderResponse();
        response.setOrderId("o1");
        response.setUserId("u1");
        response.setItems(List.of(item));
        response.setSubtotalPrice(100L);
        response.setShippingFee(10L);
        response.setTotalPrice(110L);
        response.setStatus("PENDING");

        assertEquals("o1", response.getOrderId());
        assertEquals("u1", response.getUserId());
        assertEquals(1, response.getItems().size());
        assertEquals(100L, response.getSubtotalPrice());
        assertEquals(10L, response.getShippingFee());
        assertEquals(110L, response.getTotalPrice());
        assertEquals("PENDING", response.getStatus());
        assertNotNull(response.toString());

        OrderResponse same = OrderResponse.builder()
                .orderId("o1")
                .userId("u1")
                .items(List.of(item))
                .subtotalPrice(100L)
                .shippingFee(10L)
                .totalPrice(110L)
                .status("PENDING")
                .build();

        assertEquals(response, same);
        assertEquals(response.hashCode(), same.hashCode());
        assertNotEquals(response, null);
        assertNotEquals(response, new Object());
        assertTrue(response.canEqual(same));
        assertFalse(response.canEqual("x"));
        assertNotNull(OrderResponse.builder()
                .orderId("b")
                .userId("u")
                .items(List.of(item))
                .subtotalPrice(1L)
                .shippingFee(1L)
                .totalPrice(2L)
                .status("PENDING")
                .toString());

        OrderResponse allArgs = new OrderResponse("o2", "u2", List.of(item), 1L, 2L, 3L, "DONE");
        assertEquals("o2", allArgs.getOrderId());
    }
}
