import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import request from '../utils/request'
import { api } from '../utils/api'

// 不需要登录就能访问的白名单
const whiteList = ['/login']

// Vite glob: 预加载所有视图组件，供动态路由使用
const viewModules = import.meta.glob('../views/**/*.vue')

// ─── 静态路由（固定不变） ───
const staticRoutes: RouteRecordRaw[] = [
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
]

const router = createRouter({
  history: createWebHistory(),
  routes: staticRoutes,
})

// ─── 动态路由加载状态 ───
let dynamicRoutesLoaded = false

function resolveComponent(component: string) {
  const path = `../views/${component}.vue`
  return viewModules[path]
}

/**
 * 根据菜单树生成动态路由
 * - M 类（目录）：作为父路由，component 指向 Layout，children 为子菜单
 * - C 类且 parent_id=0（独立页面）：作为独立路由，无 Layout 包裹
 * - C 类且有 M 父级：作为父路由的 children
 * - F 类（按钮）：不生成路由
 */
function generateRoutes(menuTree: any[]): RouteRecordRaw[] {
  const routes: RouteRecordRaw[] = []

  for (const menu of menuTree) {
    if (menu.menuType === 'M' && menu.path && menu.component) {
      // ── 目录：父路由 + Layout ──
      const children: RouteRecordRaw[] = []
      if (menu.children) {
        for (const child of menu.children) {
          if (child.menuType === 'C' && child.path && child.component) {
            // 子路由 path 取相对路径（去掉父级前缀）
            const relativePath = child.path.startsWith(menu.path + '/')
              ? child.path.slice(menu.path.length + 1)
              : child.path
            children.push({
              path: relativePath,
              name: `menu-${child.menuId}`,
              component: resolveComponent(child.component),
              meta: { requiresAuth: true, title: child.menuName, perms: child.perms },
            })
          }
        }
      }
      routes.push({
        path: menu.path,
        component: resolveComponent(menu.component),
        redirect: children[0] ? `${menu.path}/${children[0].path}` : undefined,
        meta: { requiresAuth: true, title: menu.menuName, perms: menu.perms },
        children,
      })
    } else if (menu.menuType === 'C' && menu.path && menu.component && menu.parentId === 0) {
      // ── 独立页面（无 Layout 包裹） ──
      routes.push({
        path: menu.path,
        name: `menu-${menu.menuId}`,
        component: resolveComponent(menu.component),
        meta: { requiresAuth: true, title: menu.menuName, perms: menu.perms },
      })
    }
  }

  return routes
}

async function loadDynamicRoutes() {
  if (dynamicRoutesLoaded) return
  try {
    const res: any = await request.get(api.menuTree)
    const menuTree: any[] = res.data || []
    const dynamicRoutes = generateRoutes(menuTree)
    for (const route of dynamicRoutes) {
      router.addRoute(route)
    }
    dynamicRoutesLoaded = true
  } catch (e) {
    console.error('加载动态路由失败', e)
  }
}

/** 登出时调用：清除动态路由，下次登录重新加载 */
export function resetDynamicRoutes() {
  const staticNames = new Set(['login', 'home'])
  router.getRoutes().forEach(r => {
    if (r.name && !staticNames.has(r.name as string)) {
      router.removeRoute(r.name)
    }
  })
  dynamicRoutesLoaded = false
}

// ─── 导航守卫 ───
router.beforeEach(async (to, _from, next) => {
  const token = localStorage.getItem('token')

  // 白名单（登录页）直接放行
  if (whiteList.includes(to.path)) {
    if (token) {
      next('/')
    } else {
      // 回到登录页时清除动态路由
      if (dynamicRoutesLoaded) resetDynamicRoutes()
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

  // 加载动态路由（仅首次）
  if (!dynamicRoutesLoaded) {
    await loadDynamicRoutes()
    // 动态路由添加后，重新匹配当前目标
    next({ ...to, replace: true })
    return
  }

  // 权限校验：基于路由 meta.perms
  const requiredPerms = to.meta?.perms as string | undefined
  if (requiredPerms) {
    const hasPerm =
      authStore.permissions.includes(requiredPerms) ||
      authStore.permissions.includes('*:*:*') ||
      authStore.roles.some(r => r.roleKey === 'admin')
    if (!hasPerm) {
      next(authStore.defaultRoute)
      return
    }
  }

  next()
})

export default router
