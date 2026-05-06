-- Enable UUID support (specific to PostgreSQL)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. Users & addresses
CREATE TABLE IF NOT EXISTS users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    password_hash TEXT NOT NULL,
    role VARCHAR(50) DEFAULT 'customer',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS addresses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(user_id) ON DELETE CASCADE,
    street TEXT NOT NULL,
    city VARCHAR(100),
    is_default BOOLEAN DEFAULT false
);

-- 2. Restaurant Structure
CREATE TABLE IF NOT EXISTS branches (
    branch_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    address TEXT,
    is_open BOOLEAN DEFAULT true
);

-- 3. Menu System
CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    product_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category_id INT REFERENCES categories(id) ON DELETE SET NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS product_variants (
    variant_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID REFERENCES products(product_id) ON DELETE CASCADE,
    name VARCHAR(100), -- e.g., 'Regular', 'Large'
    price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS add_ons (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

-- 4. Order Logic
CREATE TABLE IF NOT EXISTS orders (
    order_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(user_id) ON DELETE SET NULL,
    branch_id UUID REFERENCES branches(branch_id),
    status VARCHAR(50) DEFAULT 'pending',
    total_amount DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID REFERENCES orders(order_id) ON DELETE CASCADE,
    variant_id UUID REFERENCES product_variants(variant_id),
    quantity INT NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS order_customizations (
    order_item_id UUID REFERENCES order_items(id) ON DELETE CASCADE,
    add_on_id INT REFERENCES add_ons(id) ON DELETE CASCADE,
    PRIMARY KEY (order_item_id, add_on_id)
);

-- 5. Payments
CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID UNIQUE REFERENCES orders(order_id) ON DELETE CASCADE,
    method VARCHAR(50),
    status VARCHAR(50),
    paid_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE products DROP COLUMN category_id;


CREATE TABLE IF NOT EXISTS product_categories (
    product_id UUID REFERENCES products(product_id) ON DELETE CASCADE,
    category_id INT REFERENCES categories(id) ON DELETE CASCADE,
    PRIMARY KEY (product_id, category_id)
);

ALTER TABLE categories
ALTER COLUMN id TYPE UUID
USING gen_random_uuid();