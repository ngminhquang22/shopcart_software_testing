export interface CartValidationItem {
    productId: string;
    quantity: number;
    stock: number;
}

export function validateCartItem(item: CartValidationItem): string | null {
    if (!Number.isFinite(item.quantity) || item.quantity <= 0) {
        return 'Số lượng phải lớn hơn 0';
    }

    if (item.quantity > item.stock) {
        return 'So luong vuot qua ton kho.';
    }

    return null;
}
