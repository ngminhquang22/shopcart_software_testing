package com.shopcart.mapper;

import com.shopcart.dto.OrderRequest;
import com.shopcart.dto.OrderResponse;
import com.shopcart.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "items", source = "orderItems")
    OrderResponse toResponse(Order order);

    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "user", ignore = true)
    Order toEntity(OrderRequest request);
}
