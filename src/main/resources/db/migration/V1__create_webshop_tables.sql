CREATE TABLE products (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    price_in_cent INT NOT NULL
);
CREATE TABLE product_tags (
    product_id VARCHAR(255) NOT NULL,
    tag_index INT NOT NULL,
    tag VARCHAR(255) NOT NULL,
    PRIMARY KEY (product_id, tag_index),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
CREATE TABLE customers (
    id VARCHAR(255) PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);
CREATE TABLE shop_orders (
    id VARCHAR(255) PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    order_time DATETIME(6) NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);
CREATE TABLE order_positions (
    id VARCHAR(255) PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL,
    product_id VARCHAR(255) NOT NULL,
    quantity BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES shop_orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
