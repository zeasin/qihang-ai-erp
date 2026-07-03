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