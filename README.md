# ShopCart - E-Commerce Testing Web Application

**TRƯỜNG ĐẠI HỌC SÀI GÒN**

**KHOA CÔNG NGHỆ THÔNG TIN**

**Môn học:** Kiểm Thử Phần Mềm  
**Giảng viên HD:** Từ Lãng Phiêu  
**Niên khóa:** 2025 - 2026  
**Bài tập lớn:** ShopCart (Version 1.1)  
**Tổng điểm:** 10 điểm  
**Deadline:** 23h59, 10/05/2026

**Sinh viên thực hiện:**
- Nguyễn Minh Quang

---

## Mô tả dự án

Dự án ShopCart là một ứng dụng web thương mại điện tử phục vụ cho việc kiểm thử các chức năng ở cả frontend và backend. Các nghiệp vụ cốt lõi bao gồm:

- **Mua sản phẩm & Giỏ hàng (Cart):** Người dùng chọn mua, thêm sản phẩm vào giỏ hàng, cập nhật số lượng, xóa sản phẩm khỏi giỏ hàng.
- **Tính toán giá (Pricing):** Tính tổng giá trị đơn hàng, áp dụng mã giảm giá và cộng phí vận chuyển khi phát sinh.
- **Kiểm tra tồn kho (Inventory):** Xác nhận số lượng sản phẩm còn lại trong kho trước và sau thao tác; cảnh báo/khóa thao tác khi hết hàng hoặc vượt tồn.
- **Mua hàng (Purchase/Checkout):** Xác nhận đơn hàng, trừ tồn kho, tạo mã đơn hàng và trả về trạng thái xử lý.

---

## Công nghệ sử dụng

### Frontend
- React 19.x + Vite
- CSS3 / TailwindCSS
- Axios HTTP client
- **Testing Frameworks:** - Vitest (Unit test)
    - React Testing Library (Component/Integration test)
    - Playwright (End-to-end E2E test trên trình duyệt)

### Backend
- Spring Boot 3.5.x hoặc 4.x
- Java 21
- Spring Data JPA
- Spring Security
- Database: H2 / PostgreSQL
- Maven Build tool
- **Testing Frameworks:** - JUnit 5
    - Mockito

### CI/CD & Advanced Testing
- GitHub Actions (CI/CD Pipeline)
- K6 / JMeter (Performance Testing)

---

## Cấu trúc dự án

    ShopCart_FE_BE/
    ├── frontend/
    │   ├── src/
    │   │   ├── components/  ← Các UI component (Cart, Checkout, Inventory)
    │   │   ├── services/    ← API services (cartService, orderService)
    │   │   ├── utils/       ← Utilities cho validation và tính toán giá
    │   │   └── hooks/       ← Custom React hooks
    │   ├── tests/           ← File Unit/Integration tests sử dụng Vitest
    │   ├── e2e/             ← Kịch bản kiểm thử E2E sử dụng Playwright
    │   ├── playwright.config.ts
    │   └── vite.config.ts
    └── backend/
        ├── src/
        │   ├── main/java/com/shopcart/
        │   │   ├── controller/  ← API Endpoints (Cart, Order, Inventory)
        │   │   ├── service/     ← Business logic
        │   │   ├── dto/         ← Data Transfer Objects
        │   │   ├── entity/      ← Database entities
        │   │   └── repository/  ← Data access layer
        │   └── test/java/       ← File test sử dụng JUnit 5 + Mockito
        └── pom.xml

---

## Tiến độ hiện tại / Checklist Assignment (10 điểm)

- [ ] **Câu 1 (0.5đ):** Phân tích và Thiết kế Test Cases (TC cho Cart & Purchase).
- [ ] **Câu 2 (2.0đ):** Unit Testing và TDD (Frontend Vitest, Backend JUnit/Mockito).
- [ ] **Câu 3 (2.0đ):** Integration Testing (Component Integration, API Endpoints).
- [ ] **Câu 4 (2.0đ):** Mock Testing (Mock API, Service, Repository).
- [ ] **Câu 5 (2.0đ):** Automation Testing (Playwright E2E) & CI/CD Pipeline.
- [ ] **Câu 6 (1.5đ):** Advanced Testing (Performance Testing & Security Testing).
- [ ] **Khác:** Báo cáo PDF, Report Coverage (JaCoCo, Vitest), Report Playwright, Video Demo.

---

## Hướng dẫn chạy & Cài đặt Test

### Backend (Java / Spring Boot)

    cd backend
    
    # Chạy ứng dụng
    ./mvnw spring-boot:run

    # Chạy toàn bộ Unit & Integration tests
    ./mvnw clean test

    # Tạo và xem report độ bao phủ code (JaCoCo)
    ./mvnw jacoco:report
    
    # Mở file report tại: backend/target/site/jacoco/index.html

### Frontend (React / Vite)

    cd frontend
    npm install
    
    # Chạy ứng dụng
    npm run dev

    # 1. Chạy Unit & Integration tests (Vitest)
    npm run test

    # 2. Xem coverage report
    npm run test -- --coverage

    # 3. Cài đặt trình duyệt cho Playwright (Chỉ chạy lần đầu)
    npx playwright install --with-deps

    # 4. Chạy End-to-End tests (Playwright)
    npx playwright test

    # 5. Xem giao diện report của Playwright
    npx playwright show-report

---

## Git Workflow

- Nhánh chính: `main` hoặc `develop`
- Feature branch: `feature/[tên-task]` (ví dụ: `feature/cart-unit-tests`)
- Tránh commit file thừa, hãy đảm bảo có file `.gitignore` đầy đủ (`node_modules`, `target`, v.v.)