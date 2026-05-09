import { describe, expect, it } from 'vitest';

import type { CartItem, Product } from '../src/types';
import {
    calculateCartTotal,
    calculateOrderPrice,
    checkInventoryAvailability,
} from '../src/utils/priceCalculation';

describe('price calculation utilities', () => {
    const baseItems: CartItem[] = [
        {
            id: 'P001',
            name: 'Ao',
            price: 10000,
            stock: 10,
            quantity: 2,
        },
        {
            id: 'P002',
            name: 'Quan',
            price: 25000,
            stock: 5,
            quantity: 1,
        },
    ];

    it('calculateCartTotal: gio hang rong', () => {
        expect(calculateCartTotal([])).toBe(0);
    });

    it('calculateCartTotal: nhieu san pham', () => {
        expect(calculateCartTotal(baseItems)).toBe(45000);
    });

    it('calculateOrderPrice: khong giam gia', () => {
        expect(calculateOrderPrice(baseItems, null, 0)).toBe(45000);
    });

    it('calculateOrderPrice: ap dung ma giam gia', () => {
        expect(calculateOrderPrice(baseItems, 'SAVE10', 0)).toBe(40500);
    });

    it('calculateOrderPrice: ap dung ma giam gia 20%', () => {
        expect(calculateOrderPrice(baseItems, 'SAVE20', 0)).toBe(36000);
    });

    it('calculateCartTotal: tong gia sau khi xoa san pham', () => {
        const updatedItems = baseItems.filter((item) => item.id !== 'P002');

        expect(calculateCartTotal(updatedItems)).toBe(20000);
    });

    it('calculateOrderPrice: giam gia co dinh', () => {
        const items: CartItem[] = [
            {
                id: 'P010',
                name: 'Tui',
                price: 80000,
                stock: 10,
                quantity: 1,
            },
            {
                id: 'P011',
                name: 'Giay',
                price: 70000,
                stock: 10,
                quantity: 1,
            },
        ];

        expect(calculateOrderPrice(items, 'FLAT50000', 0)).toBe(100000);
    });

    it('calculateOrderPrice: co phi van chuyen', () => {
        expect(calculateOrderPrice(baseItems, null, 15000)).toBe(60000);
    });

    it('calculateOrderPrice: tong cuoi cung (subtotal + shipping - discount)', () => {
        expect(calculateOrderPrice(baseItems, 'SAVE10', 5000)).toBe(45500);
    });

    it('checkInventoryAvailability: du hang va thieu hang', () => {
        const productList: Product[] = [
            { id: 'P001', name: 'Ao', price: 10000, stock: 3 },
            { id: 'P002', name: 'Quan', price: 25000, stock: 1 },
        ];

        const okItems = [
            { productId: 'P001', quantity: 2 },
            { productId: 'P002', quantity: 1 },
        ];
        const badItems = [
            { productId: 'P001', quantity: 4 },
            { productId: 'P002', quantity: 1 },
        ];

        expect(checkInventoryAvailability(okItems, productList)).toBe(true);
        expect(checkInventoryAvailability(badItems, productList)).toBe(false);
    });
});
