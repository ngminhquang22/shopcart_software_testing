package com.shopcart.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntityCoverageTest {

    @Test
    void testUserEntityCoverage() {
        User user = new User();
        user.setUserId("u1");
        user.setUsername("john");
        user.setRole("USER");
        user.setEmail("john@example.com");
        user.setCartItems(List.of());
        user.setOrders(List.of());

        assertEquals("u1", user.getUserId());
        assertEquals("john", user.getUsername());
        assertEquals("USER", user.getRole());
        assertEquals("john@example.com", user.getEmail());
        assertNotNull(user.toString());

        User same = User.builder()
                .userId("u1")
                .username("john")
                .role("USER")
                .email("john@example.com")
                .cartItems(List.of())
                .orders(List.of())
                .build();
        assertEquals(user, same);
        assertEquals(user.hashCode(), same.hashCode());
        assertNotEquals(user, null);
        assertNotEquals(user, new Object());
        assertTrue(user.canEqual(same));
        assertFalse(user.canEqual("x"));
        assertNotNull(User.builder().userId("u").username("n").role("r").email("e").cartItems(List.of())
                .orders(List.of()).toString());

        User different = User.builder()
                .userId("u2")
                .username("john")
                .role("USER")
                .email("john@example.com")
                .build();
        assertNotEquals(user, different);

        User allArgs = new User("u3", "alice", "ADMIN", "alice@example.com", LocalDateTime.now(), List.of(), List.of());
        assertEquals("u3", allArgs.getUserId());

        User prePersistUser = new User();
        assertNull(prePersistUser.getCreatedAt());
        prePersistUser.onCreate();
        assertNotNull(prePersistUser.getCreatedAt());
    }

    @Test
    void testProductEntityCoverage() {
        Product product = new Product();
        product.setProductId("p1");
        product.setName("Laptop");
        product.setPrice(100L);
        product.setStatus("ACTIVE");
        product.setCartItems(List.of());
        product.setOrderItems(List.of());

        assertEquals("p1", product.getProductId());
        assertEquals("Laptop", product.getName());
        assertEquals(100L, product.getPrice());
        assertEquals("ACTIVE", product.getStatus());
        assertNotNull(product.toString());

        Product same = Product.builder()
                .productId("p1")
                .name("Laptop")
                .price(100L)
                .status("ACTIVE")
                .cartItems(List.of())
                .orderItems(List.of())
                .build();
        assertEquals(product, same);
        assertEquals(product.hashCode(), same.hashCode());
        assertNotEquals(product, null);
        assertNotEquals(product, new Object());
        assertTrue(product.canEqual(same));
        assertFalse(product.canEqual("x"));
        assertNotNull(Product.builder().productId("p").name("n").price(1L).status("s").cartItems(List.of())
                .orderItems(List.of()).toString());

        Product allArgs = new Product("p2", "Mouse", 50L, "ACTIVE", null, List.of(), List.of());
        assertEquals("p2", allArgs.getProductId());
    }

    @Test
    void testInventoryEntityCoverage() {
        Product product = Product.builder().productId("p1").name("Laptop").price(100L).status("ACTIVE").build();

        Inventory inventory = new Inventory();
        inventory.setInventoryId("i1");
        inventory.setProduct(product);
        inventory.setQuantity(10);

        assertEquals("i1", inventory.getInventoryId());
        assertEquals(product, inventory.getProduct());
        assertEquals(10, inventory.getQuantity());
        assertNotNull(inventory.toString());

        Inventory same = Inventory.builder().inventoryId("i1").product(product).quantity(10).build();
        assertEquals(inventory, same);
        assertEquals(inventory.hashCode(), same.hashCode());
        assertNotEquals(inventory, null);
        assertNotEquals(inventory, new Object());
        assertTrue(inventory.canEqual(same));
        assertFalse(inventory.canEqual("x"));
        assertNotNull(Inventory.builder().inventoryId("i").product(product).quantity(1).toString());

        Inventory allArgs = new Inventory("i2", product, 5);
        assertEquals("i2", allArgs.getInventoryId());
    }

    @Test
    void testCartItemEntityCoverage() {
        User user = User.builder().userId("u1").username("john").build();
        Product product = Product.builder().productId("p1").name("Laptop").price(100L).status("ACTIVE").build();

        CartItem item = new CartItem();
        item.setCartItemId("c1");
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(2);

        assertEquals("c1", item.getCartItemId());
        assertEquals(user, item.getUser());
        assertEquals(product, item.getProduct());
        assertEquals(2, item.getQuantity());
        assertNotNull(item.toString());

        CartItem same = CartItem.builder().cartItemId("c1").user(user).product(product).quantity(2).build();
        assertEquals(item, same);
        assertEquals(item.hashCode(), same.hashCode());
        assertNotEquals(item, null);
        assertNotEquals(item, new Object());
        assertTrue(item.canEqual(same));
        assertFalse(item.canEqual("x"));
        assertNotNull(CartItem.builder().cartItemId("x").user(user).product(product).quantity(1).toString());

        CartItem allArgs = new CartItem("c2", user, product, 1);
        assertEquals("c2", allArgs.getCartItemId());
    }

    @Test
    void testOrderItemEntityCoverage() {
        Product product = Product.builder().productId("p1").name("Laptop").price(100L).status("ACTIVE").build();
        User user = User.builder().userId("u1").username("john").build();
        Order order = Order.builder()
                .orderId("o1")
                .user(user)
                .status("PENDING")
                .subtotalPrice(100L)
                .shippingFee(10L)
                .totalPrice(110L)
                .build();

        OrderItem item = new OrderItem();
        item.setOrderItemId("oi1");
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(2);
        item.setUnitPrice(50L);

        assertEquals("oi1", item.getOrderItemId());
        assertEquals(order, item.getOrder());
        assertEquals(product, item.getProduct());
        assertEquals(2, item.getQuantity());
        assertEquals(50L, item.getUnitPrice());
        assertNotNull(item.toString());

        OrderItem same = OrderItem.builder().orderItemId("oi1").order(order).product(product).quantity(2).unitPrice(50L)
                .build();
        assertEquals(item, same);
        assertEquals(item.hashCode(), same.hashCode());
        assertNotEquals(item, null);
        assertNotEquals(item, new Object());
        assertTrue(item.canEqual(same));
        assertFalse(item.canEqual("x"));
        assertNotNull(OrderItem.builder().orderItemId("x").order(order).product(product).quantity(1).unitPrice(1L)
                .toString());

        OrderItem allArgs = new OrderItem("oi2", order, product, 1, 100L);
        assertEquals("oi2", allArgs.getOrderItemId());
    }

    @Test
    void testCouponAndOrderEntityCoverage() {
        User user = User.builder().userId("u1").username("john").build();
        Coupon coupon = new Coupon();
        coupon.setCouponCode("SAVE10");
        coupon.setDiscountType("PERCENTAGE");
        coupon.setDiscountValue(10L);
        coupon.setStatus("ACTIVE");
        coupon.setValidUntil(LocalDateTime.now().plusDays(1));
        coupon.setOrders(List.of());

        assertEquals("SAVE10", coupon.getCouponCode());
        assertEquals("PERCENTAGE", coupon.getDiscountType());
        assertEquals(10L, coupon.getDiscountValue());
        assertEquals("ACTIVE", coupon.getStatus());
        assertNotNull(coupon.toString());

        Coupon sameCoupon = Coupon.builder()
                .couponCode("SAVE10")
                .discountType("PERCENTAGE")
                .discountValue(10L)
                .status("ACTIVE")
                .validUntil(coupon.getValidUntil())
                .orders(List.of())
                .build();
        assertEquals(coupon, sameCoupon);
        assertEquals(coupon.hashCode(), sameCoupon.hashCode());
        assertTrue(coupon.canEqual(sameCoupon));
        assertFalse(coupon.canEqual("x"));
        assertNotNull(Coupon.builder()
                .couponCode("C")
                .discountType("PERCENTAGE")
                .discountValue(1L)
                .status("ACTIVE")
                .validUntil(LocalDateTime.now())
                .orders(List.of())
                .toString());

        Coupon allArgsCoupon = new Coupon("SAVE20", "FIXED_AMOUNT", 20L, "ACTIVE", LocalDateTime.now(), List.of());
        assertEquals("SAVE20", allArgsCoupon.getCouponCode());

        Order order = new Order();
        order.setOrderId("o1");
        order.setUser(user);
        order.setStatus("PENDING");
        order.setCoupon(coupon);
        order.setSubtotalPrice(100L);
        order.setShippingFee(10L);
        order.setTotalPrice(110L);
        order.setShippingAddress("addr");
        order.setPaymentMethod("COD");
        order.setOrderItems(List.of());

        assertEquals("o1", order.getOrderId());
        assertEquals(user, order.getUser());
        assertEquals(coupon, order.getCoupon());
        assertEquals(110L, order.getTotalPrice());
        assertNotNull(order.toString());

        Order sameOrder = Order.builder()
                .orderId("o1")
                .user(user)
                .status("PENDING")
                .coupon(coupon)
                .subtotalPrice(100L)
                .shippingFee(10L)
                .totalPrice(110L)
                .shippingAddress("addr")
                .paymentMethod("COD")
                .orderItems(List.of())
                .build();
        assertEquals(order, sameOrder);
        assertEquals(order.hashCode(), sameOrder.hashCode());
        assertNotEquals(order, null);
        assertNotEquals(order, new Object());
        assertTrue(order.canEqual(sameOrder));
        assertFalse(order.canEqual("x"));
        assertNotNull(Order.builder()
                .orderId("x")
                .user(user)
                .status("PENDING")
                .coupon(coupon)
                .subtotalPrice(1L)
                .shippingFee(1L)
                .totalPrice(2L)
                .shippingAddress("a")
                .paymentMethod("COD")
                .orderItems(List.of())
                .toString());

        Order allArgsOrder = new Order("o2", user, "PENDING", coupon, 10L, 1L, 11L, "a", "COD", List.of());
        assertEquals("o2", allArgsOrder.getOrderId());
    }
}
