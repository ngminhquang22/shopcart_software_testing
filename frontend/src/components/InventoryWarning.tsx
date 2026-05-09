import { useEffect, useState } from 'react';

import type { CartItem } from '../types';
import { checkStock } from '../services/inventoryService';

export interface InventoryWarningProps {
    items: CartItem[];
}

export default function InventoryWarning({ items }: InventoryWarningProps) {
    const [available, setAvailable] = useState(true);

    useEffect(() => {
        let isMounted = true;

        checkStock(items).then((response) => {
            if (isMounted) {
                setAvailable(response.available);
            }
        });

        return () => {
            isMounted = false;
        };
    }, [items]);

    if (available) {
        return null;
    }

    return (
        <div
            className="rounded border border-amber-300 bg-amber-50 px-3 py-2 text-sm text-amber-800"
            data-testid="inventory-warning"
        >
            So luong san pham vuot qua ton kho.
        </div>
    );
}
