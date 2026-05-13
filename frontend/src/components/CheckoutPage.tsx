import { useEffect, useState } from 'react';
import { orderApiService } from '../services/orderApiService';
import { cartApiService } from '../services/cartApiService';
import type { OrderRequest, CartItemResponse, OrderItem } from '../types';
import { getProductById } from '../services/productService';
import './CheckoutPage.css';

interface CheckoutPageProps {
    userId: string;
}

export function CheckoutPage({ userId }: CheckoutPageProps) {
    const [step, setStep] = useState<'items' | 'details'>('items');
    const [cartItems, setCartItems] = useState<CartItemResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState(false);

    // Form data
    const [couponCode, setCouponCode] = useState('WELCOME10');
    const [shippingFee, setShippingFee] = useState(50000);
    const [shippingAddress, setShippingAddress] = useState(
        '123 Main Street, Ho Chi Minh City'
    );
    const [paymentMethod, setPaymentMethod] = useState('COD');
    const [order, setOrder] = useState<any>(null);

    // Load cart items on mount
    useEffect(() => {
        loadCartItems();
    }, [userId]);

    const loadCartItems = async () => {
        setLoading(true);
        setError(null);
        try {
            const items = await cartApiService.getCartItems(userId);
            setCartItems(items);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to load cart');
        } finally {
            setLoading(false);
        }
    };

    const calculateSubtotal = () => {
        return cartItems.reduce((sum, item) => {
            const product = getProductById(item.productId);
            return sum + (product ? product.price * item.quantity : 0);
        }, 0);
    };

    const calculateDiscount = () => {
        if (couponCode === 'WELCOME10') {
            return calculateSubtotal() * 0.1;
        } else if (couponCode === 'SAVE100K') {
            return 100000;
        } else if (couponCode === 'SUMMER15') {
            return calculateSubtotal() * 0.15;
        }
        return 0;
    };

    const subtotal = calculateSubtotal();
    const discount = calculateDiscount();
    const total = Math.max(0, subtotal - discount) + shippingFee;

    const handleCheckout = async () => {
        if (!shippingAddress.trim()) {
            setError('Please enter shipping address');
            return;
        }

        setSubmitting(true);
        setError(null);
        try {
            const items: OrderItem[] = cartItems.map((item) => ({
                userId,
                productId: item.productId,
                quantity: item.quantity,
            }));

            const orderRequest: OrderRequest = {
                userId,
                items,
                couponCode,
                shippingFee,
                shippingAddress,
                paymentMethod,
            };

            const result = await orderApiService.createOrder(userId, orderRequest);
            setOrder(result);
            alert(`✓ Order created successfully!\nOrder ID: ${result.orderId}`);
            setStep('items'); // Reset to start
            setCartItems([]);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to create order');
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) return <div className="checkout-container"><p>Loading...</p></div>;

    if (order) {
        return (
            <div className="checkout-container">
                <div className="order-success">
                    <h2>✓ Order Confirmed!</h2>
                    <div className="order-details">
                        <p>
                            <strong>Order ID:</strong> {order.orderId}
                        </p>
                        <p>
                            <strong>Status:</strong> {order.status}
                        </p>
                        <p>
                            <strong>Total:</strong>{' '}
                            {new Intl.NumberFormat('vi-VN', {
                                style: 'currency',
                                currency: 'VND',
                            }).format(order.totalPrice)}
                        </p>
                        <p>
                            <strong>Shipping Address:</strong> {order.shippingAddress}
                        </p>
                        <p>
                            <strong>Payment Method:</strong> {order.paymentMethod}
                        </p>
                    </div>
                    <button onClick={() => setOrder(null)} className="continue-btn">
                        Continue Shopping
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="checkout-container">
            <h2>📦 Checkout</h2>
            {error && <div className="error-message">{error}</div>}

            {cartItems.length === 0 ? (
                <p className="empty-message">No items to checkout</p>
            ) : (
                <>
                    {step === 'items' && (
                        <>
                            <div className="checkout-items">
                                <h3>Order Items</h3>
                                {cartItems.map((item) => {
                                    const product = getProductById(item.productId);
                                    const itemTotal = product
                                        ? product.price * item.quantity
                                        : 0;
                                    return (
                                        <div key={item.cartItemId} className="checkout-item">
                                            <span className="item-name">{product?.name}</span>
                                            <span className="item-qty">x{item.quantity}</span>
                                            <span className="item-total">
                                                {new Intl.NumberFormat('vi-VN', {
                                                    style: 'currency',
                                                    currency: 'VND',
                                                }).format(itemTotal)}
                                            </span>
                                        </div>
                                    );
                                })}
                            </div>

                            <button
                                onClick={() => setStep('details')}
                                className="next-btn"
                            >
                                Continue to Details →
                            </button>
                        </>
                    )}

                    {step === 'details' && (
                        <>
                            <div className="checkout-form">
                                <div className="form-group">
                                    <label>Coupon Code (Optional)</label>
                                    <select
                                        value={couponCode}
                                        onChange={(e) => setCouponCode(e.target.value)}
                                    >
                                        <option value="">No Coupon</option>
                                        <option value="WELCOME10">WELCOME10 (-10%)</option>
                                        <option value="SAVE100K">SAVE100K (-100K)</option>
                                        <option value="SUMMER15">SUMMER15 (-15%)</option>
                                    </select>
                                </div>

                                <div className="form-group">
                                    <label>Shipping Fee</label>
                                    <input
                                        type="number"
                                        value={shippingFee}
                                        onChange={(e) =>
                                            setShippingFee(Math.max(0, parseInt(e.target.value)))
                                        }
                                    />
                                </div>

                                <div className="form-group">
                                    <label>Shipping Address *</label>
                                    <textarea
                                        value={shippingAddress}
                                        onChange={(e) => setShippingAddress(e.target.value)}
                                        rows={3}
                                        placeholder="Enter your full shipping address"
                                    />
                                </div>

                                <div className="form-group">
                                    <label>Payment Method</label>
                                    <select
                                        value={paymentMethod}
                                        onChange={(e) => setPaymentMethod(e.target.value)}
                                    >
                                        <option value="COD">Cash on Delivery (COD)</option>
                                        <option value="BANK_TRANSFER">Bank Transfer</option>
                                        <option value="CREDIT_CARD">Credit Card</option>
                                    </select>
                                </div>
                            </div>

                            <div className="checkout-summary">
                                <div className="summary-row">
                                    <span>Subtotal:</span>
                                    <span>
                                        {new Intl.NumberFormat('vi-VN', {
                                            style: 'currency',
                                            currency: 'VND',
                                        }).format(subtotal)}
                                    </span>
                                </div>
                                {discount > 0 && (
                                    <div className="summary-row discount">
                                        <span>Discount ({couponCode}):</span>
                                        <span>
                                            -{' '}
                                            {new Intl.NumberFormat('vi-VN', {
                                                style: 'currency',
                                                currency: 'VND',
                                            }).format(discount)}
                                        </span>
                                    </div>
                                )}
                                <div className="summary-row">
                                    <span>Shipping:</span>
                                    <span>
                                        {new Intl.NumberFormat('vi-VN', {
                                            style: 'currency',
                                            currency: 'VND',
                                        }).format(shippingFee)}
                                    </span>
                                </div>
                                <div className="summary-row total">
                                    <span>Total:</span>
                                    <span>
                                        {new Intl.NumberFormat('vi-VN', {
                                            style: 'currency',
                                            currency: 'VND',
                                        }).format(total)}
                                    </span>
                                </div>
                            </div>

                            <div className="checkout-actions">
                                <button
                                    onClick={() => setStep('items')}
                                    className="back-btn"
                                >
                                    ← Back
                                </button>
                                <button
                                    onClick={handleCheckout}
                                    disabled={submitting}
                                    className="submit-btn"
                                >
                                    {submitting ? '⏳ Processing...' : '✓ Place Order'}
                                </button>
                            </div>
                        </>
                    )}
                </>
            )}
        </div>
    );
}
