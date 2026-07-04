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
            <div class="content" v-html="renderContent(msg.content)"></div>
          </div>
        </div>
        <div v-if="loading && !streamingText" class="message assistant">
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

const route = useRoute()
const input = ref('')
const loading = ref(false)
const messages = ref<Array<{ role: string, content: string }>>([
  { role: 'assistant', content: '您好！我是启航AI ERP助手。您可以问我关于订单、商品、库存、销售数据等问题，我帮您查询和分析。' }
])
const messagesRef = ref<HTMLElement>()
// 流式累积的当前回答，非空时表示正在接收流
const streamingText = ref('')

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
  streamingText.value = ''

  // 先插入一条空的助手消息，流式内容追加到它
  const assistantMsg = { role: 'assistant', content: '' }
  messages.value.push(assistantMsg)

  try {
    const resp = await fetch('/api/ai/chat', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'text/event-stream'
      },
      body: JSON.stringify({ message: question })
    })

    if (!resp.ok || !resp.body) {
      throw new Error(`HTTP ${resp.status}`)
    }

    const reader = resp.body.getReader()
    const decoder = new TextDecoder('utf-8')
    let buffer = ''

    while (true) {
      const { done, value } = await reader.read()
      if (done) break
      buffer += decoder.decode(value, { stream: true })

      // SSE 事件以空行分隔，按行解析 data: 前缀
      const lines = buffer.split('\n')
      // 保留最后一个不完整的行
      buffer = lines.pop() || ''

      for (const line of lines) {
        const trimmed = line.trim()
        if (!trimmed || !trimmed.startsWith('data:')) continue
        const payload = trimmed.slice(5).trim()
        // Spring AI SSE 结束标志
        if (payload === '[DONE]') continue
        // 尝试解析 JSON（DeepSeek/OpenAI chunk 格式可能是字符串或 JSON）
        const chunk = extractText(payload)
        if (chunk) {
          streamingText.value += chunk
          assistantMsg.content = streamingText.value
          scrollToBottom()
        }
      }
    }

    // 流结束后，若助手消息仍为空，提示失败
    if (!assistantMsg.content) {
      assistantMsg.content = '（未收到回复内容）'
    }
  } catch (e: any) {
    assistantMsg.content = `抱歉，AI服务暂时不可用：${e.message || e}`
  } finally {
    streamingText.value = ''
    loading.value = false
    nextTick(scrollToBottom)
  }
}

/**
 * 从 SSE data 负载中提取文本。
 * Spring AI 的 stream().content() 返回 Flux<String>，
 * 每个事件 data 可能是裸字符串，也可能是 JSON 编码的字符串（带引号）。
 */
function extractText(payload: string): string {
  if (!payload) return ''
  // JSON 字符串形式，如 "你好"
  if (payload.startsWith('"') && payload.endsWith('"')) {
    try {
      return JSON.parse(payload)
    } catch {
      return payload
    }
  }
  // JSON 对象形式（部分适配器会包一层）
  if (payload.startsWith('{')) {
    try {
      const obj = JSON.parse(payload)
      return obj.content || obj.text || obj.delta || ''
    } catch {
      return payload
    }
  }
  return payload
}

function renderContent(content: string): string {
  // 简单换行处理，避免 v-html 转义问题
  return (content || '')
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/\n/g, '<br>')
}

function scrollToBottom() {
  nextTick(() => {
    const el = messagesRef.value
    if (el) el.scrollTo({ top: el.scrollHeight, behavior: 'smooth' })
  })
}

function clearChat() {
  messages.value = [
    { role: 'assistant', content: '对话已清除，有什么可以帮您的？' }
  ]
  streamingText.value = ''
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
  white-space: pre-wrap;
  word-break: break-word;
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
