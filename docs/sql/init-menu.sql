-- ====== 启航AI ERP 菜单/权限初始化脚本 (PostgreSQL) ======
-- 独立于 init-auth.sql，可单独维护菜单结构
-- 执行顺序：先 init-auth.sql（用户/角色），再 init-menu.sql（菜单/权限）

DROP TABLE IF EXISTS sys_role_menu;
DROP TABLE IF EXISTS sys_menu;

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

-- ====== 菜单数据 ======
-- menu_type: M=目录 C=菜单 F=按钮

TRUNCATE TABLE sys_role_menu;
TRUNCATE TABLE sys_menu;

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, perms, icon, menu_type) VALUES
-- 一级菜单（工作台/功能）
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

-- 二级菜单：系统管理 (menu_id=13, M=目录)
(13, '系统管理',     0, 99, null,                 null,               'system:manage',          '⚙️', 'M'),
(14,   '角色管理',   13, 1,  '/system/roles',     'system/Roles',     'system:role:manage',     null, 'C'),
(15,   '用户管理',   13, 2,  '/system/users',     'system/Users',     'system:user:manage',     null, 'C'),
(16,   '字典管理',   13, 3,  '/system/dicts',     'system/Dicts',     'system:dict:manage',     null, 'C'),
(21,   '电商平台设置', 13, 4,  '/system/platforms', 'system/Platforms','system:platform:manage',  null, 'C'),
(22,   '系统参数',    13, 5,  '/system/configs',   'system/Configs',   'system:config:manage',   null, 'C'),
(23,   '定时任务',    13, 6,  '/system/tasks',     'system/Tasks',     'system:task:manage',     null, 'C'),
(24,   '接口授权',    13, 7,  '/system/openAuth',  'system/OpenAuth',  'system:openAuth:manage', null, 'C'),

-- 二级菜单：渠道管理 (menu_id=17, M=目录)
(17, '渠道管理',     0, 3,  null,                  null,               'channel:manage',          '📡', 'M'),
(18,   '渠道设置',    17, 1,  '/channel/platforms',  'channel/Platforms','channel:platform:view',    null, 'C'),
(19,   '店铺管理',    17, 2,  '/channel/shops',      'channel/Shops',    'channel:shop:view',       null, 'C'),
(20,   '商户管理',    17, 3,  '/channel/merchants',  'channel/Merchants','channel:merchant:view',   null, 'C');

-- ====== 角色-菜单分配 ======

-- 超级管理员：分配所有菜单
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu;

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

SELECT setval('sys_menu_menu_id_seq', (SELECT MAX(menu_id) FROM sys_menu));
