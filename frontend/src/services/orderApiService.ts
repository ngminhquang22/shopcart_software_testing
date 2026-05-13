// Order API Service - Communication with backend
import { apiRequest } from './apiClient';
import type { OrderRequest, OrderResponse } from '../types';

export const orderApiService = {
    // Create new order
    createOrder: async (userId: string, request: OrderRequest): Promise<OrderResponse> => {
        return apiRequest<OrderResponse>('POST', `/orders/${userId}`, request);
    },

    // Get all orders for a user
    getOrdersByUser: async (userId: string): Promise<OrderResponse[]> => {
        return apiRequest<OrderResponse[]>('GET', `/orders/${userId}`);
    },

    // Get order by ID
    getOrderById: async (orderId: string): Promise<OrderResponse> => {
        return apiRequest<OrderResponse>('GET', `/orders/detail/${orderId}`);
    },

    // Update order status
    updateOrderStatus: async (
        orderId: string,
        status: string
    ): Promise<OrderResponse> => {
        return apiRequest<OrderResponse>(
            'PUT',
            `/orders/${orderId}/status?status=${status}`
        );
    },

    // Cancel order
    cancelOrder: async (orderId: string): Promise<void> => {
        return apiRequest<void>('DELETE', `/orders/${orderId}`);
    },
};
