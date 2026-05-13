// Cart API Service - Communication with backend
import { apiRequest } from './apiClient';
import type { CartItemRequest, CartItemResponse } from '../types';

export const cartApiService = {
    // Get all cart items for a user
    getCartItems: async (userId: string): Promise<CartItemResponse[]> => {
        return apiRequest<CartItemResponse[]>('GET', `/cart/${userId}/items`);
    },

    // Add item to cart
    addToCart: async (request: CartItemRequest): Promise<CartItemResponse> => {
        return apiRequest<CartItemResponse>('POST', '/cart/add', request);
    },

    // Update cart item quantity
    updateQuantity: async (
        userId: string,
        request: CartItemRequest
    ): Promise<CartItemResponse> => {
        return apiRequest<CartItemResponse>('PUT', `/cart/${userId}/items`, request);
    },

    // Remove item from cart
    removeFromCart: async (userId: string, productId: string): Promise<void> => {
        return apiRequest<void>('DELETE', `/cart/${userId}/items/${productId}`);
    },
};
