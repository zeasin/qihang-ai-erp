<template>
  <div class="home">
    <el-container>
      <el-header class="header">
        <div class="logo">启航AI · 企业业务平台</div>
        <div class="header-actions">
          <el-button @click="$router.push('/chat')" type="primary">
            🤖 AI对话
          </el-button>
          <el-button @click="$router.push('/dashboard')">
            📊 智能看板
          </el-button>
        </div>
      </el-header>

      <el-main class="main">
        <div class="welcome">
          <h1>欢迎使用启航AI企业业务平台</h1>
          <p class="subtitle">以AI为驱动力的企业级智能业务平台</p>

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
import { Search } from '@element-plus/icons-vue'

const router = useRouter()
const searchQuery = ref('')

const quickItems = [
  { icon: '📦', title: '查订单', desc: '查询订单状态和详情', action: '/chat?q=查订单' },
  { icon: '📊', title: '销售数据', desc: '查看今日销售情况', action: '/chat?q=今天销售数据' },
  { icon: '⚠️', title: '库存预警', desc: '库存不足的商品列表', action: '/chat?q=库存预警' },
  { icon: '📈', title: '数据分析', desc: 'AI智能分析解读', action: '/chat?q=销售分析' },
]

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