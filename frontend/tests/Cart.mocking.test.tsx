import React from 'react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import CartComponent from '../src/components/CartComponent';
import { addToCart, getCart } from '../src/services/cartService';

vi.mock('../src/services/cartService', () => ({
    addToCart: vi.fn(),
    getCart: vi.fn(),
}));

describe('Cart Mock Tests', () => {
    const mockedAddToCart = vi.mocked(addToCart);
    const mockedGetCart = vi.mocked(getCart);

    beforeEach(() => {
        vi.clearAllMocks();
        mockedGetCart.mockResolvedValue([]);
    });

    it('TC1: goi getCart khi component mount', async () => {
        render(<CartComponent userId="user01" />);

        await screen.findByTestId('empty-cart-message');
        expect(mockedGetCart).toHaveBeenCalledTimes(1);
        expect(mockedGetCart).toHaveBeenCalledWith('user01');
    });

    it('TC2: khong goi addToCart neu du lieu khong hop le', async () => {
        const user = userEvent.setup();

        render(<CartComponent userId="user01" />);

        const input = screen.getByTestId('quantity-input');
        const button = screen.getByTestId('add-to-cart-btn');

        await user.clear(input);
        await user.type(input, '0');
        await user.click(button);

        expect(mockedAddToCart).not.toHaveBeenCalled();
        expect(screen.getByText('Số lượng phải lớn hơn 0')).toBeInTheDocument();
    });

    it('TC3: mock them san pham thanh cong va verify mock calls', async () => {
        const user = userEvent.setup();
        mockedAddToCart.mockResolvedValue([
            {
                id: 'P001',
                name: 'Sample Product',
                price: 10000,
                stock: 10,
                quantity: 2,
            },
        ]);

        render(<CartComponent userId="user01" />);

        const input = screen.getByTestId('quantity-input');
        const button = screen.getByTestId('add-to-cart-btn');

        await user.clear(input);
        await user.type(input, '2');
        await user.click(button);

        expect(mockedAddToCart).toHaveBeenCalledTimes(1);
        expect(mockedAddToCart).toHaveBeenCalledWith('user01', {
            item: {
                id: 'P001',
                productId: 'P001',
                name: 'Sample Product',
                price: 10000,
                stock: 10,
                quantity: 2,
            },
        });

        expect(await screen.findByTestId('success-toast')).toHaveTextContent('them vao gio thanh cong');
        expect(screen.getByTestId('cart-badge')).toHaveTextContent('2');
    });

    it('TC4: mock them san pham that bai va verify error handling', async () => {
        const user = userEvent.setup();
        mockedAddToCart.mockRejectedValue(new Error('API failed'));

        render(<CartComponent userId="user01" />);

        const input = screen.getByTestId('quantity-input');
        const button = screen.getByTestId('add-to-cart-btn');

        await user.clear(input);
        await user.type(input, '2');
        await user.click(button);

        expect(mockedAddToCart).toHaveBeenCalledTimes(1);
        expect(await screen.findByText('them vao gio that bai')).toBeInTheDocument();
        expect(screen.queryByTestId('success-toast')).toBeNull();
    });

    it('TC5: sau khi that bai, lan submit thanh cong se hien success va xoa error', async () => {
        const user = userEvent.setup();
        mockedAddToCart.mockRejectedValueOnce(new Error('API failed'));
        mockedAddToCart.mockResolvedValueOnce([
            {
                id: 'P001',
                name: 'Sample Product',
                price: 10000,
                stock: 10,
                quantity: 1,
            },
        ]);

        render(<CartComponent userId="user01" />);

        const input = screen.getByTestId('quantity-input');
        const button = screen.getByTestId('add-to-cart-btn');

        await user.clear(input);
        await user.type(input, '1');
        await user.click(button);
        expect(await screen.findByText('them vao gio that bai')).toBeInTheDocument();

        await user.click(button);
        expect(await screen.findByTestId('success-toast')).toHaveTextContent('them vao gio thanh cong');
        expect(screen.queryByText('them vao gio that bai')).toBeNull();
        expect(mockedAddToCart).toHaveBeenCalledTimes(2);
    });
});
