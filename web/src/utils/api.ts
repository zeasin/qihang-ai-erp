export const api = {
  prefix: '/sys-api',

  // 认证
  login: '/sys-api/login',
  getInfo: '/sys-api/getInfo',
  logout: '/sys-api/logout',

  // 系统管理
  menuTree: '/sys-api/system/menu/tree',
  menuSave: '/sys-api/system/menu/save',
  menuDelete: (id: number) => `/sys-api/system/menu/${id}`,
  roleList: '/sys-api/system/role/list',
  roleSave: '/sys-api/system/role/save',
  roleDelete: (id: number) => `/sys-api/system/role/${id}`,
  roleMenus: (id: number) => `/sys-api/system/role/${id}/menus`,
  roleUpdateMenus: (id: number) => `/sys-api/system/role/${id}/menus`,
  userList: '/sys-api/system/user/list',
  userRoles: (id: number) => `/sys-api/system/user/${id}/roles`,

  // 电商平台
  platformList: '/sys-api/system/platform/list',
  platformSave: '/sys-api/system/platform/save',
  platformDelete: (id: number) => `/sys-api/system/platform/${id}`,

  // 系统参数
  configList: '/sys-api/system/config/list',
  configSave: '/sys-api/system/config/save',
  configDelete: (id: number) => `/sys-api/system/config/${id}`,

  // 定时任务
  taskList: '/sys-api/system/task/list',
  taskGet: (id: number) => `/sys-api/system/task/${id}`,
  taskUpdate: '/sys-api/system/task',

  // 接口授权
  openAuthList: '/sys-api/system/openAuth/list',
  openAuthSave: '/sys-api/system/openAuth/save',
  openAuthDelete: (id: number) => `/sys-api/system/openAuth/${id}`,

  // 字典管理
  dictTypeList: '/sys-api/system/dict/type/list',
  dictTypeSave: '/sys-api/system/dict/type/save',
  dictTypeDelete: (id: number) => `/sys-api/system/dict/type/${id}`,
  dictDataList: '/sys-api/system/dict/data/list',
  dictDataSave: '/sys-api/system/dict/data/save',
  dictDataDelete: (id: number) => `/sys-api/system/dict/data/${id}`,

  // 渠道管理
  shopList: '/sys-api/channel/shop/list',
  shopSave: '/sys-api/channel/shop/save',
  shopDelete: (id: number) => `/sys-api/channel/shop/${id}`,
  merchantList: '/sys-api/channel/merchant/list',
  merchantSave: '/sys-api/channel/merchant/save',
  merchantDelete: (id: number) => `/sys-api/channel/merchant/${id}`,

  // 基础数据
  categoryList: '/sys-api/basic/category/list',
  categorySave: '/sys-api/basic/category/save',
  categoryDelete: (id: number) => `/sys-api/basic/category/${id}`,
  brandList: '/sys-api/basic/brand/list',
  brandSave: '/sys-api/basic/brand/save',
  brandDelete: (id: number) => `/sys-api/basic/brand/${id}`,
  categoryAttrList: '/sys-api/basic/category/attribute/list',
  categoryAttrSave: '/sys-api/basic/category/attribute/save',
  categoryAttrDelete: (id: number) => `/sys-api/basic/category/attribute/${id}`,
  categoryAttrValueList: '/sys-api/basic/category/attribute/value/list',
  categoryAttrValueSave: '/sys-api/basic/category/attribute/value/save',
  categoryAttrValueDelete: (id: number) => `/sys-api/basic/category/attribute/value/${id}`,
  supplierList: '/sys-api/basic/supplier/list',
  supplierSave: '/sys-api/basic/supplier/save',
  supplierDelete: (id: number) => `/sys-api/basic/supplier/${id}`,
  carrierList: '/sys-api/basic/carrier/list',
  carrierSave: '/sys-api/basic/carrier/save',
  carrierDelete: (id: number) => `/sys-api/basic/carrier/${id}`,
  warehouseList: '/sys-api/basic/warehouse/list',
  warehouseSave: '/sys-api/basic/warehouse/save',
  warehouseDelete: (id: number) => `/sys-api/basic/warehouse/${id}`,
  regionList: '/sys-api/basic/region/list',
  regionSave: '/sys-api/basic/region/save',
  regionDelete: (id: number) => `/sys-api/basic/region/${id}`,
  logisticsCompanyList: '/sys-api/basic/logistics-company/list',
  logisticsCompanySave: '/sys-api/basic/logistics-company/save',
  logisticsCompanyDelete: (id: number) => `/sys-api/basic/logistics-company/${id}`,

  // 商品库管理
  goodsList: '/erp-api/goods/list',
  goodsGet: (id: number) => `/erp-api/goods/${id}`,
  goodsSave: '/erp-api/goods/save',
  goodsAdd: '/erp-api/goods/add',
  goodsDelete: (id: number) => `/erp-api/goods/${id}`,

  // 订单管理
  orderList: '/erp-api/order/list',
  orderGet: (id: number) => `/erp-api/order/${id}`,
  orderSave: '/erp-api/order/save',
  orderDelete: (id: number) => `/erp-api/order/${id}`,
}
