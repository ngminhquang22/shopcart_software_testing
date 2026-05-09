import { describe, expect, it } from 'vitest';

import { validateCartItem } from '../src/utils/cartValidation';

describe('validateCartItem', () => {
    const baseItem = { productId: 'P001', stock: 5 };

    it('TC1: quantity rong/null/undefined/khong phai so', () => {
        const invalidValues: Array<unknown> = [undefined, null, 'abc', Number.NaN];

        invalidValues.forEach((value) => {
            const result = validateCartItem({
                ...baseItem,
                quantity: value as number,
            });

            expect(result).toBe('Số lượng phải lớn hơn 0');
        });
    });

    it('TC2: quantity am hoac bang 0', () => {
        const invalidValues = [0, -1, -5];

        invalidValues.forEach((value) => {
            const result = validateCartItem({
                ...baseItem,
                quantity: value,
            });

            expect(result).toBe('Số lượng phải lớn hơn 0');
        });
    });

    it('TC3: quantity vuot ton kho', () => {
        const result = validateCartItem({
            ...baseItem,
            quantity: 10,
        });

        expect(result).toBe('So luong vuot qua ton kho.');
    });

    it('TC4: quantity hop le', () => {
        const result = validateCartItem({
            ...baseItem,
            quantity: 3,
        });

        expect(result).toBeNull();
    });
});
