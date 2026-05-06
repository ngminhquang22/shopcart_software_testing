package com.shopcart.mapper;

import com.shopcart.dto.OrderRequest;
import com.shopcart.dto.OrderResponse;
import com.shopcart.entity.Order;

public interface OrderMapper {

    OrderResponse toResponse(Order order);

    Order toEntity(OrderRequest request);
}
