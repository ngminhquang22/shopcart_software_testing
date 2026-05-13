import { useState, useEffect } from 'react';
import { cartApiService } from '../services/cartApiService';
import { getProductById } from '../services/productService';
import type { CartItemResponse, Product } from '../types';
import './CartPage.css';

interface CartPageProps {
    userId: string;
}

export function CartPage({ userId }: CartPageProps) {
    const [cartItems, setCartItems] = useState<CartItemResponse[]>([]);
    const [products, setProducts] = useState<Record<string, Product>>({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [removingId, setRemovingId] = useState<string | null>(null);

    useEffect(() => {
        loadCart();
    }, [userId]);

    const loadCart = async () => {
        setLoading(true);
        setError(null);
        try {
            const items = await cartApiService.getCartItems(userId);
            setCartItems(items);

            // Load product details
            const prods: Record<string, Product> = {};
            items.forEach((item) => {
                const product = getProductById(item.productId);
                if (product) {
                    prods[item.productId] = product;
                }
            });
            setProducts(prods);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to load cart');
        } finally {
            setLoading(false);
        }
    };

    const handleRemoveItem = async (cartItemId: string, productId: string) => {
        setRemovingId(cartItemId);
        try {
            await cartApiService.removeFromCart(userId, productId);
            setCartItems(cartItems.filter((item) => item.cartItemId !== cartItemId));
            alert('✓ Item removed from cart');
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to remove item');
        } finally {
            setRemovingId(null);
        }
    };

    const handleUpdateQuantity = async (
        cartItemId: string,
        productId: string,
        newQuantity: number
    ) => {
        if (newQuantity < 1) return;
        try {
            await cartApiService.updateQuantity(userId, {
                userId,
                productId,
                quantity: newQuantity,
            });
            setCartItems(
                cartItems.map((item) =>
                    item.cartItemId === cartItemId
                        ? { ...item, quantity: newQuantity }
                        : item
                )
            );
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to update quantity');
        }
    };

    const totalPrice = cartItems.reduce((sum, item) => {
        const product = products[item.productId];
        return sum + (product ? product.price * item.quantity : 0);
    }, 0);

    if (loading) return <div className="cart-container"><p>Loading cart...</p></div>;

    return (
        <div className="cart-container">
            <h2>🛒 Shopping Cart</h2>
            {error && <div className="error-message">{error}</div>}

            {cartItems.length === 0 ? (
                <p className="empty-cart">Your cart is empty. Start shopping!</p>
            ) : (
                <>
                    <div className="cart-items">
                        {cartItems.map((item) => {
                            const product = products[item.productId];
                            const itemTotal = product ? product.price * item.quantity : 0;

                            return (
                                <div key={item.cartItemId} className="cart-item">
                                    <div className="item-info">
                                        <h4>{product?.name || 'Unknown Product'}</h4>
                                        <p className="item-id">{item.productId}</p>
                                        {product && (
                                            <p className="item-price">
                                                {new Intl.NumberFormat('vi-VN', {
                                                    style: 'currency',
                                                    currency: 'VND',
                                                }).format(product.price)}
                                            </p>
                                        )}
                                    </div>
                                    <div className="item-actions">
                                        <div className="quantity-control">
                                            <button
                                                onClick={() =>
                                                    handleUpdateQuantity(
                                                        item.cartItemId,
                                                        item.productId,
                                                        item.quantity - 1
                                                    )
                                                }
                                                className="qty-btn"
                                            >
                                                −
                                            </button>
                                            <span className="qty-value">{item.quantity}</span>
                                            <button
                                                onClick={() =>
                                                    handleUpdateQuantity(
                                                        item.cartItemId,
                                                        item.productId,
                                                        item.quantity + 1
                                                    )
                                                }
                                                className="qty-btn"
                                            >
                                                +
                                            </button>
                                        </div>
                                        <div className="item-total">
                                            {new Intl.NumberFormat('vi-VN', {
                                                style: 'currency',
                                                currency: 'VND',
                                            }).format(itemTotal)}
                                        </div>
                                        <button
                                            onClick={() =>
                                                handleRemoveItem(item.cartItemId, item.productId)
                                            }
                                            disabled={removingId === item.cartItemId}
                                            className="remove-btn"
                                        >
                                            {removingId === item.cartItemId ? '🗑️...' : '🗑️ Remove'}
                                        </button>
                                    </div>
                                </div>
                            );
                        })}
                    </div>

                    <div className="cart-summary">
                        <h3>Subtotal:</h3>
                        <p className="subtotal-price">
                            {new Intl.NumberFormat('vi-VN', {
                                style: 'currency',
                                currency: 'VND',
                            }).format(totalPrice)}
                        </p>
                        <p className="items-count">
                            {cartItems.length} {cartItems.length === 1 ? 'item' : 'items'}
                        </p>
                    </div>
                </>
            )}
        </div>
    );
}
