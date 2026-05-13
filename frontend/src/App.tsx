import { useState, useEffect } from 'react';
import { ProductList } from './components/ProductList';
import { CartPage } from './components/CartPage';
import { CheckoutPage } from './components/CheckoutPage';
import { setApiConfig } from './services/apiClient';
import './App.css';

type Page = 'products' | 'cart' | 'checkout';

function App() {
  const [page, setPage] = useState<Page>('products');
  const [userId, setUserId] = useState('user-1');
  const [cartCount, setCartCount] = useState(0);

  useEffect(() => {
    setApiConfig({ userId });
  }, [userId]);

  const handleAddToCart = () => {
    setCartCount((prev) => prev + 1);
  };

  const handleUserChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setUserId(e.target.value);
    setCartCount(0);
  };

  return (
    <>
      <header className="app-header">
        <div className="header-container">
          <div className="header-left">
            <h1>🏪 ShoeCart - Premium Sneakers</h1>
            <p className="tagline">Test E-commerce Platform</p>
          </div>
          <div className="header-right">
            <div className="user-selector">
              <label>Test User:</label>
              <select value={userId} onChange={handleUserChange}>
                <option value="user-1">user-1 (Anh Nguyen)</option>
                <option value="user-2">user-2 (Quang Tran)</option>
                <option value="user-3">user-3 (Thu Le)</option>
              </select>
            </div>
          </div>
        </div>
      </header>

      <nav className="app-nav">
        <button
          className={`nav-btn ${page === 'products' ? 'active' : ''}`}
          onClick={() => setPage('products')}
        >
          🏃 Shop Sneakers
        </button>
        <button
          className={`nav-btn ${page === 'cart' ? 'active' : ''}`}
          onClick={() => setPage('cart')}
        >
          🛒 Cart {cartCount > 0 && `(${cartCount})`}
        </button>
        <button
          className={`nav-btn ${page === 'checkout' ? 'active' : ''}`}
          onClick={() => setPage('checkout')}
        >
          📦 Checkout
        </button>
      </nav>

      <main className="app-content">
        {page === 'products' && (
          <ProductList userId={userId} onAddToCart={handleAddToCart} />
        )}
        {page === 'cart' && <CartPage userId={userId} />}
        {page === 'checkout' && <CheckoutPage userId={userId} />}
      </main>

      <footer className="app-footer">
        <p>🧪 Frontend E-commerce Test Platform | Backend: http://localhost:8080</p>
      </footer>
    </>
  );
}

export default App;
