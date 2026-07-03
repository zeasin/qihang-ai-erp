<template>
  <div class="basic-layout">
    <el-container style="height: 100vh">
      <el-header class="basic-header">
        <el-button text @click="$router.push('/')">← 首页</el-button>
        <span class="basic-title">{{ parentMenu?.icon }} {{ parentMenu?.menuName }}</span>
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

const parentMenu = ref<any>(null)
const childMenus = ref<any[]>([])

onMounted(async () => {
  try {
    const res: any = await request.get(api.menuTree)
    const tree: any[] = res.data || []
    // 找到基础数据目录 (menu_id=26)
    parentMenu.value = tree.find((m: any) => m.menuId === 26)
    if (parentMenu.value?.children) {
      childMenus.value = parentMenu.value.children
        .filter((m: any) => m.menuType === 'C' && m.path)
        .sort((a: any, b: any) => (a.orderNum || 0) - (b.orderNum || 0))
    }
  } catch {}
})
</script>
<style scoped>
.basic-header { display:flex; align-items:center; gap:12px; background:#fff; border-bottom:1px solid #eee; padding:0 20px; height:56px; }
.basic-title { font-weight:600; font-size:16px; }
.basic-sidebar { background:#f8f9fb; border-right:1px solid #eee; padding-top:8px; }
.basic-main { background:#f5f7fa; padding:20px; overflow-y:auto; }
</style>
