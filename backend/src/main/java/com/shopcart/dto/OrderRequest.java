package com.shopcart.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotBlank(message = "userId is required")
    private String userId;

    @NotEmpty(message = "items must not be empty")
    @Valid
    private List<CartItemRequest> items;

    private String couponCode;

    @NotNull(message = "shippingFee is required")
    @Min(value = 0, message = "shippingFee must be >= 0")
    private Long shippingFee;

    @NotBlank(message = "shippingAddress is required")
    private String shippingAddress;

    @NotBlank(message = "paymentMethod is required")
    private String paymentMethod;
}
