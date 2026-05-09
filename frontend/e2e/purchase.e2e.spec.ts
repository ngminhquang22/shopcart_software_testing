import { expect, test } from '@playwright/test';

import { CartPage } from './pages/CartPage';
import { CheckoutPage } from './pages/CheckoutPage';

test('purchase flow places order', async ({ page }) => {
    await page.goto('http://localhost:5173');

    const cartPage = new CartPage(page);
    await cartPage.addToCart(1);

    const checkoutPage = new CheckoutPage(page);
    await checkoutPage.goToCheckout();
    await checkoutPage.placeOrder();

    await expect(checkoutPage.successMessage).toHaveText('dat hang thanh cong');
});
