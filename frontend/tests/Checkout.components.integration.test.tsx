import React from 'react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import type { CartItem } from '../src/types';
import CheckoutSummary from '../src/components/CheckoutSummary';
import PriceCalculator from '../src/components/PriceCalculator';
import InventoryWarning from '../src/components/InventoryWarning';
import { checkStock } from '../src/services/inventoryService';

vi.mock('../src/services/inventoryService', () => ({
    checkStock: vi.fn(),
}));

describe('Checkout components integration', () => {
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
            price: 25000,
            stock: 5,
            quantity: 1,
        },
    ];

    const mockedCheckStock = vi.mocked(checkStock);

    beforeEach(() => {
        vi.clearAllMocks();
        mockedCheckStock.mockResolvedValue({ available: true });
    });

    describe('a) CheckoutSummary component', () => {
        it('render danh sach san pham tu gio hang', () => {
            render(<CheckoutSummary items={mockCart} />);
            expect(screen.getByText('Ao')).toBeInTheDocument();
            expect(screen.getByText('Quan')).toBeInTheDocument();
            expect(screen.getByText('So luong: 2')).toBeInTheDocument();
            expect(screen.getByText('So luong: 1')).toBeInTheDocument();
            expect(screen.getByText('20000 VND')).toBeInTheDocument();
            expect(screen.getByText('25000 VND')).toBeInTheDocument();
        });
    });

    describe('b) PriceCalculator component (real-time)', () => {
        it('tinh dung subtotal, total va cap nhat khi ap dung coupon', async () => {
            const user = userEvent.setup();
            const onApplyCoupon = vi.fn();

            render(
                <PriceCalculator
                    items={mockCart}
                    shippingFee={5000}
                    onApplyCoupon={onApplyCoupon}
                />,
            );

            expect(screen.getByTestId('subtotal-display')).toHaveTextContent('45.000');
            expect(screen.getByTestId('total-display')).toHaveTextContent('50.000');

            await user.type(screen.getByTestId('coupon-input'), 'SAVE10');
            await user.click(screen.getByTestId('apply-coupon-btn'));

            expect(onApplyCoupon).toHaveBeenCalledWith('SAVE10');
            expect(screen.getByTestId('total-display')).toHaveTextContent('45.500');
        });
    });

    describe('c) InventoryWarning component', () => {
        it('hien thi canh bao khi checkStock tra ve available = false', async () => {
            mockedCheckStock.mockResolvedValue({ available: false });

            render(<InventoryWarning items={mockCart} />);

            const warning = await screen.findByTestId('inventory-warning');
            expect(warning).toHaveTextContent('So luong san pham vuot qua ton kho.');
            expect(mockedCheckStock).toHaveBeenCalledWith(mockCart);
        });

        it('khong hien thi canh bao khi con hang', async () => {
            mockedCheckStock.mockResolvedValue({ available: true });

            render(<InventoryWarning items={mockCart} />);

            expect(await screen.queryByTestId('inventory-warning')).toBeNull();
            expect(mockedCheckStock).toHaveBeenCalledWith(mockCart);
        });
    });
});
