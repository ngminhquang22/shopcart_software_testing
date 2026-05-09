package com.shopcart.service;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.OrderRequest;
import com.shopcart.dto.OrderResponse;
import com.shopcart.entity.Coupon;
import com.shopcart.entity.Inventory;
import com.shopcart.entity.Order;
import com.shopcart.entity.OrderItem;
import com.shopcart.entity.Product;
import com.shopcart.entity.User;
import com.shopcart.exception.OutOfStockException;
import com.shopcart.exception.ResourceNotFoundException;
import com.shopcart.mapper.OrderMapper;
import com.shopcart.repository.CouponRepository;
import com.shopcart.repository.InventoryRepository;
import com.shopcart.repository.OrderItemRepository;
import com.shopcart.repository.OrderRepository;
import com.shopcart.repository.ProductRepository;
import com.shopcart.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

        @Mock
        private OrderRepository orderRepository;

        @Mock
        private OrderItemRepository orderItemRepository;

        @Mock
        private CouponRepository couponRepository;

        @Mock
        private ProductRepository productRepository;

        @Mock
        private InventoryRepository inventoryRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private OrderMapper orderMapper;

        @InjectMocks
        private OrderServiceImpl orderService;

        private User user;
        private Product laptop;
        private Product mouse;
        private Inventory laptopInventory;
        private Inventory mouseInventory;
        private Coupon coupon;
        private OrderRequest orderRequest;
        private CartItemRequest laptopItemRequest;
        private CartItemRequest mouseItemRequest;

        @BeforeEach
        void setUp() {
                user = User.builder()
                                .userId("user-1")
                                .username("john")
                                .email("john@example.com")
                                .role("USER")
                                .build();

                laptop = Product.builder()
                                .productId("product-1")
                                .name("Laptop")
                                .price(15_000_000L)
                                .status("ACTIVE")
                                .build();

                mouse = Product.builder()
                                .productId("product-2")
                                .name("Mouse")
                                .price(500_000L)
                                .status("ACTIVE")
                                .build();

                laptopInventory = Inventory.builder()
                                .inventoryId("inventory-1")
                                .product(laptop)
                                .quantity(10)
                                .build();

                mouseInventory = Inventory.builder()
                                .inventoryId("inventory-2")
                                .product(mouse)
                                .quantity(10)
                                .build();

                coupon = Coupon.builder()
                                .couponCode("SAVE10")
                                .discountType("PERCENTAGE")
                                .discountValue(10L)
                                .status("ACTIVE")
                                .validUntil(LocalDateTime.now().plusDays(1))
                                .build();

                laptopItemRequest = new CartItemRequest("user-1", "product-1", 2);
                mouseItemRequest = new CartItemRequest("user-1", "product-2", 1);

                orderRequest = new OrderRequest(
                                "user-1",
                                List.of(laptopItemRequest, mouseItemRequest),
                                "SAVE10",
                                50_000L,
                                "123 Test Street",
                                "COD");
        }

        @Test
        void testCreateOrder_Success() {
                when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
                when(couponRepository.findByCouponCode("SAVE10")).thenReturn(Optional.of(coupon));
                when(productRepository.findById("product-1")).thenReturn(Optional.of(laptop));
                when(productRepository.findById("product-2")).thenReturn(Optional.of(mouse));
                when(inventoryRepository.findByProductProductId("product-1")).thenReturn(Optional.of(laptopInventory));
                when(inventoryRepository.findByProductProductId("product-2")).thenReturn(Optional.of(mouseInventory));
                when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
                when(orderItemRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
                when(orderMapper.toResponse(any(Order.class))).thenAnswer(invocation -> {
                        Order order = invocation.getArgument(0);
                        return OrderResponse.builder()
                                        .orderId(order.getOrderId())
                                        .userId(order.getUser().getUserId())
                                        .subtotalPrice(order.getSubtotalPrice())
                                        .shippingFee(order.getShippingFee())
                                        .totalPrice(order.getTotalPrice())
                                        .status(order.getStatus())
                                        .build();
                });

                OrderResponse response = orderService.createOrder(orderRequest, user.getUserId());

                ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
                verify(orderRepository).save(orderCaptor.capture());
                verify(orderItemRepository).saveAll(anyList());
                verify(inventoryRepository, times(2)).save(any(Inventory.class));

                Order savedOrder = orderCaptor.getValue();
                assertEquals("PENDING", savedOrder.getStatus());
                assertEquals(30_500_000L, savedOrder.getSubtotalPrice());
                assertEquals(50_000L, savedOrder.getShippingFee());
                assertEquals(27_500_000L, savedOrder.getTotalPrice());
                assertEquals("user-1", savedOrder.getUser().getUserId());
                assertEquals("SAVE10", savedOrder.getCoupon().getCouponCode());

                ArgumentCaptor<Inventory> inventoryCaptor = ArgumentCaptor.forClass(Inventory.class);
                verify(inventoryRepository, times(2)).save(inventoryCaptor.capture());
                List<Inventory> savedInventories = inventoryCaptor.getAllValues();
                assertEquals(8, savedInventories.get(0).getQuantity());
                assertEquals(9, savedInventories.get(1).getQuantity());

                assertNotNull(response);
                assertEquals(savedOrder.getOrderId(), response.getOrderId());
                assertEquals(27_500_000L, response.getTotalPrice());
        }

        @Test
        void testGetOrderById_Found() {
                Order order = Order.builder()
                                .orderId("order-1")
                                .user(user)
                                .status("PENDING")
                                .subtotalPrice(30_500_000L)
                                .shippingFee(50_000L)
                                .totalPrice(27_500_000L)
                                .shippingAddress("123 Test Street")
                                .paymentMethod("COD")
                                .build();

                when(orderRepository.findById("order-1")).thenReturn(Optional.of(order));
                when(orderMapper.toResponse(any(Order.class))).thenAnswer(invocation -> {
                        Order mappedOrder = invocation.getArgument(0);
                        return OrderResponse.builder()
                                        .orderId(mappedOrder.getOrderId())
                                        .userId(mappedOrder.getUser().getUserId())
                                        .subtotalPrice(mappedOrder.getSubtotalPrice())
                                        .shippingFee(mappedOrder.getShippingFee())
                                        .totalPrice(mappedOrder.getTotalPrice())
                                        .status(mappedOrder.getStatus())
                                        .build();
                });

                OrderResponse response = orderService.getOrderById("order-1");

                ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
                verify(orderMapper).toResponse(orderCaptor.capture());

                Order capturedOrder = orderCaptor.getValue();
                assertEquals("order-1", capturedOrder.getOrderId());
                assertEquals("user-1", capturedOrder.getUser().getUserId());
                assertEquals("PENDING", capturedOrder.getStatus());
                assertEquals(30_500_000L, capturedOrder.getSubtotalPrice());
                assertEquals(50_000L, capturedOrder.getShippingFee());
                assertEquals(27_500_000L, capturedOrder.getTotalPrice());
                assertEquals(response.getOrderId(), capturedOrder.getOrderId());
        }

        @Test
        void testGetOrderById_NotFound() {
                when(orderRepository.findById("missing-order")).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> orderService.getOrderById("missing-order"));

                assertEquals("Order not found with id: missing-order", exception.getMessage());
                verify(orderMapper, never()).toResponse(any(Order.class));
        }

        @Test
        void testUpdateOrderStatus_Success() {
                Order order = Order.builder()
                                .orderId("order-1")
                                .user(user)
                                .status("PENDING")
                                .subtotalPrice(100_000L)
                                .shippingFee(0L)
                                .totalPrice(100_000L)
                                .build();

                when(orderRepository.findById("order-1")).thenReturn(Optional.of(order));
                when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
                when(orderMapper.toResponse(any(Order.class))).thenAnswer(invocation -> {
                        Order savedOrder = invocation.getArgument(0);
                        return OrderResponse.builder()
                                        .orderId(savedOrder.getOrderId())
                                        .status(savedOrder.getStatus())
                                        .build();
                });

                OrderResponse response = orderService.updateOrderStatus("order-1", "COMPLETED");

                ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
                verify(orderRepository).save(orderCaptor.capture());

                Order savedOrder = orderCaptor.getValue();
                assertEquals("COMPLETED", savedOrder.getStatus());
                assertEquals("COMPLETED", response.getStatus());
        }

        @Test
        void testUpdateOrderStatus_NotFound_ShouldThrowException() {
                when(orderRepository.findById("missing-order")).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> orderService.updateOrderStatus("missing-order", "COMPLETED"));

                assertEquals("Order not found with id: missing-order", exception.getMessage());
                verify(orderRepository, never()).save(any(Order.class));
                verify(orderMapper, never()).toResponse(any(Order.class));
        }

        @Test
        void testCancelOrder() {
                Order order = Order.builder()
                                .orderId("order-1")
                                .user(user)
                                .status("PENDING")
                                .subtotalPrice(100_000L)
                                .shippingFee(0L)
                                .totalPrice(100_000L)
                                .build();

                when(orderRepository.findById("order-1")).thenReturn(Optional.of(order));

                orderService.cancelOrder("order-1");

                verify(orderRepository).delete(order);
        }

        @Test
        void testCancelOrder_OrderNotFound_ShouldThrowException() {
                when(orderRepository.findById("missing-order")).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> orderService.cancelOrder("missing-order"));

                assertEquals("Order not found with id: missing-order", exception.getMessage());
                verify(orderRepository, never()).delete(any(Order.class));
        }

        @Test
        void testGetOrdersByUserId_Found() {
                Order firstOrder = Order.builder()
                                .orderId("order-1")
                                .user(user)
                                .status("PENDING")
                                .subtotalPrice(30_500_000L)
                                .shippingFee(50_000L)
                                .totalPrice(27_500_000L)
                                .build();

                Order secondOrder = Order.builder()
                                .orderId("order-2")
                                .user(user)
                                .status("PENDING")
                                .subtotalPrice(1_000_000L)
                                .shippingFee(50_000L)
                                .totalPrice(1_050_000L)
                                .build();

                when(orderRepository.findByUserUserId(user.getUserId())).thenReturn(List.of(firstOrder, secondOrder));
                when(orderMapper.toResponse(firstOrder)).thenReturn(OrderResponse.builder().orderId("order-1").build());
                when(orderMapper.toResponse(secondOrder))
                                .thenReturn(OrderResponse.builder().orderId("order-2").build());

                List<OrderResponse> responses = orderService.getOrdersByUserId(user.getUserId());

                assertEquals(2, responses.size());
                assertEquals("order-1", responses.get(0).getOrderId());
                assertEquals("order-2", responses.get(1).getOrderId());
                verify(orderRepository).findByUserUserId(eq(user.getUserId()));
        }

        @Test
        void testCreateOrder_CouponExpired_ShouldThrowException() {
                coupon.setValidUntil(LocalDateTime.now().minusDays(1));

                when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
                when(couponRepository.findByCouponCode("SAVE10")).thenReturn(Optional.of(coupon));

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                () -> orderService.createOrder(orderRequest, user.getUserId()));

                assertEquals("Coupon has expired", exception.getMessage());
                verify(orderRepository, never()).save(any(Order.class));
                verify(orderItemRepository, never()).saveAll(anyList());
                verify(productRepository, never()).findById(any());
                verify(inventoryRepository, never()).save(any(Inventory.class));
        }

        @Test
        void testCreateOrder_CouponInactive_ShouldThrowException() {
                coupon.setStatus("INACTIVE");

                when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
                when(couponRepository.findByCouponCode("SAVE10")).thenReturn(Optional.of(coupon));

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                () -> orderService.createOrder(orderRequest, user.getUserId()));

                assertEquals("Coupon is not active", exception.getMessage());
                verify(orderRepository, never()).save(any(Order.class));
                verify(orderItemRepository, never()).saveAll(anyList());
                verify(productRepository, never()).findById(any());
                verify(inventoryRepository, never()).save(any(Inventory.class));
        }

        @Test
        void testCreateOrder_CouponNotFound_ShouldThrowException() {
                when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
                when(couponRepository.findByCouponCode("SAVE10")).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> orderService.createOrder(orderRequest, user.getUserId()));

                assertEquals("Coupon not found with code: SAVE10", exception.getMessage());
                verify(orderRepository, never()).save(any(Order.class));
                verify(orderItemRepository, never()).saveAll(anyList());
                verify(productRepository, never()).findById(any());
                verify(inventoryRepository, never()).save(any(Inventory.class));
        }

        @Test
        void testCreateOrder_UserNotFound_ShouldThrowException() {
                when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> orderService.createOrder(orderRequest, user.getUserId()));

                assertEquals("User not found with id: user-1", exception.getMessage());
                verify(couponRepository, never()).findByCouponCode(any());
                verify(productRepository, never()).findById(any());
                verify(inventoryRepository, never()).save(any(Inventory.class));
                verify(orderRepository, never()).save(any(Order.class));
        }

        @Test
        void testCreateOrder_ProductNotFound_ShouldThrowException() {
                OrderRequest requestWithoutCoupon = new OrderRequest(
                                "user-1",
                                List.of(laptopItemRequest, mouseItemRequest),
                                null,
                                50_000L,
                                "123 Test Street",
                                "COD");

                when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
                when(productRepository.findById("product-1")).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> orderService.createOrder(requestWithoutCoupon, user.getUserId()));

                assertEquals("Product not found with id: product-1", exception.getMessage());
                verify(inventoryRepository, never()).findByProductProductId(any());
                verify(orderRepository, never()).save(any(Order.class));
                verify(orderItemRepository, never()).saveAll(anyList());
        }

        @Test
        void testCheckAndReduceInventory_InventoryNotFound_ShouldThrowException() {
                OrderRequest requestWithoutCoupon = new OrderRequest(
                                "user-1",
                                List.of(laptopItemRequest),
                                null,
                                50_000L,
                                "123 Test Street",
                                "COD");

                when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
                when(productRepository.findById(laptop.getProductId())).thenReturn(Optional.of(laptop));
                when(inventoryRepository.findByProductProductId(laptop.getProductId())).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> orderService.createOrder(requestWithoutCoupon, user.getUserId()));

                assertEquals("Inventory not found for product id: product-1", exception.getMessage());
                verify(orderRepository, never()).save(any(Order.class));
                verify(orderItemRepository, never()).saveAll(anyList());
                verify(inventoryRepository, never()).save(any(Inventory.class));
        }

        @Test
        void testCreateOrder_WithFixedDiscountAmount() {
                coupon.setDiscountType("FIXED_AMOUNT");
                coupon.setDiscountValue(50_000L);

                when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
                when(couponRepository.findByCouponCode("SAVE10")).thenReturn(Optional.of(coupon));
                when(productRepository.findById("product-1")).thenReturn(Optional.of(laptop));
                when(productRepository.findById("product-2")).thenReturn(Optional.of(mouse));
                when(inventoryRepository.findByProductProductId("product-1")).thenReturn(Optional.of(laptopInventory));
                when(inventoryRepository.findByProductProductId("product-2")).thenReturn(Optional.of(mouseInventory));
                when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
                when(orderItemRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
                when(orderMapper.toResponse(any(Order.class))).thenAnswer(invocation -> {
                        Order order = invocation.getArgument(0);
                        return OrderResponse.builder()
                                        .orderId(order.getOrderId())
                                        .subtotalPrice(order.getSubtotalPrice())
                                        .shippingFee(order.getShippingFee())
                                        .totalPrice(order.getTotalPrice())
                                        .status(order.getStatus())
                                        .build();
                });

                OrderResponse response = orderService.createOrder(orderRequest, user.getUserId());

                assertEquals(30_500_000L, response.getSubtotalPrice());
                assertEquals(50_000L, response.getShippingFee());
                assertEquals(30_500_000L, response.getTotalPrice());
        }

        @Test
        void testCreateOrder_UnsupportedDiscountType_ShouldThrowException() {
                coupon.setDiscountType("UNKNOWN");

                when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
                when(couponRepository.findByCouponCode("SAVE10")).thenReturn(Optional.of(coupon));
                when(productRepository.findById("product-1")).thenReturn(Optional.of(laptop));
                when(productRepository.findById("product-2")).thenReturn(Optional.of(mouse));
                when(inventoryRepository.findByProductProductId("product-1")).thenReturn(Optional.of(laptopInventory));
                when(inventoryRepository.findByProductProductId("product-2")).thenReturn(Optional.of(mouseInventory));

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                () -> orderService.createOrder(orderRequest, user.getUserId()));

                assertEquals("Unsupported discount type: UNKNOWN", exception.getMessage());
                verify(orderRepository, never()).save(any(Order.class));
                verify(orderItemRepository, never()).saveAll(anyList());
                verify(inventoryRepository, times(2)).save(any(Inventory.class));
        }

        @Test
        void testCreateOrder_WithoutShippingFee() {
                OrderRequest requestWithoutShippingFee = new OrderRequest(
                                "user-1",
                                List.of(laptopItemRequest, mouseItemRequest),
                                null,
                                null,
                                "123 Test Street",
                                "COD");

                when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
                when(productRepository.findById("product-1")).thenReturn(Optional.of(laptop));
                when(productRepository.findById("product-2")).thenReturn(Optional.of(mouse));
                when(inventoryRepository.findByProductProductId("product-1")).thenReturn(Optional.of(laptopInventory));
                when(inventoryRepository.findByProductProductId("product-2")).thenReturn(Optional.of(mouseInventory));
                when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
                when(orderItemRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
                when(orderMapper.toResponse(any(Order.class))).thenAnswer(invocation -> {
                        Order order = invocation.getArgument(0);
                        return OrderResponse.builder()
                                        .orderId(order.getOrderId())
                                        .subtotalPrice(order.getSubtotalPrice())
                                        .shippingFee(order.getShippingFee())
                                        .totalPrice(order.getTotalPrice())
                                        .build();
                });

                OrderResponse response = orderService.createOrder(requestWithoutShippingFee, user.getUserId());

                assertEquals(30_500_000L, response.getSubtotalPrice());
                assertEquals(0L, response.getShippingFee());
                assertEquals(30_500_000L, response.getTotalPrice());
        }

        @Test
        void testCalculateOrderTotal() {
                when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
                when(couponRepository.findByCouponCode("SAVE10")).thenReturn(Optional.of(coupon));
                when(productRepository.findById("product-1")).thenReturn(Optional.of(laptop));
                when(productRepository.findById("product-2")).thenReturn(Optional.of(mouse));
                when(inventoryRepository.findByProductProductId("product-1")).thenReturn(Optional.of(laptopInventory));
                when(inventoryRepository.findByProductProductId("product-2")).thenReturn(Optional.of(mouseInventory));
                when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
                when(orderItemRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
                when(orderMapper.toResponse(any(Order.class))).thenAnswer(invocation -> {
                        Order order = invocation.getArgument(0);
                        return OrderResponse.builder()
                                        .orderId(order.getOrderId())
                                        .subtotalPrice(order.getSubtotalPrice())
                                        .shippingFee(order.getShippingFee())
                                        .totalPrice(order.getTotalPrice())
                                        .build();
                });

                OrderResponse response = orderService.createOrder(orderRequest, user.getUserId());

                assertEquals(30_500_000L, response.getSubtotalPrice());
                assertEquals(50_000L, response.getShippingFee());
                assertEquals(27_500_000L, response.getTotalPrice());
        }

        @Test
        void testCheckStockBeforeOrder_InsufficientStock() {
                OrderRequest insufficientStockRequest = new OrderRequest(
                                user.getUserId(),
                                List.of(new CartItemRequest(user.getUserId(), laptop.getProductId(), 11)),
                                null,
                                50_000L,
                                "123 Test Street",
                                "COD");

                laptopInventory.setQuantity(10);

                when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
                when(productRepository.findById(laptop.getProductId())).thenReturn(Optional.of(laptop));
                when(inventoryRepository.findByProductProductId(laptop.getProductId()))
                                .thenReturn(Optional.of(laptopInventory));

                OutOfStockException exception = assertThrows(OutOfStockException.class,
                                () -> orderService.createOrder(insufficientStockRequest, user.getUserId()));

                assertEquals("Product product-1 is out of stock", exception.getMessage());
                verify(orderRepository, never()).save(any(Order.class));
                verify(orderItemRepository, never()).saveAll(anyList());
                verify(inventoryRepository, never()).save(any(Inventory.class));
        }
}