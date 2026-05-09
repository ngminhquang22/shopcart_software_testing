import type { Locator, Page } from '@playwright/test';

export class CheckoutPage {
    readonly page: Page;
    readonly cartBadge: Locator;
    readonly checkoutBtn: Locator;
    readonly subtotalDisplay: Locator;
    readonly couponInput: Locator;
    readonly applyCouponBtn: Locator;
    readonly placeOrderBtn: Locator;
    readonly totalDisplay: Locator;
    readonly successMessage: Locator;

    constructor(page: Page) {
        this.page = page;
        this.cartBadge = page.locator('[data-testid="cart-badge"]');
        this.checkoutBtn = page.locator('[data-testid="checkout-btn"]');
        this.subtotalDisplay = page.locator('[data-testid="subtotal-display"]');
        this.couponInput = page.locator('[data-testid="coupon-input"]');
        this.applyCouponBtn = page.locator('[data-testid="apply-coupon-btn"]');
        this.placeOrderBtn = page.locator('[data-testid="place-order-btn"]');
        this.totalDisplay = page.locator('[data-testid="total-display"]');
        this.successMessage = page.locator('[data-testid="order-success"]');
    }

    async goToCheckout(): Promise<void> {
        await this.checkoutBtn.click();
    }

    async applyCoupon(code: string): Promise<void> {
        await this.couponInput.fill(code);
        await this.applyCouponBtn.click();
    }

    async placeOrder(): Promise<void> {
        await this.placeOrderBtn.click();
    }
}
