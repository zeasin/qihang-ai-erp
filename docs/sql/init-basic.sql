-- ====== 渠道管理相关表 (PostgreSQL) ======
DROP TABLE IF EXISTS o_shop;
DROP TABLE IF EXISTS erp_merchant;
DROP TABLE IF EXISTS o_shop_platform;

-- 渠道（平台）表
CREATE TABLE o_shop_platform (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255), code VARCHAR(255),
    app_key VARCHAR(255), app_secret VARCHAR(255),
    redirect_uri VARCHAR(255), server_url VARCHAR(255),
    status INT DEFAULT 0, sort INT DEFAULT 0
);

-- 店铺表
CREATE TABLE o_shop (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type INT NOT NULL,
    url VARCHAR(100),
    sort INT DEFAULT 9,
    status INT DEFAULT 1,
    remark VARCHAR(50),
    seller_id VARCHAR(55),
    app_key VARCHAR(100),
    app_secret VARCHAR(100),
    access_token VARCHAR(500),
    expires_in BIGINT,
    access_token_begin BIGINT,
    refresh_token VARCHAR(500),
    refresh_token_timeout BIGINT,
    api_request_url VARCHAR(100),
    api_callback_url VARCHAR(255),
    manage_user_id BIGINT,
    shop_group_id BIGINT,
    region_id BIGINT DEFAULT 1,
    merchant_id BIGINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by VARCHAR(25),
    update_time TIMESTAMP,
    update_by VARCHAR(25),
    api_status INT DEFAULT 0,
    province VARCHAR(22),
    city VARCHAR(22),
    district VARCHAR(22),
    address VARCHAR(255),
    contact VARCHAR(30),
    phone VARCHAR(40),
    seller_num VARCHAR(55),
    allow_inventory_share SMALLINT DEFAULT 0
);

-- 商户表
CREATE TABLE erp_merchant (
    id BIGSERIAL PRIMARY KEY,
    login_name VARCHAR(30) NOT NULL,
    nick_name VARCHAR(30),
    mobile VARCHAR(15) DEFAULT '',
    password VARCHAR(100) DEFAULT '',
    status CHAR(1) DEFAULT '0',
    name VARCHAR(255),
    number VARCHAR(25),
    usci VARCHAR(255),
    faren VARCHAR(25),
    link_man VARCHAR(25),
    address VARCHAR(255),
    supplier_ids VARCHAR(2000),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 初始数据
INSERT INTO o_shop_platform (id, name, code, status, sort) VALUES
(100, '淘宝天猫', 'TMALL', 0, 0),
(200, '京东POP', 'JD-POP', 0, 1),
(300, '拼多多', 'PDD', 0, 2),
(400, '抖店', 'DOUDIAN', 0, 3),
(500, '微信小店', 'WEISHOP', 0, 4),
(600, '快手', 'KUAISHOU', 0, 5),
(700, '小红书', 'XHS', 0, 6),
(999, '线下门店', 'OFFLINE', 0, 99);

INSERT INTO erp_merchant (id, login_name, name, number, status) VALUES
(1, 'qihang', '启航', 'QIHANG', '0'),
(2, 'pdd74583921645', '拼多多商户', '745839216', '0');

-- ====================================================================
-- 基础数据：商品分类
-- ====================================================================
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

-- 初始数据
INSERT INTO o_goods_category (id, number, name, parent_id, sort, create_by) VALUES
(1, 'ZM', '照明', 0, 0, 'admin'),
(2, 'LEDDX', 'LED灯芯', 1, 0, 'admin'),
(3, 'LEDDP', 'LED灯泡', 1, 1, 'admin'),
(4, 'SHOUSHI', '首饰', 0, 0, 'admin'),
(5, 'SS001', '手镯金包银', 4, 0, 'admin');

SELECT setval('o_goods_category_id_seq', (SELECT MAX(id) FROM o_goods_category));
SELECT setval('o_shop_platform_id_seq', (SELECT MAX(id) FROM o_shop_platform));
SELECT setval('erp_merchant_id_seq', (SELECT MAX(id) FROM erp_merchant));
