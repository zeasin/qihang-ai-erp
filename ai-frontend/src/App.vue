<template>
  <div id="ai-platform">
    <router-view />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from './stores/auth'

const router = useRouter()
const authStore = useAuthStore()

onMounted(async () => {
  const token = localStorage.getItem('token')
  if (token && !authStore.isLoggedIn) {
    const ok = await authStore.restoreSession()
    if (!ok && router.currentRoute.value.path !== '/login') {
      router.push('/login')
    }
  }
})
</script>

<style>
body {
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'PingFang SC', 'Microsoft YaHei', sans-serif;
}
</style>