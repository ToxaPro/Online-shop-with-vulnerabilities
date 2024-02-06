--liquibase formatted sql
--changeset id: 2024.01.14-create-tables
CREATE TABLE IF NOT EXISTS customers (
    username VARCHAR(64),
    password TEXT NOT NULL,
    surname TEXT NOT NULL,
    `name` TEXT NOT NULL,
    second_name TEXT,
    phone TEXT NOT NULL,
    email TEXT,
    address TEXT NOT NULL,
    PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT,
    `name` TEXT NOT NULL,
    description TEXT NOT NULL,
    price DOUBLE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT,
    create_date DATE,
    finish_date DATE,
    status TEXT,
    customer VARCHAR(64) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (customer) REFERENCES customers(username)
);

CREATE TABLE IF NOT EXISTS positions (
    id BIGINT AUTO_INCREMENT,
    product BIGINT,
    order_ BIGINT,
    amount INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (product) REFERENCES products(id),
    FOREIGN KEY (order_) REFERENCES orders(id)
);
