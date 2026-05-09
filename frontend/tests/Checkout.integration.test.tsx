import React from 'react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import type { CartItem } from '../src/types';
import CheckoutPage from '../src/pages/CheckoutPage';
import { checkStock } from '../src/services/inventoryService';
import { createOrder } from '../src/services/orderService';

vi.mock('../src/services/inventoryService', () => ({
    checkStock: vi.fn(),
}));

vi.mock('../src/services/orderService', () => ({
    createOrder: vi.fn(),
}));

describe('CheckoutPage integration', () => {
    const mockedCheckStock = vi.mocked(checkStock);
    const mockedCreateOrder = vi.mocked(createOrder);
    const mockCart: CartItem[] = [
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
            price: 15000,
            stock: 5,
            quantity: 1,
        },
    ];

    beforeEach(() => {
        vi.clearAllMocks();
        mockedCheckStock.mockResolvedValue({ available: true });
    });

    it('TC1: hien thi subtotal dung', () => {
        render(<CheckoutPage initialItems={mockCart} />);

        expect(screen.getByTestId('subtotal-display')).toHaveTextContent('35.000');
    });

    it('TC2: dat hang thanh cong', async () => {
        const user = userEvent.setup();
        mockedCreateOrder.mockResolvedValue({
            orderId: 'ORD-001',
            status: 'PENDING',
            totalPrice: 35000,
        });

        render(<CheckoutPage initialItems={mockCart} />);

        await user.click(screen.getByTestId('place-order-btn'));

        expect(mockedCreateOrder).toHaveBeenCalledTimes(1);
        expect(await screen.findByTestId('order-success')).toHaveTextContent('dat hang thanh cong');
    });
});
