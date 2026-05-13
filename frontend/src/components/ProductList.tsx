import { useState, useEffect } from 'react';
import { getProducts } from '../services/productService';
import { cartApiService } from '../services/cartApiService';
import type { Product, CartItemRequest } from '../types';
import './ProductList.css';

interface ProductListProps {
    userId: string;
    onAddToCart: (product: Product) => void;
}

export function ProductList({ userId, onAddToCart }: ProductListProps) {
    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [quantity, setQuantity] = useState<Record<string, number>>({});

    useEffect(() => {
        const prods = getProducts();
        setProducts(prods);
        // Initialize quantities
        const qtys: Record<string, number> = {};
        prods.forEach((p) => {
            qtys[p.productId] = 1;
        });
        setQuantity(qtys);
    }, []);

    const handleAddToCart = async (product: Product) => {
        setLoading(true);
        setError(null);
        try {
            const qty = quantity[product.productId] || 1;
            const request: CartItemRequest = {
                userId,
                productId: product.productId,
                quantity: qty,
            };
            await cartApiService.addToCart(request);
            onAddToCart(product);
            alert(`✓ Added ${product.name} (x${qty}) to cart`);
            // Reset quantity after adding
            setQuantity({ ...quantity, [product.productId]: 1 });
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to add to cart');
        } finally {
            setLoading(false);
        }
    };

    const handleQuantityChange = (productId: string, qty: number) => {
        setQuantity({ ...quantity, [productId]: Math.max(1, qty) });
    };

    return (
        <div className="product-list-container">
            <h2>🏃 Sneaker Collection</h2>
            {error && <div className="error-message">{error}</div>}
            <div className="products-grid">
                {products.map((product) => (
                    <div key={product.productId} className="product-card">
                        <div className="product-header">
                            <h3>{product.name}</h3>
                            <span className="product-id">{product.productId}</span>
                        </div>
                        <div className="product-price">
                            {new Intl.NumberFormat('vi-VN', {
                                style: 'currency',
                                currency: 'VND',
                            }).format(product.price)}
                        </div>
                        <div className="product-status">{product.status}</div>
                        <div className="product-actions">
                            <input
                                type="number"
                                min="1"
                                value={quantity[product.productId] || 1}
                                onChange={(e) =>
                                    handleQuantityChange(product.productId, parseInt(e.target.value))
                                }
                                className="quantity-input"
                            />
                            <button
                                onClick={() => handleAddToCart(product)}
                                disabled={loading}
                                className="add-to-cart-btn"
                            >
                                {loading ? '⏳ Adding...' : '🛒 Add to Cart'}
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
