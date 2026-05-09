package com.shopcart.service;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.OrderRequest;
import com.shopcart.dto.OrderResponse;
import com.shopcart.entity.Inventory;
import com.shopcart.entity.Order;
import com.shopcart.entity.Product;
import com.shopcart.entity.User;
import com.shopcart.mapper.OrderMapper;
import com.shopcart.repository.CouponRepository;
import com.shopcart.repository.InventoryRepository;
import com.shopcart.repository.OrderItemRepository;
import com.shopcart.repository.OrderRepository;
import com.shopcart.repository.ProductRepository;
import com.shopcart.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceMockTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testCheckAndReduceInventory_Success() {
        User user = User.builder().userId("U001").username("test").build();
        Product product = Product.builder().productId("P001").name("Laptop").price(10_000L).build();
        Inventory inventory = Inventory.builder().inventoryId("I001").product(product).quantity(10).build();

        OrderRequest request = new OrderRequest(
                "U001",
                List.of(new CartItemRequest("U001", "P001", 3)),
                null,
                0L,
                "HCM",
                "COD");

        when(userRepository.findById("U001")).thenReturn(Optional.of(user));
        when(productRepository.findById("P001")).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductProductId("P001")).thenReturn(Optional.of(inventory));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderItemRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toResponse(any(Order.class))).thenReturn(OrderResponse.builder().orderId("ORD-001").build());

        orderService.createOrder(request, "U001");

        ArgumentCaptor<Inventory> captor = ArgumentCaptor.forClass(Inventory.class);
        verify(inventoryRepository).save(captor.capture());
        assertEquals(7, captor.getValue().getQuantity());
    }

    @Test
    void testCreateOrder_VerifyRepositoryInteractions() {
        User user = User.builder().userId("U001").username("test").build();
        Product product = Product.builder().productId("P001").name("Laptop").price(100_000L).build();
        Inventory inventory = Inventory.builder().inventoryId("I001").product(product).quantity(10).build();

        OrderRequest request = new OrderRequest(
                "U001",
                List.of(new CartItemRequest("U001", "P001", 3)),
                null,
                10_000L,
                "HCM",
                "COD");

        when(userRepository.findById("U001")).thenReturn(Optional.of(user));
        when(productRepository.findById("P001")).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductProductId("P001")).thenReturn(Optional.of(inventory));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderItemRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toResponse(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            return OrderResponse.builder()
                    .orderId(savedOrder.getOrderId())
                    .userId(savedOrder.getUser().getUserId())
                    .subtotalPrice(savedOrder.getSubtotalPrice())
                    .shippingFee(savedOrder.getShippingFee())
                    .totalPrice(savedOrder.getTotalPrice())
                    .status(savedOrder.getStatus())
                    .build();
        });

        OrderResponse response = orderService.createOrder(request, "U001");

        verify(orderRepository, times(1)).save(any(Order.class));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order captured = orderCaptor.getValue();
        assertEquals("U001", captured.getUser().getUserId());
        assertEquals(300_000L, captured.getSubtotalPrice());
        assertEquals(10_000L, captured.getShippingFee());
        assertEquals(310_000L, captured.getTotalPrice());

        assertNotNull(response);
        assertEquals("U001", response.getUserId());
        assertEquals(310_000L, response.getTotalPrice());
    }
}
