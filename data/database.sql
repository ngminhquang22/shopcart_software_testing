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