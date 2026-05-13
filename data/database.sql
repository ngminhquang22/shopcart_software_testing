CREATE TABLE users (
    user_id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products (
    product_id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE inventory (
    inventory_id VARCHAR(255) PRIMARY KEY,
    product_id VARCHAR(255),
    quantity INTEGER NOT NULL,
    CONSTRAINT fk_inventory_product 
        FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE cart_items (
    cart_item_id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255),
    product_id VARCHAR(255),
    quantity INTEGER NOT NULL,
    CONSTRAINT fk_cart_user 
        FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_cart_product 
        FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE coupons (
    coupon_code VARCHAR(255) PRIMARY KEY,
    discount_type VARCHAR(50) NOT NULL,
    discount_value BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    valid_until TIMESTAMP
);

CREATE TABLE orders (
    order_id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    coupon_code VARCHAR(255),
    subtotal_price BIGINT NOT NULL,
    shipping_fee BIGINT NOT NULL,
    total_price BIGINT NOT NULL,
    shipping_address VARCHAR(500),
    payment_method VARCHAR(50),
    CONSTRAINT fk_order_user 
        FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_order_coupon 
        FOREIGN KEY (coupon_code) REFERENCES coupons(coupon_code)
);

CREATE TABLE orders_items (
    order_item_id VARCHAR(255) PRIMARY KEY,
    order_id VARCHAR(255),
    product_id VARCHAR(255),
    quantity INTEGER NOT NULL,
    unit_price BIGINT NOT NULL,
    CONSTRAINT fk_order_items_order 
        FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT fk_order_items_product 
        FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Seed data for testing sneaker-focused e-commerce flows
INSERT INTO users (user_id, username, role, email, created_at) VALUES
('user-1', 'nguyenvananh', 'CUSTOMER', 'anh.nguyen@example.com', '2026-05-01 08:15:00'),
('user-2', 'tranminhquang', 'CUSTOMER', 'quang.tran@example.com', '2026-05-02 09:30:00'),
('user-3', 'lethithu', 'CUSTOMER', 'thu.le@example.com', '2026-05-03 11:45:00'),
('admin-1', 'admin.shopcart', 'ADMIN', 'admin@shopcart.local', '2026-05-01 07:00:00');

INSERT INTO products (product_id, name, price, status) VALUES
('nike-air-force-1-white', 'Nike Air Force 1 Low White', 2590000, 'ACTIVE'),
('nike-air-force-1-black', 'Nike Air Force 1 Low Black', 2590000, 'ACTIVE'),
('jordan-1-chicago', 'Air Jordan 1 Retro High OG Chicago', 5890000, 'ACTIVE'),
('jordan-4-white-cement', 'Air Jordan 4 Retro White Cement', 6790000, 'ACTIVE'),
('yeezy-boost-350-v2', 'Adidas Yeezy Boost 350 V2 Bone', 7390000, 'ACTIVE'),
('new-balance-550-white', 'New Balance 550 White Grey', 3290000, 'ACTIVE'),
('asics-gel-nyc-cream', 'ASICS GEL-NYC Cream', 4190000, 'ACTIVE'),
('converse-chuck-70-high', 'Converse Chuck 70 High Black', 1890000, 'ACTIVE');

INSERT INTO inventory (inventory_id, product_id, quantity) VALUES
('inv-1', 'nike-air-force-1-white', 25),
('inv-2', 'nike-air-force-1-black', 18),
('inv-3', 'jordan-1-chicago', 7),
('inv-4', 'jordan-4-white-cement', 5),
('inv-5', 'yeezy-boost-350-v2', 12),
('inv-6', 'new-balance-550-white', 20),
('inv-7', 'asics-gel-nyc-cream', 15),
('inv-8', 'converse-chuck-70-high', 30);

INSERT INTO coupons (coupon_code, discount_type, discount_value, status, valid_until) VALUES
('WELCOME10', 'PERCENTAGE', 10, 'ACTIVE', '2026-12-31 23:59:59'),
('SAVE100K', 'FIXED_AMOUNT', 100000, 'ACTIVE', '2026-12-31 23:59:59'),
('SUMMER15', 'PERCENTAGE', 15, 'ACTIVE', '2026-08-31 23:59:59');

INSERT INTO cart_items (cart_item_id, user_id, product_id, quantity) VALUES
('cart-1', 'user-1', 'nike-air-force-1-white', 1),
('cart-2', 'user-1', 'jordan-1-chicago', 1),
('cart-3', 'user-2', 'new-balance-550-white', 2),
('cart-4', 'user-3', 'converse-chuck-70-high', 3);

INSERT INTO orders (order_id, user_id, status, coupon_code, subtotal_price, shipping_fee, total_price, shipping_address, payment_method) VALUES
('ord-1', 'user-1', 'PENDING', 'WELCOME10', 8480000, 50000, 7682000, '12 Nguyen Hue, District 1, Ho Chi Minh City', 'COD'),
('ord-2', 'user-2', 'SHIPPED', 'SAVE100K', 6580000, 30000, 6510000, '45 Le Loi, District 3, Ho Chi Minh City', 'BANK_TRANSFER'),
('ord-3', 'user-3', 'COMPLETED', NULL, 5670000, 25000, 5695000, '88 Tran Hung Dao, Hai Chau, Da Nang', 'COD');

INSERT INTO orders_items (order_item_id, order_id, product_id, quantity, unit_price) VALUES
('order-item-1', 'ord-1', 'nike-air-force-1-white', 1, 2590000),
('order-item-2', 'ord-1', 'jordan-1-chicago', 1, 5890000),
('order-item-3', 'ord-2', 'jordan-4-white-cement', 1, 6790000),
('order-item-4', 'ord-3', 'converse-chuck-70-high', 2, 1890000),
('order-item-5', 'ord-3', 'new-balance-550-white', 1, 3290000);