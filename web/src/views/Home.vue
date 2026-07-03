<template>
  <div class="home">
    <el-container>
      <el-header class="header">
        <div class="logo">启航AI ERP</div>
        <div class="header-actions">
          <el-button v-if="hasPerm('chat:view')" @click="$router.push('/chat')" type="primary" size="small">
            🤖 AI对话
          </el-button>
          <el-button v-if="hasPerm('dashboard:view')" @click="$router.push('/dashboard')" size="small">
            📊 智能看板
          </el-button>
          <el-dropdown trigger="click" v-if="authStore.isLoggedIn">
            <el-button size="small" class="user-btn">
              <span class="user-avatar">{{ authStore.userName.charAt(0) }}</span>
              {{ authStore.userName }}
              <el-icon><ArrowDown /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item disabled>
                  <div style="font-size:12px;color:#999">
                    {{ authStore.roles.map(r => r.roleName).join(' / ') }}
                  </div>
                </el-dropdown-item>
                <el-dropdown-item v-if="hasPerm('system:manage')" divided @click="goSystem">
                  ⚙️ 系统管理
                </el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main">
        <div class="welcome">
          <h1>欢迎使用启航AI ERP</h1>
          <p class="subtitle">以 AI 之力，重塑企业未来</p>

          <el-card class="search-card">
            <el-input
              v-model="searchQuery"
              placeholder="输入问题，如：昨天卖了多少单？哪些商品库存不足？"
              size="large"
              @keyup.enter="goChat"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
              <template #append>
                <el-button @click="goChat" type="primary">AI查询</el-button>
              </template>
            </el-input>
          </el-card>

          <!-- ─── 动态工作台入口 (按权限渲染) ─── -->
          <div class="section-label" v-if="workspaceMenus.length > 0">👷 我的工作台</div>
          <div class="workspace-grid" v-if="workspaceMenus.length > 0">
            <div v-for="m in workspaceMenus" :key="m.menuId"
              class="ws-card" @click="goMenu(m)">
              <div class="ws-icon">{{ m.icon || '📄' }}</div>
              <div class="ws-info">
                <div class="ws-name">{{ m.menuName }}</div>
                <div class="ws-desc">{{ m.perms }}</div>
              </div>
              <el-icon class="ws-arrow"><ArrowRight /></el-icon>
            </div>
          </div>

          <!-- ─── 系统管理入口 ─── -->
          <div class="section-label" v-if="hasPerm('system:manage')">⚙️ 系统管理</div>
          <div class="workspace-grid" v-if="hasPerm('system:manage')">
            <div v-for="m in sysMenus" :key="m.menuId"
              class="ws-card" @click="goMenu(m)">
              <div class="ws-icon">{{ m.icon || '⚙️' }}</div>
              <div class="ws-info">
                <div class="ws-name">{{ m.menuName }}</div>
                <div class="ws-desc">{{ m.perms }}</div>
              </div>
              <el-icon class="ws-arrow"><ArrowRight /></el-icon>
            </div>
          </div>

          <!-- ─── 快捷操作 ─── -->
          <div class="section-label" style="margin-top:24px">🔍 快捷操作</div>
          <div class="quick-actions">
            <el-card v-for="item in quickItems" :key="item.title"
              :body-style="{ padding: '16px' }"
              class="quick-card"
              @click="quickAction(item)">
              <div class="quick-icon">{{ item.icon }}</div>
              <div class="quick-title">{{ item.title }}</div>
              <div class="quick-desc">{{ item.desc }}</div>
            </el-card>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, ArrowDown, ArrowRight } from '@element-plus/icons-vue'
import { useAuthStore } from '../stores/auth'
import request from '../utils/request'

const router = useRouter()
const authStore = useAuthStore()
const searchQuery = ref('')
const menus = ref<any[]>([])

// 用户拥有的所有菜单权限
const userPerms = computed(() => authStore.permissions || [])

// 有权限的工作台菜单 (类型为 C，有 path)
const workspaceMenus = computed(() =>
  menus.value.filter(m =>
    m.menuType === 'C' && m.path &&
    m.menuId < 13  // 非系统管理菜单
  )
)

// 系统管理子菜单
const sysMenus = computed(() =>
  menus.value.filter(m =>
    m.menuType === 'C' && m.path &&
    m.menuId >= 13
  )
)

function hasPerm(perm: string) {
  return userPerms.value.includes(perm) || userPerms.value.includes('*:*:*')
}

function goMenu(m: any) {
  if (m.path) router.push(m.path)
}

function goSystem() {
  router.push('/system/menus')
}

const quickItems = computed(() => {
  const items: any[] = []
  if (hasPerm('workspace:order:view')) {
    items.push({ icon: '📦', title: '查订单', desc: '查询订单状态和详情', action: '/workspace/order' })
  }
  if (hasPerm('workspace:picking:view')) {
    items.push({ icon: '🔍', title: '拣货任务', desc: '查看今日拣货任务', action: '/workspace/picking' })
  }
  if (hasPerm('dashboard:view')) {
    items.push({ icon: '📊', title: '销售数据', desc: '查看今日销售情况', action: '/dashboard' })
  }
  if (hasPerm('chat:view')) {
    items.push({ icon: '📈', title: '数据分析', desc: 'AI智能分析解读', action: '/chat?q=销售分析' })
  }
  return items
})

function goChat() {
  if (searchQuery.value.trim()) {
    router.push(`/chat?q=${encodeURIComponent(searchQuery.value)}`)
  } else {
    router.push('/chat')
  }
}

function quickAction(item: any) {
  router.push(item.action)
}

async function handleLogout() {
  await authStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}

// 加载菜单
onMounted(async () => {
  if (authStore.isLoggedIn) {
    try {
      const res: any = await request.get('/sys-api/system/menu/tree')
      menus.value = res.data || []
      // 扁平化
      const flat: any[] = []
      function flatten(list: any[]) {
        for (const m of list) {
          flat.push(m)
          if (m.children) flatten(m.children)
        }
      }
      flatten(menus.value)
      menus.value = flat
    } catch {}
  }
})
</script>

<style scoped>
.section-label {
  font-size: 16px; font-weight: 600; color: #333;
  margin: 32px 0 16px; text-align: left; max-width: 800px; margin-left: auto; margin-right: auto;
}
.workspace-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 12px;
  max-width: 800px;
  margin: 0 auto 8px;
}
.ws-card {
  display: flex;
  align-items: center;
  gap: 14px;
  background: #fff;
  border: 1px solid #e8ecf1;
  border-radius: 10px;
  padding: 16px 18px;
  cursor: pointer;
  transition: all 0.15s;
}
.ws-card:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(64,158,255,0.12);
  transform: translateY(-1px);
}
.ws-icon { font-size: 28px; flex-shrink: 0; }
.ws-info { flex: 1; min-width: 0; text-align: left; }
.ws-name { font-weight: 600; font-size: 14px; color: #333; }
.ws-desc { font-size: 12px; color: #999; margin-top: 2px; }
.ws-arrow { color: #ccc; font-size: 16px; }
.user-btn { display: flex; align-items: center; gap: 6px; }
.user-avatar {
  display: inline-flex; align-items: center; justify-content: center;
  width: 24px; height: 24px; border-radius: 50%;
  background: #409eff; color: #fff; font-size: 12px; font-weight: 600;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #eee;
  padding: 0 24px;
  height: 60px;
}
.logo {
  font-size: 18px;
  font-weight: 600;
  color: #409eff;
}
.main {
  max-width: 1000px;
  margin: 60px auto;
  text-align: center;
}
.welcome h1 {
  font-size: 32px;
  margin-bottom: 8px;
}
.subtitle {
  color: #666;
  font-size: 16px;
  margin-bottom: 40px;
}
.search-card {
  max-width: 700px;
  margin: 0 auto 40px;
}
.quick-actions {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 16px;
  max-width: 800px;
  margin: 0 auto;
}
.quick-card {
  cursor: pointer;
  transition: transform 0.2s;
}
.quick-card:hover {
  transform: translateY(-2px);
}
.quick-icon {
  font-size: 28px;
  margin-bottom: 8px;
}
.quick-title {
  font-weight: 600;
  margin-bottom: 4px;
}
.quick-desc {
  font-size: 12px;
  color: #999;
}
</style>
