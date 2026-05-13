package com.shopcart.mapper;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.dto.OrderRequest;
import com.shopcart.dto.OrderResponse;
import com.shopcart.entity.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapperImplTest {

    @Test
    void testCartItemMapperImplBranches() {
        CartItemMapperImpl mapper = new CartItemMapperImpl();

        assertNull(mapper.toResponse(null));
        assertNull(mapper.toEntity(null));

        CartItemRequest request = new CartItemRequest("u1", "p1", 3);
        CartItem entity = mapper.toEntity(request);
        assertNotNull(entity);
        assertEquals(3, entity.getQuantity());
        assertNull(entity.getUser());
        assertNull(entity.getProduct());

        CartItem fullEntity = CartItem.builder()
                .cartItemId("c1")
                .user(User.builder().userId("u1").username("john").build())
                .product(Product.builder().productId("p1").name("Laptop").price(100L).status("ACTIVE").build())
                .quantity(2)
                .build();

        CartItemResponse response = mapper.toResponse(fullEntity);
        assertEquals("c1", response.getCartItemId());
        assertEquals("u1", response.getUserId());
        assertEquals("p1", response.getProductId());
        assertEquals(2, response.getQuantity());

        CartItem partialEntity = CartItem.builder()
                .cartItemId("c2")
                .quantity(1)
                .build();
        CartItemResponse partial = mapper.toResponse(partialEntity);
        assertEquals("c2", partial.getCartItemId());
        assertNull(partial.getUserId());
        assertNull(partial.getProductId());
        assertEquals(1, partial.getQuantity());
    }

    @Test
    void testOrderMapperImplBranches() {
        OrderMapperImpl mapper = new OrderMapperImpl(new CartItemMapperImpl());

        assertNull(mapper.toResponse(null));
        assertNull(mapper.toEntity(null));

        OrderRequest request = new OrderRequest("u1", List.of(new CartItemRequest("u1", "p1", 1)), "SAVE10", 10L,
                "addr", "COD");
        Order entity = mapper.toEntity(request);
        assertNotNull(entity);
        assertNull(entity.getUser());
        assertNull(entity.getOrderItems());

        User user = User.builder().userId("u1").username("john").build();
        Product product = Product.builder().productId("p1").name("Laptop").price(100L).status("ACTIVE").build();
        OrderItem item = OrderItem.builder().orderItemId("oi1").product(product).quantity(2).unitPrice(50L).build();

        Order orderWithItems = Order.builder()
                .orderId("o1")
                .user(user)
                .status("PENDING")
                .subtotalPrice(100L)
                .shippingFee(10L)
                .totalPrice(110L)
                .orderItems(List.of(item))
                .build();

        OrderResponse response = mapper.toResponse(orderWithItems);
        assertEquals("o1", response.getOrderId());
        assertEquals("u1", response.getUserId());
        assertEquals(1, response.getItems().size());
        assertEquals("oi1", response.getItems().get(0).getCartItemId());
        assertEquals("p1", response.getItems().get(0).getProductId());
        assertEquals(2, response.getItems().get(0).getQuantity());
        assertEquals("u1", response.getItems().get(0).getUserId());

        Order orderWithoutUserAndItems = Order.builder()
                .orderId("o2")
                .status("NEW")
                .subtotalPrice(0L)
                .shippingFee(0L)
                .totalPrice(0L)
                .orderItems(Collections.emptyList())
                .build();

        OrderResponse withoutUser = mapper.toResponse(orderWithoutUserAndItems);
        assertEquals("o2", withoutUser.getOrderId());
        assertNull(withoutUser.getUserId());
        assertNull(withoutUser.getItems());
        assertEquals("NEW", withoutUser.getStatus());

        Order orderWithNullItems = Order.builder()
                .orderId("o-null")
                .user(user)
                .status("PENDING")
                .subtotalPrice(1L)
                .shippingFee(0L)
                .totalPrice(1L)
                .orderItems(null)
                .build();

        OrderResponse nullItemsResponse = mapper.toResponse(orderWithNullItems);
        assertEquals("o-null", nullItemsResponse.getOrderId());
        assertNull(nullItemsResponse.getItems());

        OrderItem itemWithoutProduct = OrderItem.builder().orderItemId("oi2").quantity(1).unitPrice(10L).build();
        Order orderWithItemWithoutProduct = Order.builder()
                .orderId("o3")
                .user(user)
                .status("PENDING")
                .subtotalPrice(10L)
                .shippingFee(0L)
                .totalPrice(10L)
                .orderItems(List.of(itemWithoutProduct))
                .build();

        OrderResponse missingProduct = mapper.toResponse(orderWithItemWithoutProduct);
        assertEquals(1, missingProduct.getItems().size());
        assertNull(missingProduct.getItems().get(0).getProductId());

        OrderItem itemWithProduct = OrderItem.builder().orderItemId("oi3").product(product).quantity(1).unitPrice(10L)
                .build();
        Order orderWithItemsButNoUser = Order.builder()
                .orderId("o4")
                .user(null)
                .status("PENDING")
                .subtotalPrice(10L)
                .shippingFee(0L)
                .totalPrice(10L)
                .orderItems(List.of(itemWithProduct))
                .build();

        OrderResponse noUserInLoop = mapper.toResponse(orderWithItemsButNoUser);
        assertEquals(1, noUserInLoop.getItems().size());
        assertNull(noUserInLoop.getItems().get(0).getUserId());
    }
}
