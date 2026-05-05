package com.shopcart.mapper;

import com.shopcart.dto.CartItemRequest;
import com.shopcart.dto.CartItemResponse;
import com.shopcart.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "cartItemId", source = "cartItemId")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "productId", source = "product.productId")
    CartItemResponse toResponse(CartItem entity);

    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    CartItem toEntity(CartItemRequest request);
}
