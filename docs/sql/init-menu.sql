-- ====== 启航AI ERP 菜单/权限初始化脚本 (PostgreSQL) ======
-- 独立于 init-auth.sql，可单独维护菜单结构
-- 执行顺序：先 init-auth.sql（用户/角色），再 init-menu.sql（菜单/权限）
--
-- menu_type: M=目录 C=菜单 F=按钮
-- =============================================================

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

-- ====================================================================
-- 菜单数据
-- ====================================================================

TRUNCATE TABLE sys_role_menu;
TRUNCATE TABLE sys_menu;

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, perms, icon, menu_type) VALUES

-- ====================================================================
-- 一级：工作台/功能 (parent_id=0, 平铺直达页面)
-- ====================================================================
(1,  '智能看板',       0, 1,  '/dashboard',          'Dashboard',           'dashboard:view',         '📊', 'C'),
(2,  '订单工作台',     0, 2,  '/workspace/order',    'workspace/OrderWorkspace',     'workspace:order:view',   '📋', 'C'),
(3,    '审核订单',     2, 1,  null,                  null,                  'workspace:order:audit',  '✅', 'F'),
(4,    '打印面单',     2, 2,  null,                  null,                  'workspace:order:print',  '🖨️', 'F'),
(5,    '推送仓库',     2, 3,  null,                  null,                  'workspace:order:push',   '📤', 'F'),
(6,  '拣货工作台',     0, 3,  '/workspace/picking',  'workspace/PickingWorkspace',   'workspace:picking:view', '🔍', 'C'),
(7,  '打包工作台',     0, 4,  '/workspace/packing',  'workspace/PackingWorkspace',   'workspace:packing:view', '📦', 'C'),
(8,  '发货工作台',     0, 5,  '/workspace/shipping', 'workspace/ShippingWorkspace',  'workspace:shipping:view','🚚', 'C'),
(9,  '收货工作台',     0, 6,  '/workspace/receiving','workspace/ReceivingWorkspace', 'workspace:receiving:view','📥', 'C'),
(10, '质检工作台',     0, 7,  '/workspace/qc',       'workspace/QCWorkspace',        'workspace:qc:view',      '🔬', 'C'),
(11, '盘点工作台',     0, 8,  '/workspace/counting', 'workspace/CountingWorkspace',  'workspace:counting:view','📋', 'C'),
(12, 'AI对话',        0, 9,  '/chat',               'Chat',                'chat:view',              '🤖', 'C'),
-- ====================================================================
-- 一级：系统管理 (menu_id=13, M=目录)
-- 仅保留运维管理类功能
-- ====================================================================
(13, '系统管理',       0, 99, '/system',            'system/SystemLayout', 'system:manage',           '⚙️', 'M'),
(14,   '角色管理',     13, 1,  '/system/roles',     'system/Roles',        'system:role:manage',      '👥', 'C'),
(15,   '用户管理',     13, 2,  '/system/users',     'system/Users',        'system:user:manage',      '👤', 'C'),

(21,   '电商平台设置',  26, 5,  '/basic/platforms',  'basic/Platforms',     'system:platform:manage',  '🛒', 'C'),
(22,   '系统参数',     13, 5,  '/system/configs',   'system/Configs',      'system:config:manage',    '🔧', 'C'),
(23,   '定时任务',     13, 6,  '/system/tasks',     'system/Tasks',        'system:task:manage',      '⏰', 'C'),
(24,   '接口授权',     13, 7,  '/system/openAuth',  'system/OpenAuth',     'system:openAuth:manage',  '🔑', 'C'),
-- ====================================================================
-- ====================================================================
(19,   '店铺管理',     26, 6,  '/basic/shops',       'basic/Shops',         'channel:shop:view',       '🏪', 'C'),
(20,   '商户管理',     26, 7,  '/basic/merchants',   'basic/Merchants',     'channel:merchant:view',   '🏢', 'C'),

-- ====================================================================
-- 一级：基础数据 (menu_id=26, M=目录)
-- 收录各业务模块的公共基础/主数据，方便统一维护
-- ====================================================================
(26, '基础数据',       0, 50, '/basic',             'basic/BasicLayout',   'basic:data:manage',       '🗂️', 'M'),

-- 商品主数据
(100, '商品分类管理',  26, 1,  '/basic/category',    'basic/Category',      'basic:category:manage',   '🏷️', 'C'),
(101, '商品品牌管理',  26, 2,  '/basic/brand',       'basic/Brand',         'basic:brand:manage',      '✨', 'C'),

-- 供应商主数据
(104, '供应商档案',    26, 10, '/basic/supplier',    'basic/Supplier',      'basic:supplier:manage',   '🤝', 'C'),
(105, '采购承运商',    26, 11, '/basic/carrier',     'basic/Carrier',       'basic:carrier:manage',    '🚛', 'C'),

-- 仓库主数据
(106, '仓库管理',      26, 20, '/basic/warehouse',   'basic/Warehouse',     'basic:warehouse:manage',  '🏬', 'C'),

-- 系统基础数据
(111, '国家地区设置',  26, 40, '/basic/region',      'basic/Region',        'basic:region:manage',     '🌍', 'C'),
(112, '快递公司库',    26, 41, '/basic/logistics-company','basic/LogisticsCompany','basic:logisticsCompany:manage', '📮', 'C'),
(114, '字典管理',      26, 50, '/basic/dict',        'basic/Dict',          'basic:dict:manage',       '📖', 'C'),

(116,   '菜单管理',    13, 3,  '/system/menus',     'system/Menus',        'system:menu:manage',      '📋', 'C'),

-- ====================================================================
-- 一级：商品管理 (menu_id=30, M=目录)
-- ====================================================================
(30, '商品管理',       0, 30, '/goods',            'goods/GoodsLayout',   'goods:manage',            '📦', 'M'),
(31,   '商品列表',    30, 10, '/goods/list',      'goods/GoodsList',     'goods:goods:list',        '📋', 'C'),
-- 隐藏页面
(32,   '新增商品页面', 30, 11, '/goods/create',    'goods/GoodsCreate',   null,                    '➕', 'C'),
-- 按钮权限
(34,   '新增商品',    31, 1,  null,                null,                  'goods:goods:add',         '➕', 'F'),
(35,   '编辑商品',    31, 2,  null,                null,                  'goods:goods:edit',        '✏️', 'F'),
(36,   '删除商品',    31, 3,  null,                null,                  'goods:goods:remove',      '🗑️', 'F'),
-- ====================================================================
-- 一级：订单管理 (menu_id=40, M=目录)
-- ====================================================================
(40, '订单管理',       0, 40, '/order',            'order/OrderLayout',   'order:manage',            '📋', 'M'),
(41,   '订单列表',    40, 10, '/order/list',       'order/OrderList',     'order:order:list',        '📋', 'C'),
-- 隐藏页面
(42,   '新增订单页面', 40, 11, '/order/create',    'order/OrderCreate',   null,                    '➕', 'C'),
-- 按钮权限
(44,   '新增订单',    41, 1,  null,                null,                  'order:order:add',         '➕', 'F'),
(45,   '编辑订单',    41, 2,  null,                null,                  'order:order:edit',        '✏️', 'F'),
(46,   '删除订单',    41, 3,  null,                  null,                       'order:order:remove',      '🗑️', 'F'),
-- 售后菜单（挂在订单管理下）
(47,   '售后单列表',  40, 20, '/order/after-sale',   'order/AfterSaleList',  'after:sale:list',          '🔄', 'C'),
-- 按钮权限
(48,   '审核',        47, 1,  null,                  null,                       'after:sale:audit',         '✅', 'F'),
(49,   '退款',        47, 2,  null,                  null,                       'after:sale:refund',        '💰', 'F'),
(55,   '退货入库',    47, 3,  null,                  null,                       'after:sale:return',        '📥', 'F'),
-- ====================================================================
-- 一级：发货管理 (menu_id=50, M=目录) 供发货/仓储人员使用
-- ====================================================================
(50, '发货管理',       0, 45, '/ship',              'delivery/DeliveryLayout',   'delivery:manage',          '🚚', 'M'),
(51,   '待发货订单',  50, 10, '/ship/pending',     'delivery/PendingOrderList', 'delivery:pending:list',    '📋', 'C'),
(52,   '出库单列表',  50, 20, '/ship/outbound',    'delivery/StockOutList',     'delivery:out:list',        '📤', 'C'),
(53,   '入库单列表',  50, 30, '/ship/inbound',     'delivery/StockInList',      'delivery:in:list',         '📥', 'C'),
-- ====================================================================
-- 一级：采购管理 (menu_id=60, M=目录)
-- ====================================================================
(60, '采购管理',       0, 35, '/purchase',         'purchase/PurchaseLayout',   'purchase:manage',          '📦', 'M'),
(61,   '采购订单',    60, 10, '/purchase/list',    'purchase/PurchaseList',     'purchase:order:list',      '📋', 'C'),
(62,   '新增采购订单', 60, 11, '/purchase/create', 'purchase/PurchaseCreate',   null,                       '➕', 'C'),
(63,   '采购入库',    60, 20, '/purchase/stock-in', 'purchase/StockInList',     'purchase:stockin:list',    '📥', 'C'),
-- 按钮权限
(64,   '新增采购',    61, 1,  null,                 null,                       'purchase:order:add',       '➕', 'F'),
(65,   '编辑采购',    61, 2,  null,                 null,                       'purchase:order:edit',     '✏️', 'F'),
(66,   '删除采购',    61, 3,  null,                 null,                       'purchase:order:delete',   '🗑️', 'F'),
(67,   '采购入库',    61, 4,  null,                 null,                       'purchase:order:stockin',  '📥', 'F');

-- 隐藏页面设为不可见
UPDATE sys_menu SET visible = '1' WHERE menu_id IN (32, 42, 62);

-- ====================================================================
-- 角色-菜单分配
-- ====================================================================

-- 超级管理员：分配所有菜单
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu;

-- 订单处理员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(2,1), (2,2), (2,3), (2,4), (2,5), (2,12), (2,40), (2,41), (2,42), (2,44), (2,45), (2,46), (2,47), (2,48), (2,49), (2,55);

-- 拣货员

-- 拣货员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(3,1), (3,6), (3,12);

-- 打包员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(4,1), (4,7), (4,12);

-- 发货员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(5,1), (5,8), (5,12), (5,50), (5,51), (5,52), (5,53);

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
