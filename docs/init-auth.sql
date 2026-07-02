-- ====== 启航AI ERP 权限表初始化脚本 (PostgreSQL) ======

DROP TABLE IF EXISTS sys_role_menu;
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS sys_menu;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_user;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    user_id BIGSERIAL PRIMARY KEY,
    dept_id BIGINT,
    merchant_id BIGINT DEFAULT 0,
    user_name VARCHAR(30) NOT NULL,
    nick_name VARCHAR(30) NOT NULL,
    user_type VARCHAR(2) NOT NULL DEFAULT '00',
    email VARCHAR(50) DEFAULT '',
    phonenumber VARCHAR(11) DEFAULT '',
    sex CHAR(1) DEFAULT '0',
    avatar VARCHAR(100) DEFAULT '',
    password VARCHAR(100) DEFAULT '',
    status CHAR(1) DEFAULT '0',
    del_flag CHAR(1) DEFAULT '0',
    login_ip VARCHAR(128) DEFAULT '',
    login_date TIMESTAMP,
    create_by VARCHAR(64) DEFAULT '',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remark VARCHAR(500)
);

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    role_id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(30) NOT NULL,
    role_key VARCHAR(100) NOT NULL,
    status CHAR(1) DEFAULT '0',
    del_flag CHAR(1) DEFAULT '0',
    create_by VARCHAR(64) DEFAULT '',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT '',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remark VARCHAR(500)
);

-- 用户角色关联
CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

-- 菜单权限表
CREATE TABLE IF NOT EXISTS sys_menu (
    menu_id BIGSERIAL PRIMARY KEY,
    menu_name VARCHAR(50) NOT NULL,
    parent_id BIGINT DEFAULT 0,
    order_num INT DEFAULT 0,
    path VARCHAR(200) DEFAULT '',
    component VARCHAR(255) DEFAULT '',
    perms VARCHAR(100) DEFAULT '',
    icon VARCHAR(100) DEFAULT '',
    menu_type CHAR(1) DEFAULT 'M',
    visible CHAR(1) DEFAULT '0',
    status CHAR(1) DEFAULT '0',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remark VARCHAR(500)
);

-- 角色菜单关联
CREATE TABLE IF NOT EXISTS sys_role_menu (
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, menu_id)
);

-- ====== 初始数据 ======
-- 密码均为 BCrypt 加密，明文: admin123

INSERT INTO sys_role (role_id, role_name, role_key, status) VALUES
(1, '管理员', 'admin', '0'),
(2, '订单处理员', 'order', '0'),
(3, '拣货员', 'picker', '0'),
(4, '打包员', 'packer', '0'),
(5, '发货员', 'shipper', '0'),
(6, '收货员', 'receiver', '0'),
(7, '质检员', 'qc', '0'),
(8, '盘点员', 'counter', '0');

-- 手动填入正确的 BCrypt 哈希
-- 请用你本地生成的 hash 替换下面 XXXXXX 部分
INSERT INTO sys_user (user_id, user_name, nick_name, password, user_type, status) VALUES
(1, 'admin', '管理员', '$2a$10$DOFAuSSb.7VxP.awJS0yz.MJkoJZvGB3OYp4wsOEkK28A7lJcgT7C', '00', '0'),
(2, 'order', '张丽', '$2a$10$keyNHTqnnXR5Nz7S/e5ZeewoE9WuOlB8NjU3TT23UhDAk2GgMlIA.', '00', '0'),
(3, 'picker', '李强', '$2a$10$VdEwLVYEi7d8ybJBYpLP2ueD0nXtmMF0y4qLF9B4OLzMU6xsdWKN6', '10', '0'),
(4, 'packer', '王芳', '$2a$10$p6QbYzFly09U2NT9Oy4C9ubfTNqPl.Sny7PZeoPcvTpygnpDzDcru', '10', '0'),
(5, 'shipper', '赵刚', '$2a$10$6AejDidHrOaaXJ9pCEwNsuG226D7bE8srONslKJNnrY0/kNf.nK7q', '10', '0'),
(6, 'receiver', '陈丽', '$2a$10$56yn7i9nsG9.aQ2pMznyV.brF3DiZkODUAbRdqAXef6Wyt6LJnpm6', '10', '0'),
(7, 'qc', '刘质检', '$2a$10$hwI.5TOXbXszOkEVPnb1YOOUHFg93k3gZGOfj5ho4Z6OJ2XcL56DW', '10', '0'),
(8, 'counter', '周盘点', '$2a$10$bQRp0nY.ygwvEykuvyODs.5e8a21Rr6TsWyriDesOpNL17ckSztpS', '10', '0');

INSERT INTO sys_user_role (user_id, role_id) VALUES
(1,1), (2,2), (3,3), (4,4), (5,5), (6,6), (7,7), (8,8);

-- ====== 菜单/权限数据 ======
-- menu_type: M=目录 C=菜单 F=按钮
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, perms, icon, menu_type) VALUES
(1,  '智能看板',     0, 1,  '/dashboard',        'Dashboard',        'dashboard:view',         '📊', 'C'),
(2,  '订单工作台',   0, 2,  '/workspace/order',   'workspace/Order',  'workspace:order:view',   '📋', 'C'),
(3,    '审核订单',   2, 1,  null, null,            'workspace:order:audit',  null, 'F'),
(4,    '打印面单',   2, 2,  null, null,            'workspace:order:print',  null, 'F'),
(5,    '推送仓库',   2, 3,  null, null,            'workspace:order:push',   null, 'F'),
(6,  '拣货工作台',   0, 3,  '/workspace/picking', 'workspace/Picking','workspace:picking:view', '🔍', 'C'),
(7,  '打包工作台',   0, 4,  '/workspace/packing', 'workspace/Packing','workspace:packing:view', '📦', 'C'),
(8,  '发货工作台',   0, 5,  '/workspace/shipping','workspace/Shipping','workspace:shipping:view','🚚', 'C'),
(9,  '收货工作台',   0, 6,  '/workspace/receiving','workspace/Receiving','workspace:receiving:view','📥', 'C'),
(10, '质检工作台',   0, 7,  '/workspace/qc',      'workspace/QC',     'workspace:qc:view',      '🔬', 'C'),
(11, '盘点工作台',   0, 8,  '/workspace/counting','workspace/Counting','workspace:counting:view','📋', 'C'),
(12, 'AI对话',      0, 9,  '/chat',              'Chat',             'chat:view',              '🤖', 'C'),
(13, '系统管理',     0, 99, null,                 null,               'system:manage',          '⚙️', 'M'),
(14,   '角色管理',   13, 1,  '/system/roles',     'system/Roles',     'system:role:manage',     null, 'C'),
(15,   '用户管理',   13, 2,  '/system/users',     'system/Users',     'system:user:manage',     null, 'C'),
(16,   '字典管理',   13, 3,  '/system/dicts',     'system/Dicts',     'system:dict:manage',     null, 'C');

-- 超级管理员分配所有菜单
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE menu_id NOT IN (SELECT menu_id FROM sys_role_menu WHERE role_id = 1);

-- 订单处理员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(2,1), (2,2), (2,3), (2,4), (2,5), (2,12);

-- 拣货员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(3,1), (3,6), (3,12);

-- 打包员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(4,1), (4,7), (4,12);

-- 发货员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(5,1), (5,8), (5,12);

-- 收货员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(6,1), (6,9), (6,12);

-- 质检员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(7,1), (7,10), (7,12);

-- 盘点员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(8,1), (8,11), (8,12);

