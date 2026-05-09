import type { Locator, Page } from '@playwright/test';

export class CartPage {
    readonly page: Page;
    readonly quantityInput: Locator;
    readonly addToCartBtn: Locator;
    readonly successToast: Locator;
    readonly cartBadge: Locator;

    constructor(page: Page) {
        this.page = page;
        this.quantityInput = page.locator('[data-testid="quantity-input"]');
        this.addToCartBtn = page.locator('[data-testid="add-to-cart-btn"]');
        this.successToast = page.locator('[data-testid="success-toast"]');
        this.cartBadge = page.locator('[data-testid="cart-badge"]');
    }

    async addToCart(quantity: number | string): Promise<void> {
        await this.quantityInput.fill(String(quantity));
        await this.addToCartBtn.click();
    }
}
