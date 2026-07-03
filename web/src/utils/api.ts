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
}
