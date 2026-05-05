package com.shopcart.service;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.mapper.CartItemMapper;
import com.shopcart.repository.CartItemRepository;
import com.shopcart.repository.ProductRepository;
import com.shopcart.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    CartItemRepository cartItemRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CartItemMapper cartItemMapper;

    @InjectMocks
    CartService cartService;

    @Test
    void addCartItem_shouldReturnMappedResponse() {
        CartItemRequest request = new CartItemRequest("user-1", "product-1", 2);

        CartItemResponse expected = new CartItemResponse("ci-1", "user-1", "product-1", 2);
        when(cartItemMapper.toResponse(any())).thenReturn(expected);

        CartItemResponse result = cartService.addCartItem(request);

        assertThat(result).isNotNull();
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(result.getUserId()).isEqualTo("user-1");
    }

    @Test
    void updateQuantity_shouldReturnUpdatedResponse() {
        when(cartItemMapper.toResponse(any())).thenReturn(new CartItemResponse("ci-1", "user-1", "product-1", 5));

        var result = cartService.updateQuantity("ci-1", 5);

        assertThat(result).isNotNull();
        assertThat(result.getQuantity()).isEqualTo(5);
    }
}
