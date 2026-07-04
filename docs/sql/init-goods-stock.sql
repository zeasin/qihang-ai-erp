-- ====================================================================
-- 启航AI ERP 商品库存、出入库相关表 (PostgreSQL)
-- ====================================================================

-- SKU总库存表（ERP级别总量）
DROP TABLE IF EXISTS o_goods_sku_stock;
CREATE TABLE o_goods_sku_stock (
    id BIGSERIAL PRIMARY KEY,
    sku_id BIGINT NOT NULL DEFAULT 0,
    goods_id BIGINT NOT NULL DEFAULT 0,
    sku_code VARCHAR(100) DEFAULT '',
    goods_name VARCHAR(500) DEFAULT '',
    goods_img VARCHAR(500) DEFAULT '',
    sku_name VARCHAR(200) DEFAULT '',
    quantity INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_ogss_sku ON o_goods_sku_stock(sku_id);

-- SKU仓库库存明细
DROP TABLE IF EXISTS o_goods_sku_stock_warehouse;
CREATE TABLE o_goods_sku_stock_warehouse (
    id BIGSERIAL PRIMARY KEY,
    sku_id BIGINT NOT NULL DEFAULT 0,
    goods_id BIGINT NOT NULL DEFAULT 0,
    warehouse_id BIGINT NOT NULL DEFAULT 0,
    warehouse_name VARCHAR(100) DEFAULT '',
    sku_code VARCHAR(100) DEFAULT '',
    goods_name VARCHAR(500) DEFAULT '',
    goods_img VARCHAR(500) DEFAULT '',
    sku_name VARCHAR(200) DEFAULT '',
    quantity INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_ogssw_wh_sku ON o_goods_sku_stock_warehouse(warehouse_id, sku_id);

-- 入库单
DROP TABLE IF EXISTS erp_stock_in;
CREATE TABLE erp_stock_in (
    id BIGSERIAL PRIMARY KEY,
    in_num VARCHAR(55) DEFAULT '',
    type INT NOT NULL DEFAULT 1,
    source_no VARCHAR(55) DEFAULT '',
    source_id BIGINT DEFAULT 0,
    goods_unit INT NOT NULL DEFAULT 0,
    spec_unit INT NOT NULL DEFAULT 0,
    spec_unit_total INT NOT NULL DEFAULT 0,
    in_total INT DEFAULT 0,
    remark VARCHAR(500) DEFAULT '',
    status INT NOT NULL DEFAULT 0,
    in_time TIMESTAMP,
    complete_time TIMESTAMP,
    operator_id BIGINT DEFAULT 0,
    operator_name VARCHAR(50) DEFAULT '',
    create_by VARCHAR(50) DEFAULT '',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(50) DEFAULT '',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    warehouse_id BIGINT NOT NULL DEFAULT 0,
    warehouse_name VARCHAR(50) DEFAULT '',
    warehouse_type VARCHAR(20) DEFAULT ''
);

-- 入库单明细
DROP TABLE IF EXISTS erp_stock_in_item;
CREATE TABLE erp_stock_in_item (
    id BIGSERIAL PRIMARY KEY,
    stock_in_id BIGINT NOT NULL DEFAULT 0,
    stock_in_type INT DEFAULT 0,
    source_no VARCHAR(55) DEFAULT '',
    source_id BIGINT DEFAULT 0,
    source_item_id BIGINT DEFAULT 0,
    goods_id BIGINT NOT NULL DEFAULT 0,
    goods_num VARCHAR(80) DEFAULT '',
    goods_name VARCHAR(200) DEFAULT '',
    goods_image VARCHAR(500) DEFAULT '',
    sku_id BIGINT NOT NULL DEFAULT 0,
    sku_code VARCHAR(100) DEFAULT '',
    sku_name VARCHAR(100) DEFAULT '',
    quantity INT NOT NULL DEFAULT 0,
    in_quantity INT NOT NULL DEFAULT 0,
    remark VARCHAR(500) DEFAULT '',
    status INT DEFAULT 0,
    create_by VARCHAR(50) DEFAULT '',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(50) DEFAULT '',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    warehouse_id BIGINT DEFAULT 0,
    position_id BIGINT DEFAULT 0
);

-- 出库单
DROP TABLE IF EXISTS erp_stock_out;
CREATE TABLE erp_stock_out (
    id BIGSERIAL PRIMARY KEY,
    out_num VARCHAR(55) NOT NULL DEFAULT '',
    source_num VARCHAR(50) DEFAULT '',
    source_id BIGINT DEFAULT 0,
    type INT NOT NULL DEFAULT 1,
    shop_id BIGINT NOT NULL DEFAULT 0,
    goods_unit INT NOT NULL DEFAULT 0,
    spec_unit INT NOT NULL DEFAULT 0,
    spec_unit_total INT NOT NULL DEFAULT 0,
    out_total INT DEFAULT 0,
    remark VARCHAR(500) DEFAULT '',
    status INT NOT NULL DEFAULT 0,
    out_time TIMESTAMP,
    complete_time TIMESTAMP,
    operator_id BIGINT DEFAULT 0,
    operator_name VARCHAR(50) DEFAULT '',
    create_by VARCHAR(50) DEFAULT '',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(50) DEFAULT '',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    warehouse_id BIGINT NOT NULL DEFAULT 0,
    warehouse_name VARCHAR(50) DEFAULT '',
    merchant_id BIGINT NOT NULL DEFAULT 0
);

-- 出库单明细
DROP TABLE IF EXISTS erp_stock_out_item;
CREATE TABLE erp_stock_out_item (
    id BIGSERIAL PRIMARY KEY,
    type INT NOT NULL DEFAULT 1,
    entry_id BIGINT NOT NULL DEFAULT 0,
    source_order_id BIGINT DEFAULT 0,
    source_order_item_id BIGINT DEFAULT 0,
    source_order_num VARCHAR(50) DEFAULT '',
    original_quantity BIGINT NOT NULL DEFAULT 0,
    out_quantity BIGINT NOT NULL DEFAULT 0,
    complete_time TIMESTAMP,
    status INT NOT NULL DEFAULT 0,
    warehouse_id BIGINT NOT NULL DEFAULT 0,
    goods_id BIGINT NOT NULL DEFAULT 0,
    goods_num VARCHAR(80) DEFAULT '',
    goods_name VARCHAR(200) DEFAULT '',
    goods_image VARCHAR(500) DEFAULT '',
    sku_id BIGINT NOT NULL DEFAULT 0,
    sku_code VARCHAR(100) DEFAULT '',
    sku_name VARCHAR(100) DEFAULT '',
    create_by VARCHAR(50) DEFAULT '',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(50) DEFAULT '',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    shop_id BIGINT NOT NULL DEFAULT 0,
    merchant_id BIGINT NOT NULL DEFAULT 0
);

-- 库存变动日志
DROP TABLE IF EXISTS o_goods_stock_log;
CREATE TABLE o_goods_stock_log (
    id BIGSERIAL PRIMARY KEY,
    sku_id BIGINT NOT NULL DEFAULT 0,
    goods_id BIGINT DEFAULT 0,
    sku_code VARCHAR(100) DEFAULT '',
    goods_name VARCHAR(500) DEFAULT '',
    sku_name VARCHAR(200) DEFAULT '',
    before_quantity INT DEFAULT 0,
    change_quantity INT DEFAULT 0,
    after_quantity INT DEFAULT 0,
    type INT DEFAULT 0,
    remark VARCHAR(500) DEFAULT '',
    source_no VARCHAR(55) DEFAULT '',
    warehouse_id BIGINT DEFAULT 0,
    warehouse_name VARCHAR(100) DEFAULT '',
    create_by VARCHAR(55) DEFAULT '',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);