<template>
  <div class="dashboard">
    <el-container style="height: 100vh">
      <el-header class="dash-header">
        <el-button text @click="$router.push('/')">← 返回首页</el-button>
        <span class="title">📊 智能看板</span>
        <el-button text @click="refresh">刷新数据</el-button>
      </el-header>

      <el-main>
        <el-row :gutter="20">
          <el-col :span="6" v-for="card in statCards" :key="card.title">
            <el-card shadow="hover">
              <div class="stat-value">{{ card.value }}</div>
              <div class="stat-label">{{ card.title }}</div>
            </el-card>
          </el-col>
        </el-row>

        <el-card class="insight-card" style="margin-top: 20px">
          <template #header>
            <span>🤖 AI 智能解读</span>
          </template>
          <p class="insight-text">
            当前系统运行正常。今日销售额较昨日增长12.5%，建议关注库存预警商品。
            <el-button text type="primary" @click="$router.push('/chat?q=详细分析')">
              查看详细分析 →
            </el-button>
          </p>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
const statCards = [
  { title: '今日订单', value: '—' },
  { title: '今日销售额', value: '—' },
  { title: '待发货', value: '—' },
  { title: '库存预警', value: '—' },
]

function refresh() {
  // TODO: 从AI接口获取数据
}
</script>

<style scoped>
.dash-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #eee;
}
.dash-header .title {
  font-weight: 600;
  font-size: 16px;
}
.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #409eff;
  text-align: center;
}
.stat-label {
  text-align: center;
  color: #666;
  margin-top: 4px;
}
.insight-text {
  color: #333;
  line-height: 1.8;
  font-size: 14px;
}
</style>