# 🚀 Backend Setup Guide

## Điều kiện tiên quyết
- Java 21+
- Maven 3.8+
- PostgreSQL hoặc H2 database

---

## Cách chạy Backend

### **Option 1: Chạy với H2 In-Memory Database (RECOMMENDED)**

```bash
cd backend
mvn clean spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

Backend sẽ chạy trên **http://localhost:8080**

### **Option 2: Chạy với PostgreSQL**

#### 1. Tạo file `.env` trong thư mục backend

```bash
cd backend
echo DB_URL=jdbc:postgresql://localhost:5432/shopcart > .env
echo DB_USERNAME=postgres >> .env
echo DB_PASSWORD=your_password >> .env
```

#### 2. Chạy backend
```bash
mvn clean spring-boot:run
```

#### 3. Hoặc chạy file jar
```bash
mvn clean package -DskipTests
java -jar target/shopcart-application-1.0-SNAPSHOT.jar
```

---

## 📊 Database Setup

### Tạo database và import schema

```bash
# Kết nối PostgreSQL
psql -U postgres

# Trong psql:
CREATE DATABASE shopcart;
\c shopcart
\i path/to/data/database.sql
```

Hoặc copy-paste nội dung `data/database.sql` vào DBeaver/pgAdmin

---

## ✅ Verify Backend is Running

```bash
curl http://localhost:8080/health
```

Response:
```json
{"status":"UP"}
```

---

## 🧪 Test API với curl

### 1. Xem sản phẩm
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer user-1"
```

### 2. Thêm vào giỏ
```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer user-1" \
  -d '{
    "productId": "prod-1",
    "productName": "Nike Air Force 1 Low White",
    "quantity": 1,
    "price": 2590000
  }'
```

### 3. Xem giỏ hàng
```bash
curl -X GET http://localhost:8080/api/cart/user-1/items \
  -H "Authorization: Bearer user-1"
```

### 4. Tạo đơn hàng
```bash
curl -X POST http://localhost:8080/api/orders/user-1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer user-1" \
  -d '{
    "items": [
      {
        "productId": "prod-1",
        "productName": "Nike Air Force 1 Low White",
        "quantity": 1,
        "price": 2590000
      }
    ],
    "shippingAddress": "123 Main St, HCMC",
    "shippingFee": 50000,
    "couponCode": "WELCOME10",
    "paymentMethod": "COD"
  }'
```

---

## 📝 API Endpoints

### 🛒 Cart Endpoints
```
GET    /api/cart/{userId}/items              - Xem giỏ hàng
POST   /api/cart/add                         - Thêm vào giỏ
PUT    /api/cart/{userId}/items/{itemId}    - Cập nhật số lượng
DELETE /api/cart/{userId}/items/{productId} - Xóa khỏi giỏ
```

### 📦 Order Endpoints
```
GET    /api/orders/{userId}                 - Xem đơn hàng
POST   /api/orders/{userId}                 - Tạo đơn hàng
GET    /api/orders/detail/{orderId}         - Chi tiết đơn hàng
PUT    /api/orders/{orderId}/status         - Cập nhật trạng thái
DELETE /api/orders/{orderId}                - Hủy đơn hàng
```

### 🛡️ Auth & Public
```
POST   /auth/register                       - Đăng ký user
POST   /auth/login                          - Đăng nhập
GET    /health                              - Health check
GET    /public/products                     - Xem tất cả sản phẩm (public)
```

---

## 👤 Test Users

| User ID | Password | Email |
|---------|----------|-------|
| user-1 | password | anh.nguyen@example.com |
| user-2 | password | quang.tran@example.com |
| user-3 | password | thu.le@example.com |

**Authentication:** 
```
Header: Authorization: Bearer {user_id}
```

---

## 🔍 Troubleshooting

### "Connection refused"
Backend không chạy. Kiểm tra:
```bash
# 1. Backend process đang chạy?
lsof -i :8080

# 2. Logs
cd backend
mvn clean spring-boot:run
```

### "Database connection error"
```bash
# 1. PostgreSQL chạy chưa?
sudo service postgresql status

# 2. Check credentials
psql -U postgres -c "SELECT version();"

# 3. Database tồn tại?
psql -U postgres -l | grep shopcart
```

### "401 Unauthorized"
```bash
# 1. Missing Authorization header?
# Tất cả /api/* endpoints require:
Authorization: Bearer {userId}

# 2. Correct test user?
# Valid: user-1, user-2, user-3
# Invalid: user (missing number)
```

---

## 📈 Running Tests

### Unit Tests
```bash
cd backend
mvn clean test
```

### Integration Tests
```bash
cd backend
mvn clean verify
```

### Coverage Report
```bash
mvn clean test jacoco:report
# Report: target/site/jacoco/index.html
```

---

## 🌳 Project Structure

```
backend/
├── src/main/java/com/shopcart/
│   ├── controller/          (API endpoints)
│   ├── service/            (Business logic)
│   ├── repository/         (Data access)
│   ├── entity/             (JPA entities)
│   ├── security/           (Auth & security)
│   └── dto/                (Data transfer objects)
├── src/main/resources/
│   └── application.yaml    (Configuration)
├── src/test/java/
│   └── com/shopcart/       (Tests)
├── pom.xml                 (Maven config)
└── data/database.sql       (Schema & seed data)
```

---

## 🎯 Quick Start

```bash
# 1. Build backend
cd backend
mvn clean compile

# 2. Run backend (H2 in-memory)
mvn spring-boot:run

# 3. In another terminal, start frontend
cd frontend
npm install
npm run dev

# 4. Open browser
# Frontend: http://localhost:5173
# Backend:  http://localhost:8080
```

---

Happy Testing! 🎉
