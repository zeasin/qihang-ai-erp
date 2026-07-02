<template>
  <div class="system-layout">
    <el-container style="height: 100vh">
      <el-header class="sys-header">
        <el-button text @click="$router.push('/')">← 首页</el-button>
        <span class="sys-title">⚙️ 系统管理</span>
      </el-header>
      <el-container>
        <el-aside width="200px" class="sys-sidebar">
          <el-menu
            :default-active="activeMenu"
            router
            style="border-right: none"
          >
            <el-menu-item index="/system/menus">
              <el-icon><Menu /></el-icon>
              <span>菜单管理</span>
            </el-menu-item>
            <el-menu-item index="/system/roles">
              <el-icon><UserFilled /></el-icon>
              <span>角色管理</span>
            </el-menu-item>
            <el-menu-item index="/system/users">
              <el-icon><Avatar /></el-icon>
              <span>用户管理</span>
            </el-menu-item>

            <el-menu-item index="/system/dicts">
              <el-icon><Notebook /></el-icon>
              <span>字典管理</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main class="sys-main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Menu, UserFilled, Avatar, Notebook } from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style scoped>
.sys-header {
  display: flex; align-items: center; gap: 12px;
  background: #fff; border-bottom: 1px solid #eee;
  padding: 0 20px; height: 56px;
}
.sys-title { font-weight: 600; font-size: 16px; }
.sys-sidebar { background: #f8f9fb; border-right: 1px solid #eee; padding-top: 8px; }
.sys-main { background: #f5f7fa; padding: 20px; overflow-y: auto; }
</style>
