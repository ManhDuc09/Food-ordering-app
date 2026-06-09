-- Seed data
-- Add INSERT statements here to pre-populate the database
-- Branches
INSERT INTO branches (branch_id, name, address, is_open, latitude, longitude) VALUES
('ed7bed03-c46e-40ba-ab73-9d0a3f833ed4', 'KFC Nguyễn Huệ', '32 Nguyễn Huệ, Quận 1, TP.HCM', true, 10.773700, 106.703000),
('442a1691-9f68-4c39-a85a-54f87f259884', 'KFC Vincom Center', '70-72 Lê Thánh Tôn, Quận 1, TP.HCM', true, 10.777300, 106.701200),
('f200ae3c-83a6-4a39-b55d-93a1f8a9e798', 'KFC Gò Vấp', '1A Nguyễn Kiệm, Gò Vấp, TP.HCM', true, 10.819800, 106.679500),
('70b2b1e3-0bf8-4772-adf2-88a490d2c1b9', 'KFC Bình Thạnh', '122 Đinh Tiên Hoàng, Bình Thạnh, TP.HCM', true, 10.801100, 106.714300),
('d95d2038-68bd-424d-bd19-63e76e41302b', 'KFC Tân Bình', '246 Hoàng Văn Thụ, Tân Bình, TP.HCM', true, 10.799000, 106.665000),
('d79d7a57-cf09-4906-b81f-91476aa7d054', 'KFC Quận 7', '1058 Nguyễn Văn Linh, Quận 7, TP.HCM', true, 10.731700, 106.701000),
('b4889753-684e-4000-9e59-10d7295d5fe9', 'KFC Thủ Đức', '216 Võ Văn Ngân, Thủ Đức, TP.HCM', false, 10.850000, 106.771700);

-- Users (passwords are bcrypt hashed)
INSERT INTO users (user_id, full_name, email, phone_number, password_hash, role, created_at, branch_id) VALUES
('71feb3ee-0b81-46f4-9881-7dadb7ee0d5c', 'Customer A', 'duccute3@gmail.com', '123123', '$2a$10$XmxTLogsIhjJoSx32Cf64unfcyA6ufAtYAcEX0DG05nH8txfyMdAu', 'customer', '2026-05-10 15:18:19.029477+00', NULL),
('294bca7a-a666-4f36-852e-4ab9bdc32b4f', 'Customer B', 'duccute123@gmail.com', '12312312312', '$2a$10$uu8EgD4tmGnjEpmu3ZdNyOs.k6kVyC2BNQ9uKXzx9N08Lzwl5wdCy', 'customer', '2026-05-10 15:19:47.348526+00', NULL),
('0c4e49d3-8e9a-4bd4-89f4-5c421dd571c2', 'Branch Manager', 'manager@kfc.com', '0901234567', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'branch_manager', '2026-05-26 14:52:34.642911+00', 'ed7bed03-c46e-40ba-ab73-9d0a3f833ed4'),
('81862e38-e381-45c7-80c8-aff818dd5a5d', 'Branch Manager 2', 'duccute12@gmail.com', '0339391423', '$2a$10$GJm4E4L9w.jqIq0HJyhCd.EFitdHAOBfYhDPf28Pt6LI7NRvPTxeq', 'branch_manager', '2026-05-26 14:56:40.219907+00', '442a1691-9f68-4c39-a85a-54f87f259884'),
('8bab6d51-52c1-4edc-93f6-f6960b628ffd', 'Admin', 'duccute1234@gmail.com', '123123123123', '$2a$10$APgMVfFVNmGBn6z.xMkZceq8f3nfD21EMmW2Bl7V7oLoZOgMZCKy6', 'admin', '2026-05-27 14:46:39.986712+00', NULL);

-- Categories
INSERT INTO categories (category_id, name) VALUES
('72161a3b-1523-4b55-b640-f818566f98f9', 'Ưu Đãi'),
('9d9bf59b-66ce-4fdf-b424-39bea088248c', 'Gà Rán'),
('5ace8997-f6fb-43ce-8a34-b814a70f10a5', 'Burger - Cơm'),
('c7d6b68d-dbed-43f2-91cc-0b28c1e7243c', 'Thức Uống');

-- Products
INSERT INTO products (product_id, name, description, image_url, is_available, priority, price) VALUES
('916cf0ae-54a2-42ce-8ba6-87530370da30', 'Combo Gà Rán A', '2 Miếng Gà + 1 Khoai tây chiên + 1 Pepsi', 'https://static.kfcvietnam.com.vn/images/items/lg/D-CBO-G-A.jpg', true, 0, 222.00),
('a7816cb8-fc7c-4598-8681-2ab1b7800510', 'Pepsi Lon', 'Nước giải khát Pepsi', 'https://static.kfcvietnam.com.vn/images/items/lg/Pepsi-Can.jpg', true, 0, 22222.00),
('7fb6c016-a1f2-457f-9e93-290f2cdbb2cb', 'Gà Rán (1 Miếng)', 'Gà rán truyền thống hoặc giòn cay', 'https://static.kfcvietnam.com.vn/images/items/lg/1-PC.jpg', true, 0, 9999.00),
('83b61c6e-25fc-4c1b-9725-c86b4be1c9ef', 'Burger Zinger', 'Burger gà giòn cay đặc trưng', 'https://res.cloudinary.com/dwe496fta/image/upload/v1779977613/y0xhbvbusjsw6xmzefca.webp', true, 0, 112.00);

-- Product Categories
INSERT INTO product_categories (product_id, category_id) VALUES
('7fb6c016-a1f2-457f-9e93-290f2cdbb2cb', '9d9bf59b-66ce-4fdf-b424-39bea088248c'),
('916cf0ae-54a2-42ce-8ba6-87530370da30', '72161a3b-1523-4b55-b640-f818566f98f9'),
('a7816cb8-fc7c-4598-8681-2ab1b7800510', 'c7d6b68d-dbed-43f2-91cc-0b28c1e7243c'),
('83b61c6e-25fc-4c1b-9725-c86b4be1c9ef', '5ace8997-f6fb-43ce-8a34-b814a70f10a5');

-- Addresses
INSERT INTO addresses (id, user_id, street, city, is_default, latitude, longitude) VALUES
('f227cfd9-a925-4ef1-92cc-9fc8c97b301d', '294bca7a-a666-4f36-852e-4ab9bdc32b4f', '123123', '12312312', false, NULL, NULL),
('fb23ae69-f9f4-4d30-bd45-769a55386f76', '71feb3ee-0b81-46f4-9881-7dadb7ee0d5c', '213123', '123123123', false, NULL, NULL),
('8278645a-4958-48be-932c-2207884f0138', '294bca7a-a666-4f36-852e-4ab9bdc32b4f', 'Đường Nguyễn Chí Thanh', 'Thành phố Thủ Đức', false, NULL, NULL);

-- Orders
INSERT INTO orders (order_id, user_id, branch_id, status, total_amount, created_at, delivery_name, delivery_phone, delivery_address) VALUES
('ad12b36c-ca36-4aef-83c8-9b0d60569a61', '294bca7a-a666-4f36-852e-4ab9bdc32b4f', NULL, 'pending', 222.00, '2026-05-18 14:22:01.237316+00', NULL, NULL, NULL),
('da6f4e71-b9ef-4138-aab0-11bf7935fbc7', '294bca7a-a666-4f36-852e-4ab9bdc32b4f', NULL, 'pending', 222.00, '2026-05-18 14:22:18.286849+00', NULL, NULL, NULL),
('16504f5c-4af0-4397-836f-a1ef40760521', '294bca7a-a666-4f36-852e-4ab9bdc32b4f', NULL, 'pending', 222.00, '2026-05-23 14:53:50.896808+00', NULL, NULL, NULL),
('289792b4-19c4-4570-ba94-79ae4400dba7', '294bca7a-a666-4f36-852e-4ab9bdc32b4f', '442a1691-9f68-4c39-a85a-54f87f259884', 'delivered', 222.00, '2026-05-26 15:01:54.40263+00', '12312312', '123123123', 'sadas'),
('9b922c30-da98-4252-be0b-aaf0794ff9bc', '294bca7a-a666-4f36-852e-4ab9bdc32b4f', 'ed7bed03-c46e-40ba-ab73-9d0a3f833ed4', 'cancelled', 560.00, '2026-05-28 14:48:20.649976+00', '123123', '123123123', '123123123123'),
('fd0de441-3387-4cea-84b0-0c754c8b309a', '294bca7a-a666-4f36-852e-4ab9bdc32b4f', NULL, 'cancelled', 666.00, '2026-05-25 15:54:39.017188+00', '123123', '3123', '21312'),
('f3a5dc1a-bc0e-49a4-a098-ac4b8ac76ad6', '294bca7a-a666-4f36-852e-4ab9bdc32b4f', 'ed7bed03-c46e-40ba-ab73-9d0a3f833ed4', 'pending', 123123.12, '2026-05-28 15:02:40.241556+00', '12312312312312', '123123123', '3123'),
('28d48ab1-2932-413f-872f-0985b5d26467', '294bca7a-a666-4f36-852e-4ab9bdc32b4f', '442a1691-9f68-4c39-a85a-54f87f259884', 'delivered', 22222.00, '2026-05-28 15:12:32.480391+00', '312', '312312', '3123123123');

-- Order Items
INSERT INTO order_items (id, order_id, variant_id, quantity, product_id) VALUES
('4a66b5ca-90d6-4282-acf4-31a3198ed3ac', 'ad12b36c-ca36-4aef-83c8-9b0d60569a61', NULL, 1, '916cf0ae-54a2-42ce-8ba6-87530370da30'),
('a2bac234-b421-4dc6-bf88-452c95996ac5', 'da6f4e71-b9ef-4138-aab0-11bf7935fbc7', NULL, 1, '916cf0ae-54a2-42ce-8ba6-87530370da30'),
('bc1db035-5cbc-43da-9fe4-601918671301', '16504f5c-4af0-4397-836f-a1ef40760521', NULL, 1, '916cf0ae-54a2-42ce-8ba6-87530370da30'),
('8ced0ac5-9b84-4b96-a293-086971b90e26', 'fd0de441-3387-4cea-84b0-0c754c8b309a', NULL, 3, '916cf0ae-54a2-42ce-8ba6-87530370da30'),
('52dbfd5e-499c-418b-9f42-f46bcef033d7', '289792b4-19c4-4570-ba94-79ae4400dba7', NULL, 1, '916cf0ae-54a2-42ce-8ba6-87530370da30'),
('4c41c8dc-d15b-44f2-b6ba-0b5f577ece4b', '9b922c30-da98-4252-be0b-aaf0794ff9bc', NULL, 5, '83b61c6e-25fc-4c1b-9725-c86b4be1c9ef'),
('9d5a392b-c95b-4ec0-aace-35ff6e2d8ee8', 'f3a5dc1a-bc0e-49a4-a098-ac4b8ac76ad6', NULL, 1, '916cf0ae-54a2-42ce-8ba6-87530370da30'),
('36bff4e7-30bf-4868-a2d1-9bf65fbaa990', '28d48ab1-2932-413f-872f-0985b5d26467', NULL, 1, 'a7816cb8-fc7c-4598-8681-2ab1b7800510');

-- Payments
INSERT INTO payments (id, order_id, method, status, paid_at) VALUES
('b7a147c5-3050-4738-8b4e-a813c576ee0d', '16504f5c-4af0-4397-836f-a1ef40760521', 'COD', 'paid', '2026-05-24 16:05:22.629667+00'),
('e7ffc3c1-ea6a-412d-97b9-6e97e3e103a2', 'fd0de441-3387-4cea-84b0-0c754c8b309a', 'COD', 'paid', '2026-05-25 15:54:39.033246+00'),
('97e42a88-b4f2-446a-b152-e964b31cedb8', '289792b4-19c4-4570-ba94-79ae4400dba7', 'COD', 'pending', NULL),
('01bd46c5-5aaf-468f-bda4-650483302a73', '9b922c30-da98-4252-be0b-aaf0794ff9bc', 'VNPAY', 'pending', NULL),
('be6688f1-d2c0-44dc-9b6d-1ffaeb4ccc6f', 'f3a5dc1a-bc0e-49a4-a098-ac4b8ac76ad6', 'VNPAY', 'paid', '2026-05-28 15:03:26.859395+00'),
('3d23e053-5295-4832-877f-b4dfd9718d79', '28d48ab1-2932-413f-872f-0985b5d26467', 'COD', 'pending', NULL);

-- ==========================================
-- NEW CATEGORIES & PRODUCTS
-- ==========================================

INSERT INTO categories (category_id, name) VALUES
('ca000001-0000-0000-0000-000000000001', 'Món Mới'),
('ca000001-0000-0000-0000-000000000002', 'Combo 1 Người'),
('ca000001-0000-0000-0000-000000000003', 'Combo Nhóm'),
('ca000001-0000-0000-0000-000000000004', 'Gà Rán - Gà Quay'),
('ca000001-0000-0000-0000-000000000005', 'Burger - Cơm - Mì Ý'),
('ca000001-0000-0000-0000-000000000006', 'Thức Ăn Nhẹ'),
('ca000001-0000-0000-0000-000000000007', 'Thức Uống & Tráng Miệng')
ON CONFLICT (category_id) DO NOTHING;

INSERT INTO products (product_id, name, description, image_url, is_available, priority, price) VALUES
-- Món Mới
('bb000001-0000-0000-0000-000000000001', 'Twister Gà Giòn', 'Bánh mì cuộn gà giòn sốt mayonnaise rau củ', 'https://static.kfcvietnam.com.vn/images/items/lg/Twister.jpg', true, 1, 59000.00),
('bb000001-0000-0000-0000-000000000002', 'Gà Popcorn', 'Những miếng gà nhỏ giòn tan vị phô mai', 'https://static.kfcvietnam.com.vn/images/items/lg/Popcorn-Chicken.jpg', true, 1, 45000.00),
-- Combo 1 Người
('bb000001-0000-0000-0000-000000000003', 'Combo 1 Người Gà Rán', '1 Miếng Gà Rán + Khoai Tây Chiên Vừa + Pepsi Vừa', 'https://static.kfcvietnam.com.vn/images/items/lg/CBO-1.jpg', true, 2, 89000.00),
('bb000001-0000-0000-0000-000000000004', 'Combo 1 Người Burger', '1 Burger Zinger + Khoai Tây Chiên Vừa + Pepsi Vừa', 'https://static.kfcvietnam.com.vn/images/items/lg/CBO-BG.jpg', true, 2, 99000.00),
-- Combo Nhóm
('bb000001-0000-0000-0000-000000000005', 'Combo Gia Đình', '4 Miếng Gà + 2 Khoai Tây Chiên Lớn + 2 Pepsi Lớn', 'https://static.kfcvietnam.com.vn/images/items/lg/D-CBO-G-A.jpg', true, 3, 299000.00),
('bb000001-0000-0000-0000-000000000006', 'Combo Bạn Bè', '2 Miếng Gà + 1 Burger + 2 Khoai Tây Vừa + 2 Pepsi Vừa', 'https://static.kfcvietnam.com.vn/images/items/lg/D-CBO-G-A.jpg', true, 3, 199000.00),
-- Gà Rán - Gà Quay
('bb000001-0000-0000-0000-000000000007', 'Gà Rán 2 Miếng', '2 Miếng Gà Rán truyền thống hoặc giòn cay', 'https://static.kfcvietnam.com.vn/images/items/lg/2-PC.jpg', true, 4, 79000.00),
('bb000001-0000-0000-0000-000000000008', 'Gà Quay Sốt Cay', 'Gà quay nguyên con sốt cay đặc biệt', 'https://static.kfcvietnam.com.vn/images/items/lg/Roasted-Chicken.jpg', true, 4, 129000.00),
-- Burger - Cơm - Mì Ý
('bb000001-0000-0000-0000-000000000009', 'Cơm Gà Rán', 'Cơm trắng ăn kèm gà rán và rau củ', 'https://static.kfcvietnam.com.vn/images/items/lg/Rice-Chicken.jpg', true, 5, 69000.00),
('bb000001-0000-0000-0000-000000000010', 'Mì Ý Gà Sốt Cà Chua', 'Mì Ý sợi nhỏ với gà rán và sốt cà chua đặc biệt', 'https://static.kfcvietnam.com.vn/images/items/lg/Spaghetti.jpg', true, 5, 75000.00),
-- Thức Ăn Nhẹ
('bb000001-0000-0000-0000-000000000011', 'Khoai Tây Chiên', 'Khoai tây chiên vàng giòn rắc muối', 'https://static.kfcvietnam.com.vn/images/items/lg/French-Fries.jpg', true, 6, 35000.00),
('bb000001-0000-0000-0000-000000000012', 'Bánh Mì Que', 'Bánh mì que bơ tỏi nướng giòn', 'https://static.kfcvietnam.com.vn/images/items/lg/Breadstick.jpg', true, 6, 25000.00),
('bb000001-0000-0000-0000-000000000013', 'Bắp Nướng', 'Bắp nướng bơ tươi thơm ngon', 'https://static.kfcvietnam.com.vn/images/items/lg/Corn.jpg', true, 6, 29000.00),
-- Thức Uống & Tráng Miệng
('bb000001-0000-0000-0000-000000000014', 'Trà Đào Vải', 'Trà đào vị vải thơm mát giải nhiệt', 'https://static.kfcvietnam.com.vn/images/items/lg/Peach-Tea.jpg', true, 7, 29000.00),
('bb000001-0000-0000-0000-000000000015', 'Sundae Sô Cô La', 'Kem mềm phủ sốt sô cô la đặc', 'https://static.kfcvietnam.com.vn/images/items/lg/Sundae-Choco.jpg', true, 7, 25000.00)
ON CONFLICT (product_id) DO NOTHING;

INSERT INTO product_categories (product_id, category_id) VALUES
('bb000001-0000-0000-0000-000000000001', 'ca000001-0000-0000-0000-000000000001'),
('bb000001-0000-0000-0000-000000000002', 'ca000001-0000-0000-0000-000000000001'),
('bb000001-0000-0000-0000-000000000003', 'ca000001-0000-0000-0000-000000000002'),
('bb000001-0000-0000-0000-000000000004', 'ca000001-0000-0000-0000-000000000002'),
('bb000001-0000-0000-0000-000000000005', 'ca000001-0000-0000-0000-000000000003'),
('bb000001-0000-0000-0000-000000000006', 'ca000001-0000-0000-0000-000000000003'),
('bb000001-0000-0000-0000-000000000007', 'ca000001-0000-0000-0000-000000000004'),
('bb000001-0000-0000-0000-000000000008', 'ca000001-0000-0000-0000-000000000004'),
('bb000001-0000-0000-0000-000000000009', 'ca000001-0000-0000-0000-000000000005'),
('bb000001-0000-0000-0000-000000000010', 'ca000001-0000-0000-0000-000000000005'),
('bb000001-0000-0000-0000-000000000011', 'ca000001-0000-0000-0000-000000000006'),
('bb000001-0000-0000-0000-000000000012', 'ca000001-0000-0000-0000-000000000006'),
('bb000001-0000-0000-0000-000000000013', 'ca000001-0000-0000-0000-000000000006'),
('bb000001-0000-0000-0000-000000000014', 'ca000001-0000-0000-0000-000000000007'),
('bb000001-0000-0000-0000-000000000015', 'ca000001-0000-0000-0000-000000000007')
ON CONFLICT (product_id, category_id) DO NOTHING;