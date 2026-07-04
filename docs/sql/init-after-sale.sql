-- ====================================================================
-- 启航AI ERP 售后管理相关表 (PostgreSQL)
-- ====================================================================

-- 售后单
DROP TABLE IF EXISTS erp_after_sale;
CREATE TABLE erp_after_sale (
    id BIGSERIAL PRIMARY KEY,
    after_num VARCHAR(55) NOT NULL,
    order_id BIGINT NOT NULL DEFAULT 0,
    order_num VARCHAR(55) NOT NULL DEFAULT '',
    type INT DEFAULT 1,
    reason VARCHAR(500) DEFAULT '',
    amount DECIMAL(18,2) DEFAULT 0,
    status INT DEFAULT 0,
    apply_time TIMESTAMP,
    complete_time TIMESTAMP,
    remark VARCHAR(500) DEFAULT '',
    create_by VARCHAR(55) DEFAULT '',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(55) DEFAULT '',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 售后单明细
DROP TABLE IF EXISTS erp_after_sale_item;
CREATE TABLE erp_after_sale_item (
    id BIGSERIAL PRIMARY KEY,
    after_sale_id BIGINT NOT NULL,
    after_sale_num VARCHAR(55) DEFAULT '',
    order_item_id BIGINT DEFAULT 0,
    goods_id BIGINT DEFAULT 0,
    goods_num VARCHAR(80) DEFAULT '',
    goods_name VARCHAR(200) DEFAULT '',
    goods_image VARCHAR(500) DEFAULT '',
    sku_id BIGINT DEFAULT 0,
    sku_code VARCHAR(100) DEFAULT '',
    sku_name VARCHAR(100) DEFAULT '',
    quantity INT DEFAULT 0,
    return_quantity INT DEFAULT 0,
    refund_amount DECIMAL(18,2) DEFAULT 0,
    status INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_eas_order ON erp_after_sale(order_id);
CREATE INDEX IF NOT EXISTS idx_easi_after ON erp_after_sale_item(after_sale_id);