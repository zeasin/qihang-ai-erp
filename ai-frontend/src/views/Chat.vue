<template>
  <div class="chat-view">
    <el-container style="height: 100vh">
      <el-header class="chat-header">
        <el-button text @click="$router.push('/')">← 返回首页</el-button>
        <span class="title">🤖 AI智能助手</span>
        <el-button text @click="clearChat">清除对话</el-button>
      </el-header>

      <el-main class="chat-messages" ref="messagesRef">
        <div v-for="(msg, i) in messages" :key="i"
          :class="['message', msg.role]">
          <div class="avatar">{{ msg.role === 'user' ? '👤' : '🤖' }}</div>
          <div class="bubble">
            <div class="content" v-html="msg.content"></div>
          </div>
        </div>
        <div v-if="loading" class="message assistant">
          <div class="avatar">🤖</div>
          <div class="bubble typing">思考中...</div>
        </div>
      </el-main>

      <el-footer class="chat-footer">
        <el-input
          v-model="input"
          placeholder="输入您的问题，按Enter发送"
          size="large"
          @keyup.enter="sendMessage"
          :disabled="loading"
        >
          <template #append>
            <el-button @click="sendMessage" :disabled="loading" type="primary">
              发送
            </el-button>
          </template>
        </el-input>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const input = ref('')
const loading = ref(false)
const messages = ref<Array<{role: string, content: string}>>([
  { role: 'assistant', content: '您好！我是启航AI ERP助手。您可以问我关于订单、商品、库存、销售数据等问题，我帮您查询和分析。' }
])
const messagesRef = ref<HTMLElement>()

onMounted(() => {
  if (route.query.q) {
    input.value = route.query.q as string
    sendMessage()
  }
})

async function sendMessage() {
  if (!input.value.trim() || loading.value) return

  const question = input.value
  input.value = ''
  messages.value.push({ role: 'user', content: question })
  loading.value = true

  try {
    const res = await axios.post('/api/ai/chat', { message: question })
    messages.value.push({ role: 'assistant', content: res.data.data || res.data.msg })
  } catch (e: any) {
    messages.value.push({
      role: 'assistant',
      content: '抱歉，AI服务暂时不可用，请稍后再试。'
    })
  } finally {
    loading.value = false
    nextTick(() => {
      messagesRef.value?.scrollTo({ top: messagesRef.value.scrollHeight, behavior: 'smooth' })
    })
  }
}

function clearChat() {
  messages.value = [
    { role: 'assistant', content: '对话已清除，有什么可以帮您的？' }
  ]
}
</script>

<style scoped>
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #eee;
}
.chat-header .title {
  font-weight: 600;
  font-size: 16px;
}
.chat-messages {
  background: #f5f7fa;
  padding: 20px;
  overflow-y: auto;
}
.message {
  display: flex;
  margin-bottom: 20px;
  gap: 12px;
}
.message.user {
  flex-direction: row-reverse;
}
.avatar {
  font-size: 24px;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border-radius: 50%;
}
.bubble {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 12px;
  line-height: 1.6;
}
.user .bubble {
  background: #409eff;
  color: #fff;
  border-bottom-right-radius: 4px;
}
.assistant .bubble {
  background: #fff;
  border: 1px solid #eee;
  border-bottom-left-radius: 4px;
}
.chat-footer {
  background: #fff;
  border-top: 1px solid #eee;
  padding: 16px;
}
.typing {
  color: #999;
}
</style>