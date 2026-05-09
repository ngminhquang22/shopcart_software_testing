import type { CartItem } from '../types';

export interface AddToCartRequest {
    item: CartItem;
}

const cartStore = new Map<string, CartItem[]>();

function delay<T>(result: T, time = 300): Promise<T> {
    return new Promise((resolve) => {
        setTimeout(() => resolve(result), time);
    });
}

export async function addToCart(userId: string, request: AddToCartRequest): Promise<CartItem[]> {
    const currentCart = cartStore.get(userId) ?? [];
    const existingItemIndex = currentCart.findIndex((item) => item.id === request.item.id);

    if (existingItemIndex >= 0) {
        const updatedCart = currentCart.map((item, index) =>
            index === existingItemIndex
                ? { ...item, quantity: item.quantity + request.item.quantity }
                : item,
        );

        cartStore.set(userId, updatedCart);
        return delay(updatedCart);
    }

    const updatedCart = [...currentCart, request.item];
    cartStore.set(userId, updatedCart);

    return delay(updatedCart);
}

export async function getCart(userId: string): Promise<CartItem[]> {
    return delay(cartStore.get(userId) ?? []);
}
