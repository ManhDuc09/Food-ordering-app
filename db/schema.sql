-- KFCClone Database Schema

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;

-- Users
CREATE TABLE users (
    user_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    password_hash TEXT NOT NULL,
    role VARCHAR(50) DEFAULT 'customer',
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    branch_id UUID
);

-- Branches
CREATE TABLE branches (
    branch_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    is_open BOOLEAN DEFAULT true,
    latitude NUMERIC(9,6),
    longitude NUMERIC(9,6)
);

-- Categories
CREATE TABLE categories (
    category_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    image_url TEXT
);

-- Products
CREATE TABLE products (
    product_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image_url TEXT,
    price NUMERIC(12,2) DEFAULT 0,
    is_available BOOLEAN DEFAULT true,
    priority INT DEFAULT 0
);

-- Product ↔ Category (many-to-many)
CREATE TABLE product_categories (
    product_id UUID NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES categories(category_id) ON DELETE CASCADE,
    PRIMARY KEY (product_id, category_id)
);

-- Add-ons
CREATE TABLE add_ons (
    add_on_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price NUMERIC(10,2) NOT NULL
);

-- Addresses
CREATE TABLE addresses (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID REFERENCES users(user_id) ON DELETE CASCADE,
    street TEXT NOT NULL,
    city VARCHAR(100),
    is_default BOOLEAN DEFAULT false,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);

-- Orders
CREATE TABLE orders (
    order_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID REFERENCES users(user_id) ON DELETE SET NULL,
    branch_id UUID REFERENCES branches(branch_id),
    status VARCHAR(50) DEFAULT 'pending',
    total_amount NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    delivery_name VARCHAR(255),
    delivery_phone VARCHAR(20),
    delivery_address TEXT
);

-- Order Items
CREATE TABLE order_items (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID REFERENCES orders(order_id) ON DELETE CASCADE,
    product_id UUID REFERENCES products(product_id),
    variant_id UUID,
    quantity INT DEFAULT 1 NOT NULL
);

-- Order Item ↔ Add-on (many-to-many)
CREATE TABLE order_customizations (
    order_item_id UUID NOT NULL REFERENCES order_items(id) ON DELETE CASCADE,
    add_on_id UUID NOT NULL REFERENCES add_ons(add_on_id) ON DELETE CASCADE,
    PRIMARY KEY (order_item_id, add_on_id)
);

-- Payments
CREATE TABLE payments (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID UNIQUE REFERENCES orders(order_id) ON DELETE CASCADE,
    method VARCHAR(50),
    status VARCHAR(50),
    paid_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Foreign key: users → branches
ALTER TABLE users ADD CONSTRAINT users_branch_id_fkey
    FOREIGN KEY (branch_id) REFERENCES branches(branch_id);
