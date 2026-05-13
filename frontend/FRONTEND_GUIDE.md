# 🛒 ShoeCart - Frontend E-commerce Test Platform

## Cách chạy

### 1. **Cài đặt dependencies**
```bash
cd frontend
npm install
```

### 2. **Chạy development server**
```bash
npm run dev
```

Mở trình duyệt: **http://localhost:5173**

### 3. **Chạy tests**
```bash
npm run test
```

---

## 🎯 Các tính năng

### **1. 🏃 Shop Sneakers (Danh sách sản phẩm)**
- Hiển thị 8 sản phẩm giày hàng hiệu (Nike Air Force, Jordan, Yeezy, v.v.)
- Chọn số lượng và thêm vào giỏ hàng
- Gọi API backend: `POST /api/cart/add`

### **2. 🛒 Shopping Cart (Giỏ hàng)**
- Xem tất cả sản phẩm trong giỏ
- Cập nhật số lượng từng item
- Xóa sản phẩm khỏi giỏ
- Gọi API backend:
  - `GET /api/cart/{userId}/items`
  - `PUT /api/cart/{userId}/items`
  - `DELETE /api/cart/{userId}/items/{productId}`

### **3. 📦 Checkout (Tạo đơn hàng)**
- **Step 1:** Review items trong giỏ
- **Step 2:** Nhập địa chỉ, chọn phương thức thanh toán, áp dụng coupon
- **Step 3:** Tạo đơn hàng, xem kết quả
- Gọi API backend: `POST /api/orders/{userId}`

---

## 👥 Tài khoản Test

Có 3 user mẫu để test:

| User ID | Username | Email |
|---------|----------|-------|
| user-1 | nguyenvananh | anh.nguyen@example.com |
| user-2 | tranminhquang | quang.tran@example.com |
| user-3 | lethithu | thu.le@example.com |

Chọn user từ dropdown ở góc trên phải để thay đổi.

---

## 💳 Coupon Test

```
WELCOME10  → Giảm 10% subtotal
SAVE100K   → Giảm 100,000 VND
SUMMER15   → Giảm 15% subtotal
```

---

## 🏗️ Kiến trúc

```
src/
├── components/
│   ├── ProductList.tsx      (Danh sách sản phẩm)
│   ├── ProductList.css
│   ├── CartPage.tsx         (Giỏ hàng)
│   ├── CartPage.css
│   ├── CheckoutPage.tsx     (Checkout)
│   └── CheckoutPage.css
├── services/
│   ├── apiClient.ts         (HTTP client gọi backend)
│   ├── productService.ts    (Mock products)
│   ├── cartApiService.ts    (Cart API)
│   └── orderApiService.ts   (Order API)
├── types/
│   └── index.ts             (TypeScript interfaces)
├── App.tsx                  (Main component)
├── App.css                  (Global styles)
└── main.tsx
```

---

## 🔌 Backend API

Frontend gọi các endpoint sau:

### Cart API
```
POST   /api/cart/add                          (Thêm vào giỏ)
GET    /api/cart/{userId}/items              (Xem giỏ)
PUT    /api/cart/{userId}/items              (Cập nhật số lượng)
DELETE /api/cart/{userId}/items/{productId}  (Xóa khỏi giỏ)
```

### Order API
```
POST   /api/orders/{userId}          (Tạo đơn hàng)
GET    /api/orders/{userId}          (Xem đơn hàng)
GET    /api/orders/detail/{orderId}  (Chi tiết đơn hàng)
PUT    /api/orders/{orderId}/status  (Cập nhật trạng thái)
DELETE /api/orders/{orderId}         (Hủy đơn hàng)
```

---

## 🧪 Test Scenarios

### Scenario 1: Mua Nike Air Force
1. Vào **Shop Sneakers**
2. Tìm "Nike Air Force 1 Low White"
3. Nhập quantity = 2
4. Click **Add to Cart**
5. Vào **Cart**, xem kết quả

### Scenario 2: Checkout với Coupon
1. Thêm 2-3 sản phẩm vào giỏ
2. Vào **Checkout**
3. Step 1: Review items
4. Step 2: 
   - Coupon: WELCOME10
   - Address: "123 Main Street, HCMC"
   - Payment: COD
5. Click **Place Order**

### Scenario 3: Multi-user Test
1. Thêm sản phẩm với user-1
2. Thay đổi user sang user-2
3. Xem cart user-2 (sẽ khác)
4. Thêm sản phẩm khác
5. Checkout với user-2

---

## ⚡ Notes

- Frontend chạy trên **http://localhost:5173** (Vite dev server)
- Backend cần chạy trên **http://localhost:8080**
- Bearer token = userId (ví dụ: `Authorization: Bearer user-1`)
- Database seeded với 8 sản phẩm, 3 user, 3 coupon
- Tất cả giao diện đều responsive và test-friendly

---

## 🛠️ Development

### Thêm sản phẩm mới
Chỉnh sửa `src/services/productService.ts`:
```typescript
export const getProducts = (): Product[] => {
  return [
    // Thêm product mới ở đây
  ];
};
```

### Thay đổi backend URL
Sửa trong `src/services/apiClient.ts`:
```typescript
const API_BASE_URL = 'http://localhost:8080/api'; // ← Đây
```

---

Happy Testing! 🎉
