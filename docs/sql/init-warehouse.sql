-- ====== 仓库基础数据表 (PostgreSQL) ======

-- ====================================================================
-- 仓库管理
-- ====================================================================
DROP TABLE IF EXISTS erp_warehouse;
CREATE TABLE erp_warehouse (
    id BIGSERIAL PRIMARY KEY,
    warehouse_no VARCHAR(50),
    warehouse_name VARCHAR(100) NOT NULL,
    type INT DEFAULT 1,
    status VARCHAR(10) DEFAULT '0',
    province VARCHAR(20),
    city VARCHAR(20),
    county VARCHAR(20),
    address VARCHAR(100),
    contacts VARCHAR(20),
    phone VARCHAR(20),
    remark VARCHAR(255),
    warehouse_type VARCHAR(10) DEFAULT 'LOCAL',
    merchant_id BIGINT DEFAULT 0,
    create_by VARCHAR(25),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(25),
    update_time TIMESTAMP
);

INSERT INTO erp_warehouse (id, warehouse_no, warehouse_name, type, status, province, city, address, contacts, phone, create_by) VALUES
(1, 'WH001', '中山古镇总仓', 1, '0', '广东省', '中山市', '古镇镇中兴大道88号', '王经理', '13700001111', 'admin'),
(2, 'WH002', '义乌分仓', 1, '0', '浙江省', '义乌市', '北苑街道仓储区5号', '陈主管', '13800002222', 'admin');

SELECT setval('erp_warehouse_id_seq', (SELECT MAX(id) FROM erp_warehouse));