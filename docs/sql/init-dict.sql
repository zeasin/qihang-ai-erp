-- ====== 启航AI ERP 字典表初始化脚本 (PostgreSQL) ======

DROP TABLE IF EXISTS sys_dict_data;
DROP TABLE IF EXISTS sys_dict_type;

-- 字典类型表
CREATE TABLE sys_dict_type (
    dict_id BIGSERIAL PRIMARY KEY,
    dict_name VARCHAR(100) DEFAULT '',
    dict_type VARCHAR(100) DEFAULT '',
    status CHAR(1) DEFAULT '0',
    create_by VARCHAR(64) DEFAULT '',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remark VARCHAR(500)
);
CREATE UNIQUE INDEX idx_dict_type ON sys_dict_type(dict_type);

-- 字典数据表
CREATE TABLE sys_dict_data (
    dict_code BIGSERIAL PRIMARY KEY,
    dict_sort INT DEFAULT 0,
    dict_label VARCHAR(100) DEFAULT '',
    dict_value VARCHAR(100) DEFAULT '',
    dict_type VARCHAR(100) DEFAULT '',
    css_class VARCHAR(100),
    list_class VARCHAR(100),
    is_default CHAR(1) DEFAULT 'N',
    status CHAR(1) DEFAULT '0',
    create_by VARCHAR(64) DEFAULT '',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remark VARCHAR(500)
);
CREATE INDEX idx_dict_data_type ON sys_dict_data(dict_type);

-- ====== 初始字典类型 ======
INSERT INTO sys_dict_type (dict_id, dict_name, dict_type, status, remark) VALUES
(1, '用户性别', 'sys_user_sex', '0', '用户性别列表'),
(2, '菜单状态', 'sys_show_hide', '0', '菜单状态列表'),
(3, '系统开关', 'sys_normal_disable', '0', '系统开关列表'),
(4, '系统是否', 'sys_yes_no', '0', '系统是否列表'),
(5, '商品单位', 'goodsUnit', '0', '商品单位'),
(6, '售后问题分类', 'refund_reason_type', '0', '售后问题类型');

-- ====== 初始字典数据 ======
INSERT INTO sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, remark) VALUES
(1, 1, '男', '0', 'sys_user_sex', 'primary', 'Y', '0', '性别男'),
(2, 2, '女', '1', 'sys_user_sex', '', 'N', '0', '性别女'),
(3, 3, '未知', '2', 'sys_user_sex', '', 'N', '0', '性别未知'),
(4, 1, '显示', '0', 'sys_show_hide', 'primary', 'Y', '0', '显示菜单'),
(5, 2, '隐藏', '1', 'sys_show_hide', 'danger', 'N', '0', '隐藏菜单'),
(6, 1, '正常', '0', 'sys_normal_disable', 'primary', 'Y', '0', '正常状态'),
(7, 2, '停用', '1', 'sys_normal_disable', 'danger', 'N', '0', '停用状态'),
(8, 1, '是', 'Y', 'sys_yes_no', 'primary', 'Y', '0', '系统默认是'),
(9, 2, '否', 'N', 'sys_yes_no', 'danger', 'N', '0', '系统默认否'),
(10, 0, '件', '件', 'goodsUnit', 'default', 'N', '0', '商品单位-件'),
(11, 0, '箱', '箱', 'goodsUnit', 'default', 'N', '0', '商品单位-箱'),
(12, 0, '7天无理由退货退款', 'wuliyou', 'refund_reason_type', 'default', 'N', '0', ''),
(13, 0, '商品质量有问题', 'zhiliangwenti', 'refund_reason_type', 'default', 'N', '0', ''),
(14, 0, '不想要了', 'buyaole', 'refund_reason_type', 'default', 'N', '0', '');

-- 重置序列
SELECT setval('sys_dict_type_dict_id_seq', (SELECT MAX(dict_id) FROM sys_dict_type));
SELECT setval('sys_dict_data_dict_code_seq', (SELECT MAX(dict_code) FROM sys_dict_data));
