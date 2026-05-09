import type { CartItem, Product } from '../types';

const DEFAULT_COUPONS: Record<string, number> = {
    SAVE5: 5,
    SAVE10: 10,
    SAVE15: 15,
    SAVE20: 20,
};

function parseFixedDiscount(couponCode: string): number {
    const normalized = couponCode.trim().toUpperCase();
    if (!normalized) {
        return 0;
    }

    const match = normalized.match(/^(?:FIXED|FLAT)(\d+)(K)?$/);
    if (!match) {
        return 0;
    }

    const amount = Number(match[1]);
    if (!Number.isFinite(amount) || amount <= 0) {
        return 0;
    }

    return match[2] ? amount * 1000 : amount;
}

export function calculateCartTotal(items: CartItem[]): number {
    return items.reduce((total, item) => total + item.price * item.quantity, 0);
}

export function calculateOrderPrice(
    items: CartItem[],
    couponCode: string | null | undefined,
    shippingFee: number,
): number {
    const subtotal = calculateCartTotal(items);
    const normalizedCoupon = couponCode?.trim().toUpperCase() ?? '';
    const discountPercent = normalizedCoupon ? DEFAULT_COUPONS[normalizedCoupon] ?? 0 : 0;
    const fixedDiscount = normalizedCoupon ? parseFixedDiscount(normalizedCoupon) : 0;
    const discountAmount = subtotal * (discountPercent / 100) + fixedDiscount;

    return Math.max(subtotal - discountAmount + shippingFee, 0);
}

export function checkInventoryAvailability(items: Array<{ productId: string; quantity: number }>, productList: Product[]): boolean {
    return items.every((item) => {
        const product = productList.find((candidate) => candidate.id === item.productId);

        return Boolean(product && item.quantity > 0 && item.quantity <= product.stock);
    });
}
