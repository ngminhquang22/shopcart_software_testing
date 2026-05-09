import { useMemo, useState } from 'react';

import type { CartItem } from '../types';
import { calculateCartTotal, calculateOrderPrice } from '../utils/priceCalculation';

export interface PriceCalculatorProps {
    items: CartItem[];
    shippingFee?: number;
    onApplyCoupon?: (couponCode: string) => void;
}

export default function PriceCalculator({ items, shippingFee = 0, onApplyCoupon }: PriceCalculatorProps) {
    const [couponInput, setCouponInput] = useState('');
    const [activeCoupon, setActiveCoupon] = useState<string | null>(null);

    const subtotal = useMemo(() => calculateCartTotal(items), [items]);
    const total = useMemo(
        () => calculateOrderPrice(items, activeCoupon, shippingFee),
        [items, activeCoupon, shippingFee],
    );

    const handleApplyCoupon = () => {
        const normalized = couponInput.trim();
        setActiveCoupon(normalized || null);
        onApplyCoupon?.(normalized);
    };

    return (
        <div className="space-y-3 rounded border border-slate-200 p-4">
            <div className="flex items-center justify-between text-sm">
                <span className="text-slate-600">Tam tinh</span>
                <span className="font-semibold text-slate-800" data-testid="subtotal-display">
                    {subtotal.toLocaleString('vi-VN')}
                </span>
            </div>

            <div className="flex flex-wrap items-center gap-2">
                <input
                    className="w-40 rounded border border-slate-300 px-2 py-1 text-sm"
                    data-testid="coupon-input"
                    placeholder="Ma giam gia"
                    value={couponInput}
                    onChange={(event) => setCouponInput(event.target.value)}
                />
                <button
                    className="rounded bg-slate-900 px-3 py-1 text-sm font-semibold text-white"
                    data-testid="apply-coupon-btn"
                    type="button"
                    onClick={handleApplyCoupon}
                >
                    Ap dung
                </button>
            </div>

            <div className="flex items-center justify-between text-sm">
                <span className="text-slate-600">Tong cong</span>
                <span className="text-base font-semibold text-slate-900" data-testid="total-display">
                    {total.toLocaleString('vi-VN')}
                </span>
            </div>
        </div>
    );
}
