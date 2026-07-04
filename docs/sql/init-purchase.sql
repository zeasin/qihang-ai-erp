-- ====================================================================
-- 启航AI ERP 采购管理相关表 (PostgreSQL)
-- ====================================================================

-- 采购订单
DROP TABLE IF EXISTS erp_purchase_order;
CREATE TABLE erp_purchase_order (
    id BIGSERIAL PRIMARY KEY,
    order_num VARCHAR(55) NOT NULL,
    supplier_id BIGINT DEFAULT 0,
    supplier_name VARCHAR(255) DEFAULT '',
    total_amount DECIMAL(18,2) DEFAULT 0,
    goods_unit INT DEFAULT 0,
    spec_unit INT DEFAULT 0,
    spec_unit_total INT DEFAULT 0,
    remark VARCHAR(500) DEFAULT '',
    status INT DEFAULT 0,
    order_time TIMESTAMP,
    complete_time TIMESTAMP,
    user_id BIGINT DEFAULT 0,
    user_name VARCHAR(55) DEFAULT '',
    create_by VARCHAR(55) DEFAULT '',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(55) DEFAULT '',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 采购订单明细
DROP TABLE IF EXISTS erp_purchase_order_item;
CREATE TABLE erp_purchase_order_item (
    id BIGSERIAL PRIMARY KEY,
    purchase_order_id BIGINT NOT NULL,
    purchase_order_num VARCHAR(55) DEFAULT '',
    goods_id BIGINT DEFAULT 0,
    goods_num VARCHAR(80) DEFAULT '',
    goods_name VARCHAR(200) DEFAULT '',
    goods_image VARCHAR(500) DEFAULT '',
    sku_id BIGINT DEFAULT 0,
    sku_code VARCHAR(100) DEFAULT '',
    sku_name VARCHAR(100) DEFAULT '',
    quantity INT DEFAULT 0,
    in_quantity INT DEFAULT 0,
    purchase_price DECIMAL(18,2) DEFAULT 0,
    total_amount DECIMAL(18,2) DEFAULT 0,
    remark VARCHAR(500) DEFAULT '',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_epoi_order ON erp_purchase_order_item(purchase_order_id);