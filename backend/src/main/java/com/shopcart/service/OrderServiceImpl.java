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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private static final String ORDER_STATUS_PENDING = "PENDING";
    private static final String COUPON_STATUS_ACTIVE = "ACTIVE";
    private static final String DISCOUNT_PERCENTAGE = "PERCENTAGE";
    private static final String DISCOUNT_FIXED_AMOUNT = "FIXED_AMOUNT";

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request, String userId) {
        User user = getUserOrThrow(userId);
        long shippingFee = resolveShippingFee(request);

        Order order = initializePendingOrder(request, user, shippingFee);

        long subtotalPrice = 0L;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItemRequest itemRequest : request.getItems()) {
            Product product = getProductOrThrow(itemRequest.getProductId());
            checkAndReduceInventory(product.getProductId(), itemRequest.getQuantity());

            subtotalPrice += product.getPrice() * itemRequest.getQuantity();
            orderItems.add(buildOrderItem(order, product, itemRequest.getQuantity(), product.getPrice()));
        }

        long discountAmount = calculateDiscountAmount(subtotalPrice, request.getCouponCode());
        long totalPrice = Math.max(0L, subtotalPrice - discountAmount) + shippingFee;

        order.setSubtotalPrice(subtotalPrice);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        orderItems.forEach(item -> item.setOrder(savedOrder));
        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(savedOrderItems);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(String userId) {
        return orderRepository.findByUserUserId(userId)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toResponse(order);
    }

    private Order initializePendingOrder(OrderRequest request, User user, long shippingFee) {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setUser(user);
        order.setStatus(ORDER_STATUS_PENDING);
        order.setShippingFee(shippingFee);
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setSubtotalPrice(0L);
        order.setTotalPrice(shippingFee);
        order.setCoupon(resolveCoupon(request.getCouponCode()));
        return order;
    }

    private User getUserOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private Product getProductOrThrow(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }

    private void checkAndReduceInventory(String productId, Integer requestedQuantity) {
        Inventory inventory = inventoryRepository.findByProductProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product id: " + productId));

        if (inventory.getQuantity() < requestedQuantity) {
            throw new OutOfStockException("Product " + productId + " is out of stock");
        }

        inventory.setQuantity(inventory.getQuantity() - requestedQuantity);
        inventoryRepository.save(inventory);
    }

    private OrderItem buildOrderItem(Order order, Product product, Integer quantity, Long unitPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(UUID.randomUUID().toString());
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(unitPrice);
        return orderItem;
    }

    private long calculateDiscountAmount(long subtotalPrice, String couponCode) {
        Coupon coupon = resolveCoupon(couponCode);
        if (coupon == null) {
            return 0L;
        }

        if (DISCOUNT_PERCENTAGE.equals(coupon.getDiscountType())) {
            return subtotalPrice * coupon.getDiscountValue() / 100L;
        }

        if (DISCOUNT_FIXED_AMOUNT.equals(coupon.getDiscountType())) {
            return coupon.getDiscountValue();
        }

        throw new IllegalArgumentException("Unsupported discount type: " + coupon.getDiscountType());
    }

    private Coupon resolveCoupon(String couponCode) {
        if (couponCode == null || couponCode.isBlank()) {
            return null;
        }

        Coupon coupon = couponRepository.findByCouponCode(couponCode)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with code: " + couponCode));

        if (!COUPON_STATUS_ACTIVE.equals(coupon.getStatus())) {
            throw new IllegalArgumentException("Coupon is not active");
        }

        if (coupon.getValidUntil() != null && coupon.getValidUntil().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Coupon has expired");
        }

        return coupon;
    }

    private long resolveShippingFee(OrderRequest request) {
        return request.getShippingFee() == null ? 0L : request.getShippingFee();
    }
}
