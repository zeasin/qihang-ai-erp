<template>
  <div class="login-page">
    <div class="login-container">
      <!-- 左侧品牌区 -->
      <div class="login-brand">
        <div class="brand-content">
          <div class="brand-logo">🚀</div>
          <h1 class="brand-title">启航AI ERP</h1>
          <p class="brand-subtitle">以 AI 之力，重塑企业未来</p>
          <div class="brand-features">
            <div class="feature-item">📋 订单处理 &nbsp;·&nbsp; 🔍 智能拣货 &nbsp;·&nbsp; 📦 高效打包</div>
            <div class="feature-item">🚚 发货管理 &nbsp;·&nbsp; 📊 数据分析 &nbsp;·&nbsp; 🤖 AI 助手</div>
          </div>
        </div>
      </div>

      <!-- 右侧登录表单 -->
      <div class="login-form-wrap">
        <div class="login-form-inner">
          <h2 class="form-title">用户登录</h2>
          <p class="form-desc">请输入账号和密码登录系统</p>

          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            size="large"
            class="login-form"
            @keyup.enter="handleLogin"
          >
            <el-form-item prop="username">
              <el-input
                v-model="form.username"
                placeholder="请输入账号"
                :prefix-icon="User"
                clearable
              />
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="form.password"
                type="password"
                placeholder="请输入密码"
                :prefix-icon="Lock"
                show-password
              />
            </el-form-item>

            <el-form-item prop="code" v-if="captchaEnabled">
              <div class="captcha-row">
                <el-input
                  v-model="form.code"
                  placeholder="验证码"
                  style="flex: 1"
                />
                <img
                  :src="captchaUrl"
                  class="captcha-img"
                  @click="refreshCaptcha"
                  title="点击刷新验证码"
                />
              </div>
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                :loading="loading"
                class="login-btn"
                @click="handleLogin"
              >
                {{ loading ? '登录中...' : '登 录' }}
              </el-button>
            </el-form-item>
          </el-form>

          <!-- 演示账号提示 -->
          <el-alert title="演示账号" type="info" :closable="false" style="margin-top: 16px">
            <template #default>
              <div>管理员：admin / admin123</div>
              <div>订单处理员：order / order123</div>
              <div>拣货员：picker / picker123</div>
            </template>
          </el-alert>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useAuthStore, roleWorkspaceMap } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const formRef = ref()
const loading = ref(false)
const captchaEnabled = ref(false)  // 后端可配置是否需要验证码
const captchaUrl = ref('')

const form = reactive({
  username: '',
  password: '',
  code: '',
  uuid: '',
})

const rules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 2, max: 30, message: '账号长度 2-30 位', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 30, message: '密码长度 5-30 位', trigger: 'blur' },
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
  ],
}

function refreshCaptcha() {
  // captchaUrl.value = '/api/sys-api/captchaImage?t=' + Date.now()
}

async function handleLogin() {
  if (!formRef.value) return

  await formRef.value.validate(async (valid: boolean) => {
    if (!valid) return

    loading.value = true
    try {
      await authStore.login(form.username, form.password, form.code, form.uuid)
      ElMessage.success(`欢迎回来，${authStore.userName}！`)

      // 根据角色跳转对应工作台
      const target = authStore.defaultRoute
      router.push(target)
    } catch (e: any) {
      ElMessage.error(e.message || '登录失败')
      refreshCaptcha()
    } finally {
      loading.value = false
    }
  })
}

// 初始化验证码
refreshCaptcha()
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #0f1724 0%, #1a2332 50%, #0d1b2a 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-container {
  display: flex;
  width: 900px;
  min-height: 560px;
  background: #fff;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

/* 左侧品牌 */
.login-brand {
  width: 420px;
  background: linear-gradient(135deg, #1a73e8 0%, #0d47a1 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48px;
}
.brand-content { text-align: center; }
.brand-logo { font-size: 64px; margin-bottom: 16px; }
.brand-title { font-size: 32px; font-weight: 700; margin-bottom: 8px; letter-spacing: 2px; }
.brand-subtitle { font-size: 14px; opacity: 0.8; margin-bottom: 40px; }
.brand-features { text-align: left; }
.feature-item { font-size: 13px; opacity: 0.85; padding: 6px 0; line-height: 1.8; }

/* 右侧表单 */
.login-form-wrap {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48px;
}
.login-form-inner { width: 100%; max-width: 360px; }
.form-title { font-size: 24px; font-weight: 600; color: #1a1a2e; margin-bottom: 4px; }
.form-desc { font-size: 13px; color: #999; margin-bottom: 28px; }
.login-form { width: 100%; }
.captcha-row { display: flex; gap: 12px; width: 100%; align-items: center; }
.captcha-img {
  width: 120px; height: 40px; border-radius: 6px;
  cursor: pointer; border: 1px solid #dcdfe6;
}
.login-btn { width: 100%; margin-top: 8px; }
</style>
