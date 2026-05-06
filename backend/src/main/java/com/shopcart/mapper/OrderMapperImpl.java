package com.shopcart.mapper;

import com.shopcart.dto.CartItemResponse;
import com.shopcart.dto.OrderRequest;
import com.shopcart.dto.OrderResponse;
import com.shopcart.entity.Order;
import com.shopcart.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapperImpl implements OrderMapper {

    private final CartItemMapper cartItemMapper;

    public OrderMapperImpl(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    public OrderResponse toResponse(Order order) {
        if (order == null)
            return null;
        OrderResponse resp = new OrderResponse();
        resp.setOrderId(order.getOrderId());
        if (order.getUser() != null)
            resp.setUserId(order.getUser().getUserId());

        // Convert OrderItems to CartItemResponse
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            List<CartItemResponse> items = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                CartItemResponse itemResp = new CartItemResponse();
                itemResp.setCartItemId(orderItem.getOrderItemId());
                if (orderItem.getProduct() != null)
                    itemResp.setProductId(orderItem.getProduct().getProductId());
                itemResp.setQuantity(orderItem.getQuantity());
                if (order.getUser() != null)
                    itemResp.setUserId(order.getUser().getUserId());
                items.add(itemResp);
            }
            resp.setItems(items);
        }

        resp.setSubtotalPrice(order.getSubtotalPrice());
        resp.setShippingFee(order.getShippingFee());
        resp.setTotalPrice(order.getTotalPrice());
        resp.setStatus(order.getStatus());
        return resp;
    }

    @Override
    public Order toEntity(OrderRequest request) {
        if (request == null)
            return null;
        Order entity = new Order();
        // orderItems and user are ignored as per mapping
        return entity;
    }
}
