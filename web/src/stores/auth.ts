import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import request from '../utils/request'
import { api } from '../utils/api'
import { resetDynamicRoutes } from '../router'

export interface UserInfo {
  userId: number
  userName: string
  nickName: string
  userType: string  // 00=系统用户 10=仓库人员 20=商户 30=供应商 40=店铺
  avatar: string
  phonenumber: string
  status: string
  deptId?: number
  merchantId?: number
}

export interface RoleInfo {
  roleId: number
  roleKey: string   // 如: "admin", "order", "picker", "packer", "shipper", "receiver", "qc", "counter"
  roleName: string  // 如: "管理员", "订单处理员", "拣货员", "打包员", "发货员", "收货员", "质检员", "盘点员"
}

// 角色 → 工作台路由映射

export const roleWorkspaceMap: Record<string, string> = {
  admin:    '/dashboard',
  order:    '/workspace/order',
  picker:   '/workspace/picking',
  packer:   '/workspace/packing',
  shipper:  '/workspace/shipping',
  receiver: '/workspace/receiving',
  qc:       '/workspace/qc',
  counter:  '/workspace/counting',
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const user = ref<UserInfo | null>(null)
  const roles = ref<RoleInfo[]>([])
  const permissions = ref<string[]>([])

  const isLoggedIn = computed(() => !!token.value && !!user.value)
  const userName = computed(() => user.value?.nickName || user.value?.userName || '')
  const userAvatar = computed(() => user.value?.avatar || '')

  // 获取用户默认工作台路由
  const defaultRoute = computed(() => {
    // 所有用户登录后都先进首页，首页按权限动态渲染工作台卡片
    return '/'
  })

  // 登录
  async function login(username: string, password: string, code?: string, uuid?: string) {
    const res: any = await request.post(api.login, {
      username,
      password,
      code: code || '',
      uuid: uuid || '',
    })
    const newToken = res.token
    token.value = newToken
    localStorage.setItem('token', newToken)
    // 登录后获取用户信息
    await fetchUserInfo()
    return res
  }

  // 获取用户信息
  async function fetchUserInfo() {
    const res: any = await request.get(api.getInfo)
    user.value = res.user
    roles.value = res.roles || []
    permissions.value = res.permissions || []
    return res
  }

  // 退出登录
  async function logout() {
    try {
      await request.post(api.logout)
    } catch {
      // 忽略退出异常
    }
    token.value = ''
    user.value = null
    roles.value = []
    permissions.value = []
    localStorage.removeItem('token')
    // 清除动态路由，下次登录重新加载
    resetDynamicRoutes()
  }

  // 从 token 恢复登录态（页面刷新时调用）
  async function restoreSession() {
    if (!token.value) return false
    try {
      await fetchUserInfo()
      return true
    } catch {
      token.value = ''
      localStorage.removeItem('token')
      return false
    }
  }

  return {
    token,
    user,
    roles,
    permissions,
    isLoggedIn,
    userName,
    userAvatar,
    defaultRoute,
    login,
    logout,
    fetchUserInfo,
    restoreSession,
  }
})
