-- ====================================================================
-- 启航AI ERP 商品库存、出入库相关表 (PostgreSQL)
-- ====================================================================

-- ====================================================================
-- SKU总库存表（ERP级别总量）
-- ====================================================================
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

-- ====================================================================
-- SKU仓库库存明细
-- ====================================================================
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

-- ====================================================================
-- 入库单
-- ====================================================================
DROP TABLE IF EXISTS erp_stock_in;
CREATE TABLE erp_stock_in (
    id BIGSERIAL PRIMARY KEY,
    in_num VARCHAR(55) DEFAULT '' COMMENT '入库单号',
    type INT NOT NULL DEFAULT 1 COMMENT '入库类型1采购入库2退货入库3盘点入库4其他入库',
    source_no VARCHAR(55) DEFAULT '' COMMENT '来源单据号',
    source_id BIGINT DEFAULT 0 COMMENT '来源单据Id',
    goods_unit INT NOT NULL DEFAULT 0 COMMENT '商品数',
    spec_unit INT NOT NULL DEFAULT 0 COMMENT '商品规格数',
    spec_unit_total INT NOT NULL DEFAULT 0 COMMENT '总件数',
    in_total INT DEFAULT 0 COMMENT '已入库数量',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    status INT NOT NULL DEFAULT 0 COMMENT '状态：0待入库1部分入库2全部入库',
    in_time TIMESTAMP COMMENT '入库时间',
    complete_time TIMESTAMP COMMENT '完成入库时间',
    operator_id BIGINT DEFAULT 0 COMMENT '入库操作人userid',
    operator_name VARCHAR(50) DEFAULT '' COMMENT '入库操作人',
    create_by VARCHAR(50) DEFAULT '' COMMENT '创建人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(50) DEFAULT '' COMMENT '更新人',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    warehouse_id BIGINT NOT NULL DEFAULT 0 COMMENT '仓库id',
    warehouse_name VARCHAR(50) DEFAULT '' COMMENT '仓库名',
    warehouse_type VARCHAR(20) DEFAULT '' COMMENT '仓库类型'
);

-- ====================================================================
-- 入库单明细
-- ====================================================================
DROP TABLE IF EXISTS erp_stock_in_item;
CREATE TABLE erp_stock_in_item (
    id BIGSERIAL PRIMARY KEY,
    stock_in_id BIGINT NOT NULL DEFAULT 0 COMMENT '入库单id',
    stock_in_type INT DEFAULT 0 COMMENT '来源类型（1采购订单2退货订单）',
    source_no VARCHAR(55) DEFAULT '' COMMENT '来源单号',
    source_id BIGINT DEFAULT 0 COMMENT '来源单id',
    source_item_id BIGINT DEFAULT 0 COMMENT '来源单itemId',
    goods_id BIGINT NOT NULL DEFAULT 0 COMMENT '商品id',
    goods_num VARCHAR(80) DEFAULT '' COMMENT '商品编码',
    goods_name VARCHAR(200) DEFAULT '' COMMENT '商品名称',
    goods_image VARCHAR(500) DEFAULT '' COMMENT '商品图片',
    sku_id BIGINT NOT NULL DEFAULT 0 COMMENT '商品规格id',
    sku_code VARCHAR(100) DEFAULT '' COMMENT '商品规格编码',
    sku_name VARCHAR(100) DEFAULT '' COMMENT '规格名称',
    quantity INT NOT NULL DEFAULT 0 COMMENT '原始数量',
    in_quantity INT NOT NULL DEFAULT 0 COMMENT '入库数量',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    status INT DEFAULT 0 COMMENT '状态：0待入库1部分入库2已入库',
    create_by VARCHAR(50) DEFAULT '' COMMENT '创建人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(50) DEFAULT '' COMMENT '更新人',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    warehouse_id BIGINT DEFAULT 0 COMMENT '仓库id',
    position_id BIGINT DEFAULT 0 COMMENT '仓位id'
);

-- ====================================================================
-- 出库单
-- ====================================================================
DROP TABLE IF EXISTS erp_stock_out;
CREATE TABLE erp_stock_out (
    id BIGSERIAL PRIMARY KEY,
    out_num VARCHAR(55) NOT NULL DEFAULT '' COMMENT '出库单号',
    source_num VARCHAR(50) DEFAULT '' COMMENT '来源单据号',
    source_id BIGINT DEFAULT 0 COMMENT '来源单据Id',
    type INT NOT NULL DEFAULT 1 COMMENT '出库类型1订单发货出库2采购退货出库3盘点出库4报损出库',
    shop_id BIGINT NOT NULL DEFAULT 0 COMMENT '店铺id',
    goods_unit INT NOT NULL DEFAULT 0 COMMENT '商品数',
    spec_unit INT NOT NULL DEFAULT 0 COMMENT '商品规格数',
    spec_unit_total INT NOT NULL DEFAULT 0 COMMENT '总件数',
    out_total INT DEFAULT 0 COMMENT '已出库数量',
    remark VARCHAR(500) DEFAULT '' COMMENT '备注',
    status INT NOT NULL DEFAULT 0 COMMENT '状态：0待出库1部分出库2全部出库',
    out_time TIMESTAMP COMMENT '出库时间',
    complete_time TIMESTAMP COMMENT '完成出库时间',
    operator_id BIGINT DEFAULT 0 COMMENT '出库操作人userid',
    operator_name VARCHAR(50) DEFAULT '' COMMENT '出库操作人',
    create_by VARCHAR(50) DEFAULT '' COMMENT '创建人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(50) DEFAULT '' COMMENT '更新人',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    warehouse_id BIGINT NOT NULL DEFAULT 0 COMMENT '仓库id',
    warehouse_name VARCHAR(50) DEFAULT '' COMMENT '仓库名',
    merchant_id BIGINT NOT NULL DEFAULT 0 COMMENT '商户ID'
);

-- ====================================================================
-- 出库单明细
-- ====================================================================
DROP TABLE IF EXISTS erp_stock_out_item;
CREATE TABLE erp_stock_out_item (
    id BIGSERIAL PRIMARY KEY,
    type INT NOT NULL DEFAULT 1 COMMENT '出库类型1订单拣货出库2采购退货出库3盘点出库4报损出库',
    entry_id BIGINT NOT NULL DEFAULT 0 COMMENT '出库单id（外键）',
    source_order_id BIGINT DEFAULT 0 COMMENT '来源订单id',
    source_order_item_id BIGINT DEFAULT 0 COMMENT '来源订单itemId',
    source_order_num VARCHAR(50) DEFAULT '' COMMENT '来源订单号',
    original_quantity BIGINT NOT NULL DEFAULT 0 COMMENT '总数量',
    out_quantity BIGINT NOT NULL DEFAULT 0 COMMENT '已出库数量',
    complete_time TIMESTAMP COMMENT '完成出库时间',
    status INT NOT NULL DEFAULT 0 COMMENT '状态：0待出库1部分出库2全部出库',
    warehouse_id BIGINT NOT NULL DEFAULT 0 COMMENT '仓库id',
    goods_id BIGINT NOT NULL DEFAULT 0 COMMENT '商品id',
    goods_num VARCHAR(80) DEFAULT '' COMMENT '商品编码',
    goods_name VARCHAR(200) DEFAULT '' COMMENT '商品名称',
    goods_image VARCHAR(500) DEFAULT '' COMMENT '商品图片',
    sku_id BIGINT NOT NULL DEFAULT 0 COMMENT '商品规格id',
    sku_code VARCHAR(100) DEFAULT '' COMMENT '商品规格编码',
    sku_name VARCHAR(100) DEFAULT '' COMMENT '规格名称',
    create_by VARCHAR(50) DEFAULT '' COMMENT '创建人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(50) DEFAULT '' COMMENT '更新人',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    shop_id BIGINT NOT NULL DEFAULT 0 COMMENT '店铺id',
    merchant_id BIGINT NOT NULL DEFAULT 0 COMMENT '商户ID'
);