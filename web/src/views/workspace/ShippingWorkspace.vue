<template>
  <div class="ws-placeholder">
    <el-container style="height: 100vh">
      <el-header class="ws-header">
        <el-button text @click="$router.push('/')">← 首页</el-button>
        <span class="ws-title">{{ title }}</span>
        <span></span>
      </el-header>
      <el-main class="ws-body">
        <el-result icon="info" title="开发中" sub-title="该工作台正在建设中，敬请期待">
          <template #extra>
            <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
          </template>
        </el-result>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
const route = useRoute()

const labels: Record<string, string> = {
  'workspace-picking': '🔍 拣货工作台',
  'workspace-packing': '📦 打包工作台',
  'workspace-shipping': '🚚 发货工作台',
  'workspace-receiving': '📥 收货工作台',
  'workspace-qc': '🔬 质检工作台',
  'workspace-counting': '📋 盘点工作台',
}
const title = computed(() => labels[route.name as string] || '工作台')
</script>

<style scoped>
.ws-header {
  display: flex; justify-content: space-between; align-items: center;
  background: #fff; border-bottom: 1px solid #eee;
  padding: 0 20px; height: 56px;
}
.ws-title { font-weight: 600; font-size: 16px; }
.ws-body { display: flex; align-items: center; justify-content: center; }
</style>
