import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

// 不需要登录就能访问的白名单
const whiteList = ['/login']

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/Login.vue'),
  },
  {
    path: '/',
    name: 'home',
    component: () => import('../views/Home.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/chat',
    name: 'chat',
    component: () => import('../views/Chat.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/dashboard',
    name: 'dashboard',
    component: () => import('../views/Dashboard.vue'),
    meta: { requiresAuth: true },
  },

  // ─── 角色工作台 ───
  {
    path: '/workspace',
    name: 'workspace',
    redirect: '/workspace/order',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'order',
        name: 'workspace-order',
        component: () => import('../views/workspace/OrderWorkspace.vue'),
        meta: { roles: ['admin', 'order'] },
      },
      {
        path: 'picking',
        name: 'workspace-picking',
        component: () => import('../views/workspace/PickingWorkspace.vue'),
        meta: { roles: ['admin', 'picker'] },
      },
      {
        path: 'packing',
        name: 'workspace-packing',
        component: () => import('../views/workspace/PackingWorkspace.vue'),
        meta: { roles: ['admin', 'packer'] },
      },
      {
        path: 'shipping',
        name: 'workspace-shipping',
        component: () => import('../views/workspace/ShippingWorkspace.vue'),
        meta: { roles: ['admin', 'shipper'] },
      },
      {
        path: 'receiving',
        name: 'workspace-receiving',
        component: () => import('../views/workspace/ReceivingWorkspace.vue'),
        meta: { roles: ['admin', 'receiver'] },
      },
      {
        path: 'qc',
        name: 'workspace-qc',
        component: () => import('../views/workspace/QCWorkspace.vue'),
        meta: { roles: ['admin', 'qc'] },
      },
      {
        path: 'counting',
        name: 'workspace-counting',
        component: () => import('../views/workspace/CountingWorkspace.vue'),
        meta: { roles: ['admin', 'counter'] },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// ─── 导航守卫 ───
router.beforeEach(async (to, _from, next) => {
  const token = localStorage.getItem('token')

  // 白名单（登录页）直接放行
  if (whiteList.includes(to.path)) {
    // 已登录用户访问登录页 → 跳首页
    if (token) {
      next('/')
    } else {
      next()
    }
    return
  }

  // 需要登录但没 token → 跳登录页
  if (!token) {
    next(`/login?redirect=${to.path}`)
    return
  }

  // 有 token 但还没加载用户信息 → 尝试恢复
  const authStore = (await import('../stores/auth')).useAuthStore()
  if (!authStore.isLoggedIn) {
    const ok = await authStore.restoreSession()
    if (!ok) {
      next(`/login?redirect=${to.path}`)
      return
    }
  }

  // 角色权限校验（如果路由配置了 roles）
  const requiredRoles = to.meta?.roles as string[] | undefined
  if (requiredRoles && requiredRoles.length > 0) {
    const userRoles = authStore.roles.map(r => r.roleKey)
    const hasRole = requiredRoles.some(r => r === 'admin' || userRoles.includes(r))
    if (!hasRole) {
      // 没有权限，跳转到默认工作台
      next(authStore.defaultRoute)
      return
    }
  }

  next()
})

export default router
