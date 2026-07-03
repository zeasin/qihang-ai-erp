<template>
  <div class="channel-layout">
    <el-container style="height: 100vh">
      <el-header class="ch-header">
        <el-button text @click="$router.push('/')">← 首页</el-button>
        <span class="ch-title">📡 渠道管理</span>
      </el-header>
      <el-container>
        <el-aside width="200px" class="ch-sidebar">
          <el-menu :default-active="activeMenu" router style="border-right: none">
            <el-menu-item index="/channel/platforms">
              <el-icon><Connection /></el-icon><span>渠道设置</span>
            </el-menu-item>
            <el-menu-item index="/channel/shops">
              <el-icon><Shop /></el-icon><span>店铺管理</span>
            </el-menu-item>
            <el-menu-item index="/channel/merchants">
              <el-icon><OfficeBuilding /></el-icon><span>商户管理</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main class="ch-main"><router-view /></el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Connection, Shop, OfficeBuilding } from '@element-plus/icons-vue'
const route = useRoute()
const activeMenu = computed(() => route.path)
</script>
<style scoped>
.ch-header { display:flex; align-items:center; gap:12px; background:#fff; border-bottom:1px solid #eee; padding:0 20px; height:56px; }
.ch-title { font-weight:600; font-size:16px; }
.ch-sidebar { background:#f8f9fb; border-right:1px solid #eee; padding-top:8px; }
.ch-main { background:#f5f7fa; padding:20px; overflow-y:auto; }
</style>
