<template>
  <div class="basic-layout">
    <el-container style="height: 100vh">
      <el-header class="basic-header">
        <el-button text @click="$router.push('/')">← 首页</el-button>
        <span class="basic-title">📦 库存管理</span>
      </el-header>
      <el-container>
        <el-aside width="200px" class="basic-sidebar">
          <el-menu :default-active="activeMenu" router style="border-right: none">
            <el-menu-item v-for="m in childMenus" :key="m.menuId" :index="m.path">
              <el-icon v-if="m.icon"><span style="font-size:16px">{{ m.icon }}</span></el-icon>
              <span>{{ m.menuName }}</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main class="basic-main"><router-view /></el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import request from '../../utils/request'
import { api } from '../../utils/api'

const route = useRoute()
const activeMenu = computed(() => route.path)

const childMenus = ref<any[]>([])

onMounted(async () => {
  try {
    const res: any = await request.get(api.menuTree)
    const tree = res.data || []
    const menu = tree.find((m: any) => m.path === '/inventory')
    if (menu?.children) {
      childMenus.value = menu.children.filter((c: any) => c.menuType === 'C' && c.visible !== '1')
    }
  } catch { /* ignore */ }
})
</script>
<style scoped>
.basic-header { display: flex; align-items: center; gap: 12px; background: #fff; border-bottom: 1px solid #e4e7ed; padding: 0 20px; height: 48px !important; }
.basic-title { font-size: 16px; font-weight: 600; }
.basic-sidebar { background: #fff; border-right: 1px solid #e4e7ed; }
.basic-main { background: #f5f7fa; padding: 16px; }
</style>