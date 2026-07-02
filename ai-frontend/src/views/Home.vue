<template>
  <div class="home">
    <el-container>
      <el-header class="header">
        <div class="logo">启航AI ERP</div>
        <div class="header-actions">
          <el-button @click="$router.push('/chat')" type="primary" size="small">
            🤖 AI对话
          </el-button>
          <el-button @click="$router.push('/dashboard')" size="small">
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
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main">
        <div class="welcome">
          <h1>欢迎使用启航AI ERP</h1>
          <p class="subtitle">以 AI 之力，重塑企业未来 — 智能、高效、可感知的新一代企业资源计划系统</p>

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

          <!-- ─── 角色工作台入口 ─── -->
          <div class="section-label">👷 一线操作工作台</div>
          <div class="workspace-grid">
            <div v-for="ws in workspaces" :key="ws.path"
              class="ws-card" @click="$router.push(ws.path)">
              <div class="ws-icon">{{ ws.icon }}</div>
              <div class="ws-info">
                <div class="ws-name">{{ ws.name }}</div>
                <div class="ws-desc">{{ ws.desc }}</div>
              </div>
              <el-tag size="small" :type="ws.tagType" class="ws-badge">{{ ws.badge }}</el-tag>
            </div>
          </div>

          <!-- ─── 分析管理入口 ─── -->
          <div class="section-label">🧠 管理与分析</div>
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
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, ArrowDown } from '@element-plus/icons-vue'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const searchQuery = ref('')

const workspaces = [
  { icon: '📋', name: '订单处理', desc: '审核订单 · 打印面单 · 推送仓库', path: '/workspace/order', badge: '3 待审', tagType: 'danger' },
  { icon: '🔍', name: '拣货', desc: '扫码拣货 · 校验SKU · 集货', path: '/workspace/picking', badge: '7 待拣', tagType: 'warning' },
  { icon: '📦', name: '打包', desc: '复核商品 · 装箱 · 贴单', path: '/workspace/packing', badge: '5 待包', tagType: 'warning' },
  { icon: '🚚', name: '发货', desc: '交接快递 · 称重 · 发货确认', path: '/workspace/shipping', badge: '12 待发', tagType: 'primary' },
  { icon: '📥', name: '收货', desc: '采购入库 · 退货入库 · 上架', path: '/workspace/receiving', badge: '2 待收', tagType: 'info' },
  { icon: '🔬', name: '质检', desc: '来料检验 · 出货抽检', path: '/workspace/qc', badge: '4 待检', tagType: 'info' },
  { icon: '📋', name: '盘点', desc: '盘点任务 · 差异调整', path: '/workspace/counting', badge: '1 进行中', tagType: 'info' },
]

const quickItems = [
  { icon: '📊', title: '智能看板', desc: '关键指标可视化', action: '/dashboard' },
  { icon: '🤖', title: 'AI对话', desc: '自然语言查数据做分析', action: '/chat' },
  { icon: '⚠️', title: '库存预警', desc: '库存不足的商品列表', action: '/chat?q=库存预警' },
  { icon: '📈', title: '数据分析', desc: 'AI智能分析解读', action: '/chat?q=销售分析' },
]

async function handleLogout() {
  await authStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}

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
</script>

<style scoped>
/* ─── 角色工作台网格 ─── */
.section-label {
  font-size: 16px; font-weight: 600; color: #333;
  margin: 32px 0 16px; text-align: left; max-width: 800px; margin-left: auto; margin-right: auto;
}
.workspace-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
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
.ws-badge { flex-shrink: 0; }

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
  grid-template-columns: repeat(4, 1fr);
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