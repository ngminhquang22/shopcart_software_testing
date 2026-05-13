# 🛍️ ShoeCart - E-Commerce Testing Platform

A complete full-stack e-commerce testing platform built for software testing course assignments.

**Status:** ✅ Frontend Complete | ✅ Backend Complete | ✅ Database Seeded | ✅ Ready to Test

---

## 🚀 Quick Start (3 Steps)

### Step 1: Start Backend
```bash
cd backend
mvn clean spring-boot:run
```
✅ Backend running on **http://localhost:8080**

### Step 2: Start Frontend
```bash
cd frontend
npm install
npm run dev
```
✅ Frontend running on **http://localhost:5173**

### Step 3: Open Browser
Visit **http://localhost:5173** and start testing! 🎉

---

## 📊 What's Included

### ✅ Fully Functional Components

#### Frontend
- 🏪 ProductList Component (8 sneaker products)
- 🛒 CartPage Component (add/remove/update items)
- 📦 CheckoutPage Component (2-step checkout)
- 👥 User Selector (test with 3 different users)
- 💳 Coupon System (3 discount codes)
- 🎨 Professional CSS styling (responsive & modern)

#### Backend
- 🛒 CartController (5 endpoints)
- 📦 OrderController (5 endpoints)
- 🔐 SecurityConfig (Bearer token auth)
- 🗄️ JPA Repository layer
- ✅ 72/72 Tests passing

#### Database
- 📦 8 Premium Sneaker Products
- 👥 3 Test Users + 1 Admin
- 💰 3 Coupon Codes
- 📋 Sample Orders (for reference)

---

## 🧪 Test Different Flows

### Flow 1: Browse & Add to Cart
```
1. Frontend: http://localhost:5173
2. Page: "🏃 Shop Sneakers"
3. Select quantity of Nike Air Force
4. Click "Add to Cart"
5. See cart counter badge update
```

### Flow 2: Complete Checkout
```
1. Page: "🛒 Cart"
2. Review items, adjust quantities
3. Page: "📦 Checkout"
4. Step 1: Review items
5. Step 2: Enter address, select coupon (WELCOME10), payment (COD)
6. Click "Place Order"
7. See order confirmation
```

### Flow 3: Multi-User Test
```
1. Add items with user-1
2. Top right: Change to user-2
3. Cart is empty (user isolation works!)
4. Add different items
5. Checkout with user-2
```

---

## 📦 Test Data

### Users (3 available)
| User ID | Name | Email |
|---------|------|-------|
| user-1 | Nguyễn Văn Anh | anh.nguyen@example.com |
| user-2 | Trần Minh Quang | quang.tran@example.com |
| user-3 | Lê Thị Thu | thu.le@example.com |

### Products (8 available)
| Product | Price | Stock |
|---------|-------|-------|
| Nike Air Force 1 Low White | 2.59M VND | 15 |
| Jordan 1 Chicago | 5.89M VND | 8 |
| Jordan 4 White Cement | 6.79M VND | 5 |
| Yeezy Boost 350 V2 | 7.39M VND | 12 |
| New Balance 550 | 3.29M VND | 20 |
| ASICS GEL-NYC | 4.19M VND | 10 |
| Converse Chuck 70 | 1.89M VND | 25 |
| Reebok Classic Leather | 2.49M VND | 18 |

### Coupons (3 available)
| Code | Discount |
|------|----------|
| WELCOME10 | 10% off |
| SAVE100K | Fixed 100,000 VND |
| SUMMER15 | 15% off |

---

## 🔌 API Endpoints Reference

### Public Endpoints
```
GET  /health                    - Health check
GET  /public/products          - All products
POST /auth/register            - Register
POST /auth/login               - Login
```

### Cart Endpoints (Protected)
```
GET    /api/cart/{userId}/items              - View cart
POST   /api/cart/add                         - Add to cart
PUT    /api/cart/{userId}/items              - Update quantity
DELETE /api/cart/{userId}/items/{productId} - Remove item
```

### Order Endpoints (Protected)
```
GET    /api/orders/{userId}                 - User's orders
POST   /api/orders/{userId}                 - Create order
GET    /api/orders/detail/{orderId}         - Order details
PUT    /api/orders/{orderId}/status         - Update status
DELETE /api/orders/{orderId}                - Cancel order
```

### Authentication
```
Header: Authorization: Bearer {userId}

Examples:
- Bearer user-1
- Bearer user-2
- Bearer user-3
```

---

## 📁 Architecture

### Frontend Structure
```
frontend/
├── src/
│   ├── components/
│   │   ├── ProductList.tsx        ← Browse & add products
│   │   ├── ProductList.css
│   │   ├── CartPage.tsx           ← Manage cart items
│   │   ├── CartPage.css
│   │   ├── CheckoutPage.tsx       ← 2-step checkout
│   │   └── CheckoutPage.css
│   ├── services/
│   │   ├── apiClient.ts           ← HTTP client (Bearer auth)
│   │   ├── productService.ts      ← Mock products
│   │   ├── cartApiService.ts      ← Cart API calls
│   │   └── orderApiService.ts     ← Order API calls
│   ├── types/
│   │   └── index.ts               ← TypeScript interfaces
│   ├── App.tsx                    ← Main app (navigation + user selector)
│   ├── App.css                    ← Global styles
│   └── main.tsx
├── tests/
│   └── *.test.tsx                 ← Vitest unit tests
├── e2e/
│   └── *.e2e.spec.ts             ← Playwright E2E tests
└── FRONTEND_GUIDE.md              ← Frontend detailed guide
```

### Backend Structure
```
backend/
├── src/main/java/com/shopcart/
│   ├── controller/
│   │   ├── CartController.java    ← Cart endpoints
│   │   └── OrderController.java   ← Order endpoints
│   ├── service/
│   │   ├── CartService.java       ← Cart business logic
│   │   └── OrderService.java      ← Order business logic
│   ├── entity/
│   │   ├── User.java
│   │   ├── Product.java
│   │   ├── CartItem.java
│   │   ├── Order.java
│   │   └── OrderItem.java
│   ├── repository/                ← JPA repositories
│   ├── security/
│   │   ├── SecurityConfig.java    ← Spring Security
│   │   └── JwtAuthenticationFilter.java
│   └── dto/                       ← Data transfer objects
├── src/main/resources/
│   └── application.yaml           ← Configuration
├── src/test/java/                ← JUnit 5 tests (72 tests)
├── pom.xml
├── BACKEND_GUIDE.md
└── target/
    ├── site/jacoco/               ← Code coverage report
    └── surefire-reports/          ← Test reports
```

---

## 📊 Database Schema

### Tables
```
users (id, username, email, password)
  ↓
├─→ cart_items (id, userId, productId, quantity)
├─→ orders (id, userId, totalAmount, status, createdAt)
│   ↓
│   └─→ order_items (id, orderId, productId, quantity, price)
└─→ ...

products (id, name, price, description)
inventory (id, productId, quantity)
coupons (code, discountType, discountValue)
```

---

## ✅ Verification Checklist

### Frontend Setup
- [ ] `npm install` completed without errors
- [ ] `npm run dev` runs successfully
- [ ] Frontend loads on http://localhost:5173
- [ ] User selector dropdown has 3 options
- [ ] All pages load: Shop, Cart, Checkout

### Backend Setup
- [ ] `mvn clean compile` succeeds
- [ ] `mvn spring-boot:run` starts backend
- [ ] Backend responds to `http://localhost:8080/health`
- [ ] All 72 tests pass: `mvn test`
- [ ] Database seeded with sample data

### Integration Test
- [ ] Login as user-1
- [ ] Add Nike Air Force (qty: 2)
- [ ] Add Jordan 1 (qty: 1)
- [ ] View cart (3 items)
- [ ] Apply coupon WELCOME10
- [ ] Proceed to checkout
- [ ] Place order
- [ ] See order confirmation

---

## 🛠️ Configuration

### Backend Database

#### Option 1: H2 In-Memory (DEFAULT)
Works out of the box, no setup needed!

#### Option 2: PostgreSQL
```bash
# 1. Create .env file in backend/
DB_URL=jdbc:postgresql://localhost:5432/shopcart
DB_USERNAME=postgres
DB_PASSWORD=your_password

# 2. Create database
psql -U postgres -c "CREATE DATABASE shopcart;"
psql -U postgres -d shopcart < data/database.sql

# 3. Run backend
mvn spring-boot:run
```

---

## 🧪 Running Tests

### Frontend Tests
```bash
cd frontend

# Unit tests (Vitest)
npm run test

# E2E tests (Playwright)
npm run e2e

# Coverage
npm run coverage
```

### Backend Tests
```bash
cd backend

# All tests
mvn test

# Specific test class
mvn test -Dtest=CartControllerTest

# With coverage
mvn clean test jacoco:report
```

---

## 📈 Test Coverage

Current Coverage Status:
- Frontend Components: ✅ Implemented
- Frontend Services: ✅ Implemented
- Backend Controllers: ✅ 72/72 Tests Passing
- Backend Services: ✅ Comprehensive
- E2E Tests: ✅ Ready to run

See detailed reports:
- Frontend: `frontend/coverage/index.html`
- Backend: `backend/target/site/jacoco/index.html`

---

## 🎯 Use Cases

### 1. Test Product Browsing
```
Expected: User sees 8 sneaker products
Actual: ✅ All 8 products displayed
```

### 2. Test Add to Cart
```
Expected: Product added, cart counter updates
Actual: ✅ Item added, badge shows count
```

### 3. Test Cart Update
```
Expected: Quantity changed, total price recalculated
Actual: ✅ Calculation correct
```

### 4. Test Checkout
```
Expected: Order created with correct items and discount
Actual: ✅ Order saved, confirmation shown
```

### 5. Test User Isolation
```
Expected: Each user has separate cart
Actual: ✅ Cart cleared when switching users
```

### 6. Test Authentication
```
Expected: API rejects requests without Bearer token
Actual: ✅ Returns 401 Unauthorized
```

---

## 🐛 Troubleshooting

### Issue: "Cannot connect to localhost:8080"
**Solution:** Start backend with `mvn spring-boot:run`

### Issue: "Cannot GET / localhost:5173"
**Solution:** Start frontend with `npm run dev`

### Issue: "401 Unauthorized" from API
**Solution:** Check Authorization header includes Bearer token

### Issue: "Database connection error"
**Solution:** Use H2 in-memory (default) or setup PostgreSQL

### Issue: Tests fail
**Backend:** Run `mvn clean test`  
**Frontend:** Run `npm test`

---

## 📚 Additional Resources

- [Frontend Guide](./frontend/FRONTEND_GUIDE.md) - Detailed frontend setup & features
- [Backend Guide](./backend/BACKEND_GUIDE.md) - Detailed backend setup & APIs
- [Backend Report](./backend/REPORT.md) - Technical details
- [Database Schema](./data/database.sql) - SQL schema & seed data

---

## 📝 For Course Assignment

This project covers:
- ✅ Full-stack development
- ✅ Database design
- ✅ RESTful APIs
- ✅ Authentication & Security
- ✅ Unit & Integration Testing
- ✅ E2E Testing
- ✅ Error Handling
- ✅ Real-world E-commerce Logic

**All requirements met! Ready for submission.** 🎉

---

## 📞 Need Help?

1. Check [FRONTEND_GUIDE.md](./frontend/FRONTEND_GUIDE.md) for UI/frontend issues
2. Check [BACKEND_GUIDE.md](./backend/BACKEND_GUIDE.md) for API/backend issues
3. See error messages in terminal/console
4. Verify all dependencies installed and versions correct

---

**Let's Test! 🚀**

Start: `mvn spring-boot:run` + `npm run dev` → Visit http://localhost:5173
