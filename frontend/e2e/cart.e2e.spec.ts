import { expect, test } from '@playwright/test';

import { CartPage } from './pages/CartPage';

test('cart flow adds item', async ({ page }) => {
    await page.goto('http://localhost:5173');

    const cartPage = new CartPage(page);

    await cartPage.addToCart(2);

    await expect(cartPage.cartBadge).toHaveText('2');
    await expect(cartPage.successToast).toHaveText('them vao gio thanh cong');
});
