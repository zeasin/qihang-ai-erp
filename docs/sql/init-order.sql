-- ====== 订单管理表 (PostgreSQL) ======

-- ====================================================================
-- 订单主表
-- ====================================================================
DROP TABLE IF EXISTS o_order_item;
DROP TABLE IF EXISTS o_order;

CREATE TABLE o_order (
    id BIGSERIAL PRIMARY KEY,
    order_num VARCHAR(50) NOT NULL,
    order_mode INT DEFAULT 1,
    order_status INT DEFAULT 0,
    goods_amount DECIMAL(10,2) DEFAULT 0,
    amount DECIMAL(10,2) DEFAULT 0,
    payment DECIMAL(10,2) DEFAULT 0,
    post_fee DECIMAL(10,2) DEFAULT 0,
    discount_amount DECIMAL(10,2) DEFAULT 0,
    receiver_name VARCHAR(100),
    receiver_mobile VARCHAR(20),
    province VARCHAR(50),
    city VARCHAR(50),
    town VARCHAR(50),
    address VARCHAR(200),
    remark VARCHAR(500),
    order_time TIMESTAMP,
    merchant_id BIGINT DEFAULT 0,
    create_by VARCHAR(25),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(25),
    update_time TIMESTAMP,
    logistics_company VARCHAR(100) DEFAULT '',
    logistics_no VARCHAR(100) DEFAULT '',
    warehouse_id BIGINT DEFAULT 0,
    warehouse_name VARCHAR(100) DEFAULT '',
    ship_time TIMESTAMP
);

-- ====================================================================
-- 订单明细表
-- ====================================================================
CREATE TABLE o_order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    order_num VARCHAR(50),
    goods_id BIGINT DEFAULT 0,
    goods_sku_id BIGINT DEFAULT 0,
    goods_title VARCHAR(200),
    goods_img VARCHAR(500),
    goods_num VARCHAR(50),
    goods_spec VARCHAR(500),
    sku_num VARCHAR(100),
    goods_price DECIMAL(10,2) DEFAULT 0,
    quantity INT DEFAULT 1,
    item_amount DECIMAL(10,2) DEFAULT 0,
    discount_amount DECIMAL(10,2) DEFAULT 0,
    payment DECIMAL(10,2) DEFAULT 0,
    remark VARCHAR(500),
    refund_count INT DEFAULT 0,
    refund_status INT DEFAULT 1,
    create_by VARCHAR(25),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(25),
    update_time TIMESTAMP
);