-- ====== 商品基础数据表 (PostgreSQL) ======

-- ====================================================================
-- 商品品牌表
-- ====================================================================
DROP TABLE IF EXISTS o_goods_brand;
CREATE TABLE o_goods_brand (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    num VARCHAR(25),
    status INT DEFAULT 1,
    create_by VARCHAR(25),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(25),
    update_time TIMESTAMP
);

-- 初始数据
INSERT INTO o_goods_brand (id, name, num, status, create_by) VALUES
(1, '飞利浦', 'PHILIPS', 1, 'admin'),
(2, '欧普', 'OPPLE', 1, 'admin'),
(3, '雷士', 'NVC', 1, 'admin');

SELECT setval('o_goods_brand_id_seq', (SELECT MAX(id) FROM o_goods_brand));

-- ====================================================================
-- 基础数据：商品分类
-- ====================================================================
DROP TABLE IF EXISTS o_goods_category_attribute_value;
DROP TABLE IF EXISTS o_goods_category_attribute;
DROP TABLE IF EXISTS o_goods_category;

CREATE TABLE o_goods_category (
    id BIGSERIAL PRIMARY KEY,
    number VARCHAR(18),
    name VARCHAR(20) NOT NULL,
    remark VARCHAR(50),
    parent_id BIGINT DEFAULT 0,
    path VARCHAR(45) DEFAULT '',
    sort INT DEFAULT 0,
    image VARCHAR(100),
    is_delete SMALLINT DEFAULT 0,
    create_by VARCHAR(25),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(25),
    update_time TIMESTAMP,
    merchant_id BIGINT DEFAULT 0
);

INSERT INTO o_goods_category (id, number, name, parent_id, sort, create_by) VALUES
(1, 'ZM', '照明', 0, 0, 'admin'),
(2, 'LEDDX', 'LED灯芯', 1, 0, 'admin'),
(3, 'LEDDP', 'LED灯泡', 1, 1, 'admin'),
(4, 'SHOUSHI', '首饰', 0, 0, 'admin'),
(5, 'SS001', '手镯金包银', 4, 0, 'admin');

SELECT setval('o_goods_category_id_seq', (SELECT MAX(id) FROM o_goods_category));

-- ====================================================================
-- 分类规格属性
-- ====================================================================
DROP TABLE IF EXISTS o_goods_category_attribute;
CREATE TABLE o_goods_category_attribute (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    type INT DEFAULT 0,
    title VARCHAR(45),
    code VARCHAR(10)
);

INSERT INTO o_goods_category_attribute (id, category_id, type, title, code) VALUES
(1, 1, 1, '瓦数', 'color'),
(2, 4, 1, '颜色', 'color');

SELECT setval('o_goods_category_attribute_id_seq', (SELECT MAX(id) FROM o_goods_category_attribute));

-- ====================================================================
-- 规格属性值
-- ====================================================================
DROP TABLE IF EXISTS o_goods_category_attribute_value;
CREATE TABLE o_goods_category_attribute_value (
    id BIGSERIAL PRIMARY KEY,
    category_attribute_id BIGINT,
    value VARCHAR(45),
    sku_code VARCHAR(10),
    order_num INT DEFAULT 0,
    is_delete INT DEFAULT 0
);

INSERT INTO o_goods_category_attribute_value (id, category_attribute_id, value, sku_code, order_num, is_delete) VALUES
(1, 1, '15W', '15W', 0, 0),
(2, 1, '18W', '18W', 0, 0),
(3, 1, '24W', '24W', 0, 0),
(4, 1, '36W', '36W', 0, 0),
(5, 1, '72W', '72W', 0, 0),
(6, 2, '默认', '00', 0, 0);

SELECT setval('o_goods_category_attribute_value_id_seq', (SELECT MAX(id) FROM o_goods_category_attribute_value));

-- ====================================================================
-- 供应商档案
-- ====================================================================
DROP TABLE IF EXISTS erp_supplier;
CREATE TABLE erp_supplier (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50),
    number VARCHAR(18),
    is_shipper INT DEFAULT 0,
    warehouse_id BIGINT DEFAULT 0,
    link_man VARCHAR(10),
    contact VARCHAR(15),
    province VARCHAR(20),
    city VARCHAR(20),
    county VARCHAR(20),
    address VARCHAR(100),
    usci VARCHAR(50),
    bl VARCHAR(255),
    bl_period VARCHAR(30),
    bl_faren VARCHAR(25),
    bank VARCHAR(100),
    bank_account_name VARCHAR(100),
    bank_account VARCHAR(100),
    purchaser_name VARCHAR(50),
    remark VARCHAR(100),
    disable INT DEFAULT 0,
    is_delete INT DEFAULT 0,
    merchant_id BIGINT DEFAULT 0,
    create_by VARCHAR(25),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(25),
    update_time TIMESTAMP
);

INSERT INTO erp_supplier (id, name, number, link_man, contact, address, create_by) VALUES
(1, '广东中山照明供应商', 'ZMGY', '张三', '13800138001', '广东省中山市古镇镇', 'admin'),
(2, '深圳电子元器件供应商', 'SZDZ', '李四', '13900139002', '广东省深圳市福田区', 'admin');

SELECT setval('erp_supplier_id_seq', (SELECT MAX(id) FROM erp_supplier));

-- ====================================================================
-- 采购承运商
-- ====================================================================
DROP TABLE IF EXISTS erp_purchase_logistics;
CREATE TABLE erp_purchase_logistics (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50),
    name VARCHAR(100) NOT NULL,
    remark VARCHAR(255),
    status INT DEFAULT 0,
    merchant_id BIGINT DEFAULT 0,
    shop_id BIGINT DEFAULT 0
);

INSERT INTO erp_purchase_logistics (id, code, name, status) VALUES
(1, 'SF', '顺丰快递', 1),
(2, 'JD122', '京东物流', 1),
(3, 'ZTO', '中通快递', 1),
(4, 'YTO', '圆通速递', 1);

SELECT setval('erp_purchase_logistics_id_seq', (SELECT MAX(id) FROM erp_purchase_logistics));