import type { CartItem } from '../types';

export interface CheckStockResponse {
    available: boolean;
}

function delay<T>(result: T, time = 300): Promise<T> {
    return new Promise((resolve) => {
        setTimeout(() => resolve(result), time);
    });
}

export async function checkStock(items: CartItem[]): Promise<CheckStockResponse> {
    const available = items.every((item) => item.quantity > 0 && item.quantity <= item.stock);

    return delay({ available });
}
