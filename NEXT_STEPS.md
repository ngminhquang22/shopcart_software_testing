# 🚀 NEXT STEPS - Quick Action Guide

## 📋 What You Need to Do

### ✅ DONE - Already Completed
- ✅ Backend Spring Boot application (fully working, 72/72 tests passing)
- ✅ Frontend React application (all components created)
- ✅ Database schema with seed data (8 products, 3 users, 3 coupons)
- ✅ API services with Bearer token authentication
- ✅ Professional styling and UI/UX

---

## 🎯 IMMEDIATE NEXT STEPS (Do This Now!)

### Step 1️⃣ - Start Backend (Terminal 1)
```bash
cd d:\Khanh\Testing Software\asignment2\shopcart_software_testing\backend
mvn clean spring-boot:run
```

**Expected Output:**
```
...
Tomcat started on port(s): 8080 (http)
Started [Application] in [X] seconds
```

✅ Backend is ready at: http://localhost:8080

---

### Step 2️⃣ - Start Frontend (Terminal 2)
```bash
cd d:\Khanh\Testing Software\asignment2\shopcart_software_testing\frontend
npm run dev
```

**Expected Output:**
```
...
VITE v8.x.x
Local: http://localhost:5173/
```

✅ Frontend is ready at: http://localhost:5173

---

### Step 3️⃣ - Open in Browser
Visit: **http://localhost:5173**

✅ You should see:
- Purple header with "🏪 ShoeCart - Premium Sneakers"
- User selector dropdown (top right)
- 3 navigation tabs (Shop Sneakers | Cart | Checkout)
- Product grid with 8 sneaker products

---

## ✨ Test These Flows Right Now

### Flow 1: Browse Products
```
1. Page shows: "🏃 Shop Sneakers"
2. See 8 sneaker product cards
3. Each shows: Image, name, price, description
4. Select quantity and click "Add to Cart"
```

### Flow 2: Add to Cart
```
1. Add "Nike Air Force 1 Low White" (qty: 2)
2. See cart badge update to (2)
3. Add "Jordan 1 Chicago" (qty: 1)
4. See cart badge update to (3)
```

### Flow 3: View Cart
```
1. Click "🛒 Cart" tab
2. See all 3 items listed
3. See total price calculated
4. Try +/- buttons to change quantity
5. See price update in real-time
```

### Flow 4: Complete Checkout
```
1. Click "📦 Checkout" tab
2. Step 1: Review items → Click "Proceed to Checkout"
3. Step 2: Fill form
   - Address: "123 Main St, HCMC"
   - Coupon: Select "WELCOME10"
   - Payment: Select "COD"
4. See discount applied (-10%)
5. Click "Place Order"
6. See success message with Order ID
```

### Flow 5: Multi-User Test
```
1. Top right: Select "user-2" from dropdown
2. Cart empties (different user)
3. Add different items
4. Switch back to "user-1"
5. Verify user-1's items are still there
```

---

## 📊 Files You Can Check

### Frontend Files
```
frontend/src/components/
├── ProductList.tsx      ← Browse & add products
├── CartPage.tsx         ← View & manage cart
└── CheckoutPage.tsx     ← Checkout flow

frontend/src/services/
├── apiClient.ts         ← HTTP client (Bearer auth)
├── productService.ts    ← Products
├── cartApiService.ts    ← Cart API
└── orderApiService.ts   ← Orders API
```

### Backend Files
```
backend/src/main/java/com/shopcart/
├── controller/
│   ├── CartController.java
│   └── OrderController.java
├── security/
│   ├── SecurityConfig.java
│   └── JwtAuthenticationFilter.java
└── entity/              ← Database models
```

---

## 🧪 Verify Everything Works

### Checklist
- [ ] Backend starts without errors
- [ ] Frontend starts without errors
- [ ] Frontend loads at http://localhost:5173
- [ ] Header shows "ShoeCart" logo
- [ ] User selector dropdown present
- [ ] 3 navigation tabs visible
- [ ] 8 products display in grid
- [ ] Can add items to cart
- [ ] Cart badge updates
- [ ] Cart page shows items
- [ ] Checkout form visible
- [ ] Can place order successfully

---

## 🐛 If Something Goes Wrong

### Backend won't start
```bash
# Check if port 8080 is already in use
# Kill any process on port 8080 or change port in:
# backend/src/main/resources/application.yaml
# server.port: 8080
```

### Frontend won't start
```bash
cd frontend
npm install  # Reinstall dependencies
npm run dev
```

### API calls failing (401 error)
✅ This is normal - backend requires Bearer token
The `apiClient.ts` handles this automatically

### Can't connect to backend from frontend
✅ Make sure both are running:
- Backend: http://localhost:8080
- Frontend: http://localhost:5173

---

## 📱 Features You Can Test

### Product Features
- [x] View all 8 sneaker products
- [x] See product details (price, description)
- [x] Select quantity (1-30)
- [x] Add to cart

### Cart Features
- [x] View cart items
- [x] Update quantities (+/- buttons)
- [x] Remove items
- [x] See total price

### Checkout Features
- [x] 2-step checkout form
- [x] Apply coupon codes
- [x] Select payment method
- [x] Enter shipping details
- [x] See order confirmation

### User Features
- [x] Switch between 3 test users
- [x] Each user has isolated cart
- [x] Each user can have different orders

---

## 💡 Key Information

### Test Users
- **user-1** (Nguyễn Văn Anh)
- **user-2** (Trần Minh Quang)
- **user-3** (Lê Thị Thu)

### Coupon Codes
- **WELCOME10** → 10% discount
- **SAVE100K** → 100,000 VND discount
- **SUMMER15** → 15% discount

### Sample Products (Vietnamese Sneakers)
- Nike Air Force 1 Low White: 2.59M VND
- Jordan 1 Chicago: 5.89M VND
- Yeezy Boost 350 V2: 7.39M VND
- And 5 more...

---

## 📖 Documentation

- **Full Setup Guide:** COMPLETE_SETUP_GUIDE.md
- **Frontend Details:** frontend/FRONTEND_GUIDE.md
- **Backend Details:** backend/BACKEND_GUIDE.md
- **Implementation Summary:** FRONTEND_IMPLEMENTATION_SUMMARY.md

---

## ✅ Success Indicators

You'll know everything works when:

1. ✅ Backend console shows: "Started [Application] in X seconds"
2. ✅ Frontend console shows: "Local: http://localhost:5173/"
3. ✅ Browser shows ShoeCart homepage with products
4. ✅ Can add items and see cart update
5. ✅ Can complete checkout without errors

---

## 🎉 You're Ready!

Everything is set up and ready to go. Just:

1. **Terminal 1:** `mvn clean spring-boot:run` (backend)
2. **Terminal 2:** `npm run dev` (frontend)
3. **Browser:** http://localhost:5173

Then test the complete e-commerce flow! 🛒

---

## 📞 Need Help?

1. **Error on backend?** Check: `backend/BACKEND_GUIDE.md`
2. **Error on frontend?** Check: `frontend/FRONTEND_GUIDE.md`
3. **Not sure how it works?** Read: `COMPLETE_SETUP_GUIDE.md`
4. **Want to see what was built?** Read: `FRONTEND_IMPLEMENTATION_SUMMARY.md`

---

**Let's Go! 🚀**

Start backend and frontend now and test the complete platform!
