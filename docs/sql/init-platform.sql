-- ====== 渠道设置 (o_shop_platform) PostgreSQL ======

DROP TABLE IF EXISTS o_shop_platform;

CREATE TABLE o_shop_platform (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    code VARCHAR(255),
    app_key VARCHAR(255),
    app_secret VARCHAR(255),
    redirect_uri VARCHAR(255),
    server_url VARCHAR(255),
    status INT DEFAULT 0,
    sort INT DEFAULT 0
);

INSERT INTO o_shop_platform (id, name, code, app_key, app_secret, redirect_uri, server_url, status, sort) VALUES
(100, '淘宝天猫', 'TMALL', '', '', 'https://erp.qihangerp.cn/prod-api/api/oms-api/tao/oauth_callback', 'http://gw.api.taobao.com/router/rest', 0, 0),
(200, '京东POP', 'JD-POP', '', '', 'https://erp.qihangerp.cn/prod-api/api/oms-api/jd/oauth_callback', 'https://api.jd.com/routerjson', 0, 1),
(300, '拼多多', 'PDD', '', '', 'https://erp.qihangerp.cn/prod-api/api/oms-api/pdd/oauth_callback', 'https://gw-api.pinduoduo.com/api/router', 0, 2),
(400, '抖店', 'DOUDIAN', '', '', 'https://erp.qihangerp.cn/oauth/dou_callback', 'https://openapi-fxg.jinritemai.com/', 0, 3),
(500, '微信小店', 'WEISHOP', '', '', '', 'https://api.weixin.qq.com', 0, 4),
(600, '快手', 'KUAISHOU', '', '', 'https://erp.qihangerp.cn/prod-api/api/oms-api/ks/oauth_callback', 'https://developer.kuaishou.com', 0, 5),
(700, '小红书', 'XHS', '', '', 'https://erp.qihangerp.cn/prod-api/api/oms-api/xhs/oauth_callback', 'https://ark.xiaohongshu.com/ark/open_api/v3/common_controller', 0, 6),
(999, '线下门店', 'OFFLINE', '', '', '', NULL, 0, 99);

SELECT setval('o_shop_platform_id_seq', (SELECT MAX(id) FROM o_shop_platform));
