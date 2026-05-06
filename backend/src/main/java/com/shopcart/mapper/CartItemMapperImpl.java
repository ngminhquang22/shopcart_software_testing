package com.shopcart.mapper;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.entity.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapperImpl implements CartItemMapper {

    @Override
    public CartItemResponse toResponse(CartItem entity) {
        if (entity == null)
            return null;
        CartItemResponse resp = new CartItemResponse();
        resp.setCartItemId(entity.getCartItemId());
        if (entity.getUser() != null)
            resp.setUserId(entity.getUser().getUserId());
        if (entity.getProduct() != null)
            resp.setProductId(entity.getProduct().getProductId());
        resp.setQuantity(entity.getQuantity());
        return resp;
    }

    @Override
    public CartItem toEntity(CartItemRequest request) {
        if (request == null)
            return null;
        CartItem entity = new CartItem();
        entity.setQuantity(request.getQuantity());
        // user and product are ignored as per mapping
        return entity;
    }
}
