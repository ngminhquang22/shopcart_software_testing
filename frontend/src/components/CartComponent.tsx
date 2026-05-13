import { useEffect, useMemo, useState } from 'react';

import type { CartItem, Product } from '../types';
import { validateCartItem } from '../utils/cartValidation';
import { addToCart, getCart } from '../services/cartService';

export interface CartComponentProps {
    userId: string;
}

const DEFAULT_PRODUCT: Product = {
    id: 'P001',
    productId: 'P001',
    name: 'Sample Product',
    price: 10000,
    stock: 10,
};

export default function CartComponent({ userId }: CartComponentProps) {
    const [cartItems, setCartItems] = useState<CartItem[]>([]);
    const [quantityInput, setQuantityInput] = useState<string>('1');
    const [successMessage, setSuccessMessage] = useState<string>('');
    const [errorMessage, setErrorMessage] = useState<string>('');

    useEffect(() => {
        let isMounted = true;

        getCart(userId).then((items) => {
            if (isMounted) {
                setCartItems(items);
            }
        });

        return () => {
            isMounted = false;
        };
    }, [userId]);

    const totalQuantity = useMemo(
        () => cartItems.reduce((total, item) => total + item.quantity, 0),
        [cartItems],
    );

    const handleAddToCart = async () => {
        const quantity = Number(quantityInput);
        const validationError = validateCartItem({
            productId: DEFAULT_PRODUCT.id,
            quantity,
            stock: DEFAULT_PRODUCT.stock,
        });

        if (validationError) {
            setErrorMessage(validationError);
            setSuccessMessage('');
            return;
        }

        try {
            const updatedCart = await addToCart(userId, {
                item: { ...DEFAULT_PRODUCT, quantity },
            });

            setCartItems(updatedCart);
            setErrorMessage('');
            setSuccessMessage('them vao gio thanh cong');
        } catch {
            setSuccessMessage('');
            setErrorMessage('them vao gio that bai');
        }
    };

    return (
        <section className="mx-auto flex w-full max-w-2xl flex-col gap-4 rounded border border-slate-200 bg-white p-4 shadow-sm">
            <header className="flex items-center justify-between">
                <h2 className="text-lg font-semibold text-slate-800">Gio hang</h2>
                <span className="rounded-full bg-slate-900 px-3 py-1 text-xs font-semibold text-white" data-testid="cart-badge">
                    {totalQuantity}
                </span>
            </header>

            {cartItems.length === 0 ? (
                <div className="rounded border border-dashed border-slate-300 p-4 text-center text-sm text-slate-500" data-testid="empty-cart-message">
                    Gio hang dang trong.
                </div>
            ) : (
                <ul className="space-y-2">
                    {cartItems.map((item) => (
                        <li key={item.id} className="flex items-center justify-between rounded border border-slate-200 p-3 text-sm">
                            <div>
                                <p className="font-medium text-slate-800">{item.name}</p>
                                <p className="text-slate-500">So luong: {item.quantity}</p>
                            </div>
                            <p className="font-semibold text-slate-700">{item.price * item.quantity} VND</p>
                        </li>
                    ))}
                </ul>
            )}

            <div className="flex flex-col gap-3 rounded border border-slate-200 p-3">
                <p className="text-sm font-medium text-slate-700">Them san pham mac dinh</p>
                <div className="flex flex-wrap items-center gap-3">
                    <input
                        className="w-24 rounded border border-slate-300 px-2 py-1 text-sm"
                        data-testid="quantity-input"
                        type="number"
                        min={1}
                        max={DEFAULT_PRODUCT.stock}
                        value={quantityInput}
                        onChange={(event) => setQuantityInput(event.target.value)}
                    />
                    <button
                        className="rounded bg-slate-900 px-4 py-2 text-sm font-semibold text-white"
                        data-testid="add-to-cart-btn"
                        type="button"
                        onClick={handleAddToCart}
                    >
                        Them vao gio
                    </button>
                </div>
                {errorMessage ? <p className="text-sm text-red-600">{errorMessage}</p> : null}
                {successMessage ? (
                    <div className="rounded bg-emerald-50 px-3 py-2 text-sm text-emerald-700" data-testid="success-toast">
                        {successMessage}
                    </div>
                ) : null}
            </div>
        </section>
    );
}
