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

describe('CartComponent rendering & interactions', () => {
    const mockedGetCart = vi.mocked(getCart);

    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('TC1: render gio hang trong', async () => {
        mockedGetCart.mockResolvedValue([]);

        render(<CartComponent userId="user01" />);

        expect(await screen.findByTestId('empty-cart-message')).toBeInTheDocument();
        expect(screen.getByTestId('cart-badge')).toHaveTextContent('0');
    });

    it('TC2: nguoi dung nhap so luong', async () => {
        const user = userEvent.setup();
        mockedGetCart.mockResolvedValue([]);

        render(<CartComponent userId="user01" />);

        const input = screen.getByTestId('quantity-input') as HTMLInputElement;

        await user.clear(input);
        await user.type(input, '3');

        expect(input.value).toBe('3');
    });
});

describe('CartComponent error handling & success messages', () => {
    const mockedAddToCart = vi.mocked(addToCart);
    const mockedGetCart = vi.mocked(getCart);

    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('TC1: hien thi loi khi so luong khong hop le', async () => {
        const user = userEvent.setup();
        mockedGetCart.mockResolvedValue([]);

        render(<CartComponent userId="user01" />);

        const input = screen.getByTestId('quantity-input');
        const button = screen.getByTestId('add-to-cart-btn');

        await user.clear(input);
        await user.type(input, '0');
        await user.click(button);

        expect(screen.getByText('Số lượng phải lớn hơn 0')).toBeInTheDocument();
        expect(screen.queryByTestId('success-toast')).toBeNull();
    });

    it('TC2: hien thi success message khi them vao gio', async () => {
        const user = userEvent.setup();
        mockedGetCart.mockResolvedValue([]);
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

        const successToast = await screen.findByTestId('success-toast');
        expect(successToast).toHaveTextContent('them vao gio thanh cong');
    });
});

describe('CartComponent mocking addToCart', () => {
    const mockedAddToCart = vi.mocked(addToCart);
    const mockedGetCart = vi.mocked(getCart);

    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('TC1: mock successful response va verify mock calls', async () => {
        const user = userEvent.setup();
        mockedGetCart.mockResolvedValue([]);
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
                name: 'Sample Product',
                price: 10000,
                stock: 10,
                quantity: 2,
            },
        });

        expect(await screen.findByTestId('success-toast')).toHaveTextContent('them vao gio thanh cong');
    });

    it('TC2: mock failed response va verify mock calls', async () => {
        const user = userEvent.setup();
        mockedGetCart.mockResolvedValue([]);
        mockedAddToCart.mockRejectedValue(new Error('API failed'));

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
                name: 'Sample Product',
                price: 10000,
                stock: 10,
                quantity: 2,
            },
        });

        expect(await screen.findByText('them vao gio that bai')).toBeInTheDocument();
        expect(screen.queryByTestId('success-toast')).toBeNull();
    });
});
