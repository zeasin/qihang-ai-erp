<template>
  <div class="picking-workspace" :class="{ 'pda-mode': pdaMode }">
    <el-container style="height: 100vh">

      <!-- ─── 顶部栏 ─── -->
      <el-header class="pw-header">
        <div class="pw-left">
          <el-button text @click="$router.push('/')" size="small">← 首页</el-button>
          <span class="pw-title">🔍 拣货工作台</span>
        </div>
        <div class="pw-right">
          <el-tag :type="pdaMode ? 'primary' : 'info'" size="small" style="cursor:pointer" @click="pdaMode = !pdaMode">
            {{ pdaMode ? '📱 PDA 模式' : '🖥️ 桌面模式' }}
          </el-tag>
          <el-tag type="success" effect="dark" size="small">
            {{ stats.done }}/{{ stats.total }} 已完成
          </el-tag>
          <el-tag type="warning" effect="dark" size="small" v-if="stats.mistakes > 0">
            ⚠️ {{ stats.mistakes }} 异常
          </el-tag>
        </div>
      </el-header>

      <el-container>
        <!-- ─── 左侧：波次任务列表 ─── -->
        <el-aside :width="pdaMode ? '0' : '320px'" class="pw-sidebar" :class="{ collapsed: pdaMode }">
          <div class="sidebar-inner">
            <div class="pick-tabs">
              <el-tabs v-model="activeWave" stretch>
                <el-tab-pane v-for="wave in waves" :key="wave.id" :label="`${wave.name} (${wave.remaining})`" :name="wave.id">
                  <div class="location-list">
                    <div v-for="loc in wave.locations" :key="loc.id"
                      :class="['loc-item', {
                        'loc-current': currentLoc?.id === loc.id,
                        'loc-done': loc.done,
                      }]"
                      @click="jumpToLocation(loc)">
                      <div class="loc-code">{{ loc.code }}</div>
                      <div class="loc-info">
                        <div class="loc-sku">{{ loc.skuName }}</div>
                        <div class="loc-qty">× {{ loc.qty }}</div>
                      </div>
                      <div v-if="loc.done" class="loc-check">✅</div>
                    </div>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
          </div>
        </el-aside>

        <!-- ─── 主拣货区 ─── -->
        <el-main class="pw-main">
          <!-- 无任务状态 -->
          <div v-if="!currentLoc" class="pw-idle">
            <div class="idle-icon">📦</div>
            <h2>选择拣货任务开始拣货</h2>
            <p class="idle-hint">扫描货位码 / 扫描订单号 / 点击左侧任务</p>

            <div class="scan-simulator">
              <el-input
                ref="scanInput"
                v-model="scanBuffer"
                placeholder="扫描货位码或订单号..."
                size="large"
                clearable
                @keyup.enter="handleScan"
              >
                <template #prefix><el-icon><Search /></el-icon></template>
                <template #append>
                  <el-button @click="handleScan" type="primary">扫码</el-button>
                </template>
              </el-input>
            </div>

            <div class="recent-tasks" v-if="recentPicks.length">
              <div class="recent-title">最近拣货记录</div>
              <div v-for="r in recentPicks.slice(-3).reverse()" :key="r.time" class="recent-item">
                <span>{{ r.sku }}</span>
                <span class="recent-qty">×{{ r.qty }}</span>
                <span class="recent-time">{{ r.time }}</span>
              </div>
            </div>
          </div>

          <!-- 拣货进行中 -->
          <div v-else class="pw-active">
            <!-- 进度条 -->
            <div class="progress-section">
              <div class="progress-bar-bg">
                <div class="progress-bar-fill" :style="{ width: progressPercent + '%' }"></div>
              </div>
              <div class="progress-text">波次进度：{{ stats.done }}/{{ stats.total }}</div>
            </div>

            <!-- 当前位置卡片（大字号，远距离可见） -->
            <div class="loc-card" @click="speakLocation">
              <div class="loc-badge">{{ currentLoc.code }}</div>
              <div class="loc-detail">
                <div class="loc-sku-big">{{ currentLoc.skuName }}</div>
                <div class="loc-qty-big">需拣 <strong>{{ currentLoc.qty }}</strong> 件</div>
              </div>
            </div>

            <!-- 辅助信息 -->
            <div class="loc-extra">
              <el-descriptions :column="3" size="small" border>
                <el-descriptions-item label="商品编码">{{ currentLoc.skuCode }}</el-descriptions-item>
                <el-descriptions-item label="订单号" v-if="currentLoc.orderNum">{{ currentLoc.orderNum }}</el-descriptions-item>
                <el-descriptions-item label="货位">{{ currentLoc.code }}</el-descriptions-item>
              </el-descriptions>
            </div>

            <!-- 操作区 -->
            <div class="pick-actions">
              <el-button type="success" size="large" class="action-btn" @click="confirmPick" :loading="picking">
                ✅ 确认拣货
              </el-button>
              <el-button type="danger" size="large" class="action-btn" @click="reportShortage">
                ⚠️ 缺货 / 数量不符
              </el-button>
              <el-button @click="speakLocation" class="action-btn-secondary">
                🔊 语音播报
              </el-button>
            </div>

            <!-- 语音状态 -->
            <div class="voice-status" v-if="voiceEnabled">
              <el-tag size="small" type="info" effect="plain">
                🔊 {{ currentLoc.code }} — {{ currentLoc.skuName }}，需拣 {{ currentLoc.qty }} 件
              </el-tag>
            </div>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

// ─── 状态 ───
const pdaMode = ref(false)
const scanBuffer = ref('')
const picking = ref(false)
const voiceEnabled = ref(true)
const activeWave = ref('wave-1')
const scanInput = ref<any>(null)

// ─── 数据 ───
interface PickLocation {
  id: string
  code: string
  skuName: string
  skuCode: string
  qty: number
  orderNum?: string
  done: boolean
}

interface Wave {
  id: string
  name: string
  remaining: number
  locations: PickLocation[]
}

const waves = reactive<Wave[]>([
  {
    id: 'wave-1',
    name: '波次 #1',
    remaining: 4,
    locations: [
      { id: 'l1', code: 'A-01-03', skuName: '运动鞋 白色/42码', skuCode: 'SKU1001', qty: 2, orderNum: 'JD20260701001', done: false },
      { id: 'l2', code: 'B-02-05', skuName: '休闲T恤 黑色/L', skuCode: 'SKU1002', qty: 3, orderNum: 'JD20260701001', done: false },
      { id: 'l3', code: 'A-03-01', skuName: '帆布袋 蓝色/均码', skuCode: 'SKU1003', qty: 1, orderNum: 'TB20260701002', done: false },
      { id: 'l4', code: 'C-01-08', skuName: '蓝牙耳机 金色/256G', skuCode: 'SKU1004', qty: 3, orderNum: 'DD20260701003', done: false },
    ],
  },
  {
    id: 'wave-2',
    name: '波次 #2',
    remaining: 3,
    locations: [
      { id: 'l5', code: 'A-02-01', skuName: '保温杯 红色/小号', skuCode: 'SKU1005', qty: 5, orderNum: 'JD20260630004', done: false },
      { id: 'l6', code: 'D-01-04', skuName: '钥匙扣 随机色', skuCode: 'SKU1006', qty: 10, orderNum: 'PDD20260630005', done: false },
      { id: 'l7', code: 'B-01-07', skuName: '卫衣 深灰/XL', skuCode: 'SKU1007', qty: 1, orderNum: 'TB20260630006', done: false },
    ],
  },
])

const currentLoc = ref<PickLocation | null>(null)

const recentPicks = reactive<Array<{ sku: string; qty: number; time: string }>>([])

const stats = reactive({ done: 0, total: 7, mistakes: 0 })

const progressPercent = computed(() =>
  stats.total > 0 ? Math.round((stats.done / stats.total) * 100) : 0
)

// ─── 扫码输入模拟 ───
function handleScan() {
  const code = scanBuffer.value.trim()
  if (!code) return

  // 匹配货位码
  for (const wave of waves) {
    const found = wave.locations.find(
      l => l.code.toUpperCase() === code.toUpperCase() && !l.done
    )
    if (found) {
      currentLoc.value = found
      scanBuffer.value = ''
      speakLocation()
      return
    }
  }
  ElMessage.warning(`未找到待拣货位: ${code}`)
  scanBuffer.value = ''
}

function jumpToLocation(loc: PickLocation) {
  if (loc.done) {
    ElMessage.info('该货位已拣货完成')
    return
  }
  currentLoc.value = loc
  speakLocation()
}

// ─── 拣货操作 ───
function confirmPick() {
  if (!currentLoc.value) return
  picking.value = true

  // 模拟耗时
  setTimeout(() => {
    const loc = currentLoc.value!
    loc.done = true
    stats.done++

    // 更新波次剩余
    const wave = waves.find(w => w.locations.includes(loc))
    if (wave) wave.remaining = wave.locations.filter(l => !l.done).length

    recentPicks.push({ sku: loc.skuName, qty: loc.qty, time: new Date().toLocaleTimeString() })

    ElMessage.success({
      message: `✅ ${loc.skuName} ×${loc.qty} 拣货完成`,
      duration: 2000,
    })

    // 自动播报下一位置
    const next = findNextLocation()
    if (next) {
      currentLoc.value = next
      setTimeout(() => speakLocation(), 500)
    } else {
      currentLoc.value = null
      ElMessageBox.alert('🎉 该波次所有拣货任务已完成！', '波次完成', { type: 'success' })
    }

    picking.value = false
  }, 400)
}

function reportShortage() {
  if (!currentLoc.value) return
  ElMessageBox.prompt(
    `实际数量（应拣 ${currentLoc.value.qty} 件）`,
    '缺货 / 数量不符',
    { inputValue: '0', inputType: 'number' }
  ).then(({ value }) => {
    stats.mistakes++
    ElMessage.warning(`已记录异常：${currentLoc.value!.skuName} 实拣 ${value} 件（应拣 ${currentLoc.value!.qty} 件）`)
    // 仍标记完成，避免阻塞流程，但记入异常
    currentLoc.value!.done = true
    stats.done++
    const next = findNextLocation()
    currentLoc.value = next
  }).catch(() => {})
}

function findNextLocation(): PickLocation | null {
  for (const wave of waves) {
    for (const loc of wave.locations) {
      if (!loc.done) return loc
    }
  }
  return null
}

// ─── 语音播报 ───
function speakLocation() {
  if (!voiceEnabled.value || !currentLoc.value) return
  const loc = currentLoc.value
  if ('speechSynthesis' in window) {
    const utter = new SpeechSynthesisUtterance(
      `货位 ${loc.code}，${loc.skuName}，需拣 ${loc.qty} 件`
    )
    utter.lang = 'zh-CN'
    utter.rate = 0.9
    window.speechSynthesis.cancel()
    window.speechSynthesis.speak(utter)
  }
}

// ─── 自动播报首个任务 ───
onMounted(() => {
  const first = findNextLocation()
  if (first) {
    currentLoc.value = first
    setTimeout(() => speakLocation(), 800)
  }
})
</script>

<style scoped>
.picking-workspace {
  --brand: #409eff;
  --success: #67c23a;
  --danger: #f56c6c;
  --bg: #f0f2f5;
}

/* ─── PDA 模式全局放大 ─── */
.pda-mode {
  font-size: 1.15rem;
}
.pda-mode .action-btn {
  font-size: 1.2rem;
  padding: 24px 48px;
  height: auto;
  width: 100%;
}
.pda-mode .loc-card {
  padding: 32px 40px;
}
.pda-mode .loc-badge {
  font-size: 3rem;
}

/* ─── 头部 ─── */
.pw-header {
  display: flex; justify-content: space-between; align-items: center;
  background: #fff; border-bottom: 2px solid #e8ecf1;
  padding: 0 20px; height: 52px;
}
.pw-left { display: flex; align-items: center; gap: 8px; }
.pw-title { font-weight: 600; font-size: 15px; }
.pw-right { display: flex; gap: 8px; align-items: center; }

/* ─── 侧栏 ─── */
.pw-sidebar {
  background: #f8f9fb; border-right: 1px solid #e8ecf1;
  overflow-y: auto; transition: width 0.2s;
}
.pw-sidebar.collapsed {
  width: 0 !important; overflow: hidden; border: none;
}
.sidebar-inner { padding: 12px; }
.location-list { display: flex; flex-direction: column; gap: 6px; }
.loc-item {
  display: flex; align-items: center; gap: 10px;
  padding: 10px 12px; border-radius: 8px;
  background: #fff; border: 2px solid transparent;
  cursor: pointer; transition: all 0.12s;
}
.loc-item:hover { border-color: #d9e1ec; }
.loc-current { border-color: var(--brand) !important; background: #f0f7ff; }
.loc-done { opacity: 0.5; background: #f5f5f5; }
.loc-code {
  font-weight: 700; font-size: 16px; color: var(--brand);
  background: #eef4ff; padding: 4px 10px; border-radius: 6px;
  font-family: 'SF Mono', monospace;
}
.loc-done .loc-code { background: #e8f5e9; color: var(--success); }
.loc-info { flex: 1; min-width: 0; }
.loc-sku { font-size: 13px; color: #333; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.loc-qty { font-size: 12px; color: #999; }
.loc-check { font-size: 18px; flex-shrink: 0; }

/* ─── 主区域 ─── */
.pw-main {
  background: #fff; display: flex; align-items: center; justify-content: center;
  padding: 40px;
}

/* 空闲状态 */
.pw-idle { text-align: center; max-width: 480px; }
.idle-icon { font-size: 64px; margin-bottom: 16px; }
.idle-hint { color: #999; margin: 8px 0 32px; }
.scan-simulator { margin-bottom: 32px; }
.recent-tasks { text-align: left; }
.recent-title { font-weight: 600; font-size: 13px; color: #666; margin-bottom: 8px; }
.recent-item { display: flex; gap: 12px; font-size: 13px; padding: 4px 0; color: #333; }
.recent-qty { color: var(--brand); font-weight: 600; }
.recent-time { margin-left: auto; color: #999; font-size: 11px; }

/* 拣货中 */
.pw-active { width: 100%; max-width: 700px; }

/* 进度 */
.progress-section { margin-bottom: 24px; }
.progress-bar-bg {
  height: 8px; background: #e8ecf1; border-radius: 4px; overflow: hidden;
}
.progress-bar-fill {
  height: 100%; background: linear-gradient(90deg, var(--success), #8bc34a);
  border-radius: 4px; transition: width 0.4s;
}
.progress-text { font-size: 12px; color: #666; margin-top: 6px; text-align: right; }

/* 货位卡片 */
.loc-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff; border-radius: 16px; padding: 28px 36px;
  display: flex; align-items: center; gap: 28px;
  cursor: pointer; user-select: none;
  box-shadow: 0 8px 24px rgba(102,126,234,0.3);
  margin-bottom: 16px;
}
.loc-badge {
  font-size: 2.4rem; font-weight: 800; font-family: 'SF Mono', monospace;
  letter-spacing: 2px;
  background: rgba(255,255,255,0.18);
  padding: 12px 20px; border-radius: 12px;
}
.loc-detail { flex: 1; }
.loc-sku-big { font-size: 1.5rem; font-weight: 600; margin-bottom: 4px; }
.loc-qty-big { font-size: 1.1rem; opacity: 0.9; }
.loc-qty-big strong { font-size: 1.6rem; }

/* 辅助信息 */
.loc-extra { margin-bottom: 20px; }

/* 操作按钮 */
.pick-actions {
  display: flex; gap: 12px; flex-wrap: wrap;
}
.pick-actions .action-btn {
  flex: 1; min-width: 180px;
}
.pick-actions .action-btn-secondary {
  min-width: 120px;
}

/* 语音状态 */
.voice-status { margin-top: 16px; }
</style>
