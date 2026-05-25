-- Enable UUID support
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ==========================================
-- 1. USER & LOCATION MANAGEMENT
-- ==========================================
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

CREATE TABLE IF NOT EXISTS branches (
    branch_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    address TEXT,
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6),
    is_open BOOLEAN DEFAULT true
);

-- ==========================================
-- 2. THE MENU (Simplified Version)
-- ==========================================
CREATE TABLE IF NOT EXISTS categories (
    category_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    product_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image_url TEXT,
    price DECIMAL(12,2) DEFAULT 0,
    is_available BOOLEAN DEFAULT true,
    priority INT DEFAULT 0
);

-- The "Link" table for Many-to-Many
CREATE TABLE IF NOT EXISTS product_categories (
    product_id UUID REFERENCES products(product_id) ON DELETE CASCADE,
    category_id UUID REFERENCES categories(category_id) ON DELETE CASCADE,
    PRIMARY KEY (product_id, category_id)
);

CREATE TABLE IF NOT EXISTS add_ons (
    add_on_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

-- ==========================================
-- 3. ORDERS & PAYMENTS
-- ==========================================
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
    product_id UUID REFERENCES products(product_id), -- Linked directly now (no variants)
    quantity INT NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS order_customizations (
    order_item_id UUID REFERENCES order_items(id) ON DELETE CASCADE,
    add_on_id UUID REFERENCES add_ons(add_on_id) ON DELETE CASCADE,
    PRIMARY KEY (order_item_id, add_on_id)
);

CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID UNIQUE REFERENCES orders(order_id) ON DELETE CASCADE,
    method VARCHAR(50),
    status VARCHAR(50),
    paid_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE order_items
ADD COLUMN product_id UUID REFERENCES products(product_id);

-- Add delivery info to orders
ALTER TABLE orders ADD COLUMN IF NOT EXISTS delivery_name VARCHAR(255);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS delivery_phone VARCHAR(20);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS delivery_address TEXT;