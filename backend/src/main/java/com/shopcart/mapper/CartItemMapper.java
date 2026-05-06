package com.shopcart.mapper;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.entity.CartItem;

public interface CartItemMapper {

    CartItemResponse toResponse(CartItem entity);

    CartItem toEntity(CartItemRequest request);
}
