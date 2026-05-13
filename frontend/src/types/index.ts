export interface Product {
    id: string;
    productId: string;
    name: string;
    price: number;
    stock: number;
    status?: string;
}

export interface CartItem extends Product {
    quantity: number;
}

export interface CartItemRequest {
    userId: string;
    productId: string;
    quantity: number;
}

export interface CartItemResponse {
    cartItemId: string;
    userId: string;
    productId: string;
    quantity: number;
}

export interface OrderItem {
    userId: string;
    productId: string;
    quantity: number;
}

export interface OrderRequest {
    userId: string;
    items: OrderItem[];
    couponCode: string;
    shippingFee: number;
    shippingAddress: string;
    paymentMethod: string;
}

export interface OrderResponse {
    orderId: string;
    userId: string;
    items: CartItemResponse[];
    subtotalPrice: number;
    shippingFee: number;
    totalPrice: number;
    status: string;
    couponCode?: string;
    shippingAddress: string;
    paymentMethod: string;
}

export interface ApiResponse<T> {
    success: boolean;
    data?: T;
    message?: string;
    error?: string;
}
