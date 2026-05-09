package com.shopcart.service;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.entity.CartItem;
import com.shopcart.entity.Inventory;
import com.shopcart.entity.Product;
import com.shopcart.entity.User;
import com.shopcart.exception.OutOfStockException;
import com.shopcart.exception.ResourceNotFoundException;
import com.shopcart.mapper.CartItemMapper;
import com.shopcart.repository.CartItemRepository;
import com.shopcart.repository.InventoryRepository;
import com.shopcart.repository.ProductRepository;
import com.shopcart.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private Product product;
    private Inventory inventory;
    private User user;
    private CartItemRequest request;
    private CartItem existingCartItem;
    private CartItemResponse cartItemResponse;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .productId("product-1")
                .name("Laptop")
                .price(25_000_000L)
                .status("ACTIVE")
                .build();

        inventory = Inventory.builder()
                .inventoryId("inventory-1")
                .product(product)
                .quantity(10)
                .build();

        user = User.builder()
                .userId("user-1")
                .username("john")
                .email("john@example.com")
                .role("USER")
                .build();

        request = new CartItemRequest("user-1", "product-1", 2);

        existingCartItem = CartItem.builder()
                .cartItemId("cart-item-1")
                .user(user)
                .product(product)
                .quantity(3)
                .build();

        cartItemResponse = CartItemResponse.builder()
                .cartItemId("cart-item-1")
                .userId("user-1")
                .productId("product-1")
                .quantity(2)
                .build();
    }

    @Test
    void testAddToCart_Success() {
        when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductProductId(product.getProductId())).thenReturn(Optional.of(inventory));
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));
        when(cartItemRepository.findByUserUserIdAndProductProductId(request.getUserId(), product.getProductId()))
                .thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartItemMapper.toResponse(any(CartItem.class))).thenReturn(cartItemResponse);

        CartItemResponse response = cartService.addToCart(request, request.getUserId());

        ArgumentCaptor<CartItem> cartItemCaptor = ArgumentCaptor.forClass(CartItem.class);
        verify(cartItemRepository).save(cartItemCaptor.capture());

        CartItem savedCartItem = cartItemCaptor.getValue();
        assertEquals(request.getQuantity(), savedCartItem.getQuantity());
        assertEquals(user.getUserId(), savedCartItem.getUser().getUserId());
        assertEquals(product.getProductId(), savedCartItem.getProduct().getProductId());
        assertEquals(cartItemResponse, response);
    }

    @Test
    void testAddToCart_ProductExists_ShouldIncreaseQuantity() {
        when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductProductId(product.getProductId())).thenReturn(Optional.of(inventory));
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));
        when(cartItemRepository.findByUserUserIdAndProductProductId(request.getUserId(), product.getProductId()))
                .thenReturn(Optional.of(existingCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartItemMapper.toResponse(any(CartItem.class))).thenReturn(cartItemResponse);

        cartService.addToCart(request, request.getUserId());

        ArgumentCaptor<CartItem> cartItemCaptor = ArgumentCaptor.forClass(CartItem.class);
        verify(cartItemRepository).save(cartItemCaptor.capture());

        CartItem updatedCartItem = cartItemCaptor.getValue();
        assertEquals(5, updatedCartItem.getQuantity());
        assertEquals(existingCartItem.getCartItemId(), updatedCartItem.getCartItemId());
    }

    @Test
    void testAddToCart_InsufficientStock_ShouldThrowException() {
        request = new CartItemRequest("user-1", "product-1", 11);

        when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductProductId(product.getProductId())).thenReturn(Optional.of(inventory));

        OutOfStockException exception = assertThrows(OutOfStockException.class,
                () -> cartService.addToCart(request, request.getUserId()));

        assertEquals("Requested quantity exceeds available stock", exception.getMessage());
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testAddToCart_ProductNotFound_ShouldThrowException() {
        when(productRepository.findById(request.getProductId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.addToCart(request, request.getUserId()));

        assertEquals("Product not found with id: product-1", exception.getMessage());
        verify(inventoryRepository, never()).findByProductProductId(any());
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testAddToCart_InventoryNotFound_ShouldThrowException() {
        when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductProductId(product.getProductId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.addToCart(request, request.getUserId()));

        assertEquals("Inventory not found for product id: product-1", exception.getMessage());
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testAddToCart_UserNotFound_ShouldThrowException() {
        when(productRepository.findById(request.getProductId())).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductProductId(product.getProductId())).thenReturn(Optional.of(inventory));
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.addToCart(request, request.getUserId()));

        assertEquals("User not found with id: user-1", exception.getMessage());
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testUpdateQuantity_Success() {
        CartItemRequest updateRequest = new CartItemRequest("user-1", "product-1", 4);
        CartItemResponse updatedResponse = CartItemResponse.builder()
                .cartItemId("cart-item-1")
                .userId("user-1")
                .productId("product-1")
                .quantity(4)
                .build();

        when(productRepository.findById(updateRequest.getProductId())).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductProductId(product.getProductId())).thenReturn(Optional.of(inventory));
        when(userRepository.findById(updateRequest.getUserId())).thenReturn(Optional.of(user));
        when(cartItemRepository.findByUserUserIdAndProductProductId(updateRequest.getUserId(), product.getProductId()))
                .thenReturn(Optional.of(existingCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartItemMapper.toResponse(any(CartItem.class))).thenReturn(updatedResponse);

        CartItemResponse response = cartService.updateQuantity(updateRequest, updateRequest.getUserId());

        ArgumentCaptor<CartItem> cartItemCaptor = ArgumentCaptor.forClass(CartItem.class);
        verify(cartItemRepository).save(cartItemCaptor.capture());

        CartItem savedCartItem = cartItemCaptor.getValue();
        assertEquals(updateRequest.getQuantity(), savedCartItem.getQuantity());
        assertEquals(updatedResponse, response);
    }

    @Test
    void testUpdateQuantity_InsufficientStock_ShouldThrowException() {
        CartItemRequest updateRequest = new CartItemRequest("user-1", "product-1", 11);

        when(productRepository.findById(updateRequest.getProductId())).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductProductId(product.getProductId())).thenReturn(Optional.of(inventory));

        OutOfStockException exception = assertThrows(OutOfStockException.class,
                () -> cartService.updateQuantity(updateRequest, updateRequest.getUserId()));

        assertEquals("Requested quantity exceeds available stock", exception.getMessage());
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testUpdateQuantity_CartItemNotFound_ShouldThrowException() {
        CartItemRequest updateRequest = new CartItemRequest("user-1", "product-1", 4);

        when(productRepository.findById(updateRequest.getProductId())).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductProductId(product.getProductId())).thenReturn(Optional.of(inventory));
        when(userRepository.findById(updateRequest.getUserId())).thenReturn(Optional.of(user));
        when(cartItemRepository.findByUserUserIdAndProductProductId(updateRequest.getUserId(), product.getProductId()))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.updateQuantity(updateRequest, updateRequest.getUserId()));

        assertEquals("Cart item not found for user id: user-1 and product id: product-1", exception.getMessage());
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testRemoveFromCart_Success() {
        when(cartItemRepository.findByUserUserIdAndProductProductId(user.getUserId(), product.getProductId()))
                .thenReturn(Optional.of(existingCartItem));

        cartService.removeFromCart(user.getUserId(), product.getProductId());

        verify(cartItemRepository).delete(existingCartItem);
    }

    @Test
    void testRemoveFromCart_CartItemNotFound_ShouldThrowException() {
        when(cartItemRepository.findByUserUserIdAndProductProductId(user.getUserId(), product.getProductId()))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.removeFromCart(user.getUserId(), product.getProductId()));

        assertEquals("Cart item not found for user id: user-1 and product id: product-1", exception.getMessage());
        verify(cartItemRepository, never()).delete(any(CartItem.class));
    }

    @Test
    void testGetCartItemsByUserId_ShouldMapAllItems() {
        CartItem secondCartItem = CartItem.builder()
                .cartItemId("cart-item-2")
                .user(user)
                .product(product)
                .quantity(1)
                .build();

        CartItemResponse firstResponse = CartItemResponse.builder()
                .cartItemId("cart-item-1")
                .userId("user-1")
                .productId("product-1")
                .quantity(3)
                .build();
        CartItemResponse secondResponse = CartItemResponse.builder()
                .cartItemId("cart-item-2")
                .userId("user-1")
                .productId("product-1")
                .quantity(1)
                .build();

        when(cartItemRepository.findByUserUserId(user.getUserId()))
                .thenReturn(List.of(existingCartItem, secondCartItem));
        when(cartItemMapper.toResponse(existingCartItem)).thenReturn(firstResponse);
        when(cartItemMapper.toResponse(secondCartItem)).thenReturn(secondResponse);

        List<CartItemResponse> responses = cartService.getCartItemsByUserId(user.getUserId());

        assertEquals(2, responses.size());
        assertEquals(firstResponse, responses.get(0));
        assertEquals(secondResponse, responses.get(1));
        verify(cartItemRepository).findByUserUserId(eq(user.getUserId()));
    }
}