import type { CartItem, OrderResponse } from '../types';
import { calculateOrderPrice } from '../utils/priceCalculation';

export interface CreateOrderRequest {
    items: CartItem[];
    couponCode?: string | null;
    shippingFee?: number;
}

function delay<T>(result: T, time = 300): Promise<T> {
    return new Promise((resolve) => {
        setTimeout(() => resolve(result), time);
    });
}

export async function createOrder(request: CreateOrderRequest): Promise<OrderResponse> {
    const totalPrice = calculateOrderPrice(
        request.items,
        request.couponCode,
        request.shippingFee ?? 0,
    );

    return delay({
        orderId: 'ORD-001',
        status: 'PENDING',
        totalPrice,
    });
}
