import type { CartItem } from '../types';

export interface CheckoutSummaryProps {
    items: CartItem[];
}

export default function CheckoutSummary({ items }: CheckoutSummaryProps) {
    if (items.length === 0) {
        return (
            <div className="rounded border border-dashed border-slate-300 p-4 text-sm text-slate-500">
                Khong co san pham trong gio.
            </div>
        );
    }

    return (
        <ul className="space-y-2">
            {items.map((item) => (
                <li key={item.id} className="flex items-center justify-between rounded border border-slate-200 p-3 text-sm">
                    <div>
                        <p className="font-medium text-slate-800">{item.name}</p>
                        <p className="text-slate-500">So luong: {item.quantity}</p>
                    </div>
                    <p className="font-semibold text-slate-700">{item.price * item.quantity} VND</p>
                </li>
            ))}
        </ul>
    );
}
