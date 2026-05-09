import { useEffect, useState } from 'react';

import type { CartItem } from '../types';
import CheckoutSummary from '../components/CheckoutSummary';
import InventoryWarning from '../components/InventoryWarning';
import PriceCalculator from '../components/PriceCalculator';
import { getCart } from '../services/cartService';
import { createOrder } from '../services/orderService';

const DEFAULT_USER_ID = 'user01';
const DEFAULT_SHIPPING_FEE = 0;

export interface CheckoutPageProps {
    initialItems?: CartItem[];
}

export default function CheckoutPage({ initialItems = [] }: CheckoutPageProps) {
    const [cartItems, setCartItems] = useState<CartItem[]>(initialItems);
    const [couponCode, setCouponCode] = useState<string>('');
    const [orderSuccess, setOrderSuccess] = useState(false);
    const [showCheckout, setShowCheckout] = useState(true);

    useEffect(() => {
        if (initialItems.length > 0) {
            return undefined;
        }

        let isMounted = true;

        getCart(DEFAULT_USER_ID).then((items) => {
            if (isMounted) {
                setCartItems(items);
            }
        });

        return () => {
            isMounted = false;
        };
    }, [initialItems]);

    const handlePlaceOrder = async () => {
        await createOrder({
            items: cartItems,
            couponCode: couponCode || null,
            shippingFee: DEFAULT_SHIPPING_FEE,
        });

        setOrderSuccess(true);
    };

    return (
        <section className="mx-auto flex w-full max-w-3xl flex-col gap-6 p-4">
            <button
                className="w-fit rounded bg-slate-900 px-4 py-2 text-sm font-semibold text-white"
                data-testid="checkout-btn"
                type="button"
                onClick={() => setShowCheckout(true)}
            >
                Di toi trang checkout
            </button>

            {showCheckout ? (
                <div className="grid gap-6 lg:grid-cols-[1.2fr_0.8fr]">
                    <div className="flex flex-col gap-4">
                        <h1 className="text-xl font-semibold text-slate-800">Checkout</h1>
                        <CheckoutSummary items={cartItems} />
                        <InventoryWarning items={cartItems} />
                    </div>

                    <div className="flex flex-col gap-4">
                        <PriceCalculator
                            items={cartItems}
                            shippingFee={DEFAULT_SHIPPING_FEE}
                            onApplyCoupon={(code) => setCouponCode(code)}
                        />
                        <button
                            className="rounded bg-emerald-600 px-4 py-2 text-sm font-semibold text-white"
                            data-testid="place-order-btn"
                            type="button"
                            onClick={handlePlaceOrder}
                        >
                            Dat hang
                        </button>
                        {orderSuccess ? (
                            <div className="rounded bg-emerald-50 px-3 py-2 text-sm text-emerald-700" data-testid="order-success">
                                dat hang thanh cong
                            </div>
                        ) : null}
                    </div>
                </div>
            ) : null}
        </section>
    );
}
