<template>
  <div class="order-workspace">
    <el-container style="height: 100vh">
      <el-header class="ws-header">
        <div class="ws-brand">
          <el-button text @click="$router.push('/')">← 首页</el-button>
          <span class="ws-title">📋 订单处理工作台</span>
        </div>
        <div class="ws-meta">
          <el-tag type="success" effect="dark">待审核 {{ pendingAudit }}</el-tag>
          <el-tag type="warning" effect="dark">待打印 {{ pendingPrint }}</el-tag>
          <el-tag type="primary" effect="dark">待推送 {{ pendingPush }}</el-tag>
        </div>
      </el-header>

      <el-container>
        <el-aside width="380px" class="ws-sidebar">
          <div class="sidebar-tabs">
            <el-tabs v-model="activeTab" stretch>
              <el-tab-pane label="待审核" name="audit">
                <div v-if="auditOrders.length === 0" class="empty-state">✅ 暂无待审核订单</div>
                <div v-for="o in auditOrders" :key="o.id"
                  :class="['order-card', { active: selected?.id === o.id }]"
                  @click="selected = o">
                  <div class="card-top">
                    <span class="order-num">{{ o.orderNum }}</span>
                    <span class="amount">¥{{ (o.amount / 100).toFixed(2) }}</span>
                  </div>
                  <div class="card-mid">{{ o.buyerName }} · {{ o.buyerMobile }}</div>
                  <div class="card-bot">
                    <el-tag size="small">{{ o.platform }}</el-tag>
                    <span class="time">{{ o.orderTime }}</span>
                  </div>
                </div>
              </el-tab-pane>
              <el-tab-pane label="待打印" name="print">
                <div v-if="printOrders.length === 0" class="empty-state">✅ 暂无待打印订单</div>
                <div v-for="o in printOrders" :key="o.id"
                  :class="['order-card', { active: selected?.id === o.id }]"
                  @click="selected = o">
                  <div class="card-top">
                    <span class="order-num">{{ o.orderNum }}</span>
                    <span class="amount">¥{{ (o.amount / 100).toFixed(2) }}</span>
                  </div>
                  <div class="card-mid">{{ o.buyerName }} · {{ o.buyerMobile }}</div>
                  <div class="card-bot">
                    <el-tag size="small">{{ o.platform }}</el-tag>
                    <el-tag size="small" type="warning" v-if="o.urgent">加急</el-tag>
                    <span class="time">{{ o.orderTime }}</span>
                  </div>
                </div>
              </el-tab-pane>
              <el-tab-pane label="待推送" name="push">
                <div v-if="pushOrders.length === 0" class="empty-state">✅ 已全部推送仓库</div>
                <div v-for="o in pushOrders" :key="o.id"
                  :class="['order-card', { active: selected?.id === o.id }]"
                  @click="selected = o">
                  <div class="card-top">
                    <span class="order-num">{{ o.orderNum }}</span>
                    <span class="amount">¥{{ (o.amount / 100).toFixed(2) }}</span>
                  </div>
                  <div class="card-mid">{{ o.buyerName }} · {{ o.buyerMobile }}</div>
                  <div class="card-bot">
                    <el-tag size="small">{{ o.platform }}</el-tag>
                    <el-tag size="small" plain>已打印</el-tag>
                    <span class="time">{{ o.orderTime }}</span>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-aside>

        <el-main class="ws-detail" v-if="selected">
          <el-descriptions :column="2" border size="small" title="📄 订单信息">
            <el-descriptions-item label="订单号">{{ selected.orderNum }}</el-descriptions-item>
            <el-descriptions-item label="下单时间">{{ selected.orderTime }}</el-descriptions-item>
            <el-descriptions-item label="买家">{{ selected.buyerName }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ selected.buyerMobile }}</el-descriptions-item>
            <el-descriptions-item label="平台">{{ selected.platform }}</el-descriptions-item>
            <el-descriptions-item label="金额">¥{{ (selected.amount / 100).toFixed(2) }}</el-descriptions-item>
            <el-descriptions-item label="收货地址" :span="2">{{ selected.address }}</el-descriptions-item>
          </el-descriptions>

          <el-card class="detail-section" shadow="never">
            <template #header><span>🧾 商品明细</span></template>
            <el-table :data="selected.items" size="small" stripe>
              <el-table-column prop="skuName" label="规格" />
              <el-table-column prop="goodsName" label="商品名" />
              <el-table-column prop="quantity" label="数量" width="60" align="center" />
              <el-table-column label="金额" width="100" align="right">
                <template #default="{ row }">¥{{ (row.amount / 100).toFixed(2) }}</template>
              </el-table-column>
            </el-table>
          </el-card>

          <div class="action-bar">
            <template v-if="activeTab === 'audit'">
              <el-button type="success" size="large" @click="passAudit" :loading="loading">✅ 审核通过</el-button>
              <el-button type="danger" size="large" @click="rejectAudit">❌ 驳回</el-button>
              <el-button @click="showAddress = !showAddress">📍 查看地图</el-button>
            </template>
            <template v-if="activeTab === 'print'">
              <el-button type="primary" size="large" @click="printWaybill" :loading="loading">🖨️ 打印电子面单</el-button>
              <el-button type="success" size="large" :disabled="true">🖨️ 批量打印（{{ printOrders.length }}单）</el-button>
            </template>
            <template v-if="activeTab === 'push'">
              <el-button type="primary" size="large" @click="pushToWarehouse" :loading="loading">📦 推送仓库发货</el-button>
              <el-button @click="printAgain">🖨️ 重新打印</el-button>
            </template>
            <el-button text @click="selected = null">关闭</el-button>
          </div>

          <el-collapse v-model="showAddress" class="map-collapse">
            <el-collapse-item title="📍 收货地址地图" name="map">
              <div class="map-placeholder">
                <p>{{ selected.address }}</p>
                <el-text type="info">（此处集成高德/百度地图）</el-text>
              </div>
            </el-collapse-item>
          </el-collapse>
        </el-main>

        <el-main class="ws-detail ws-empty" v-else>
          <div class="empty-hint">
            <span class="empty-icon">👈</span>
            <p>从左侧列表选择一个订单进行处理</p>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const activeTab = ref('audit')
const selected = ref<any>(null)
const loading = ref(false)
const showAddress = ref(false)

const pendingAudit = ref(3)
const pendingPrint = ref(7)
const pendingPush = ref(2)

interface OrderItem {
  skuName: string; goodsName: string; quantity: number; amount: number
}
interface Order {
  id: number; orderNum: string; buyerName: string; buyerMobile: string
  platform: string; amount: number; orderTime: string; address: string
  urgent?: boolean; items: OrderItem[]
}

const auditOrders = reactive<Order[]>([
  { id: 1, orderNum: 'JD20260701001', buyerName: '张三', buyerMobile: '138****1234', platform: '京东', amount: 29900, orderTime: '2026-07-01 14:23', address: '北京市朝阳区建国路88号SOHO现代城A座1508', items: [{ skuName: '白色 42码', goodsName: '运动鞋', quantity: 1, amount: 19900 }, { skuName: '黑色 L', goodsName: '休闲T恤', quantity: 2, amount: 10000 }] },
  { id: 2, orderNum: 'TB20260701002', buyerName: '李四', buyerMobile: '139****5678', platform: '淘宝', amount: 8800, orderTime: '2026-07-01 15:07', address: '上海市浦东新区张江高科博云路2号', items: [{ skuName: '蓝色 均码', goodsName: '帆布袋', quantity: 1, amount: 8800 }] },
  { id: 3, orderNum: 'DD20260701003', buyerName: '王五', buyerMobile: '136****9012', platform: '抖店', amount: 156000, orderTime: '2026-07-01 16:44', address: '广东省深圳市南山区科技园南区', items: [{ skuName: '金色 256G', goodsName: '蓝牙耳机', quantity: 3, amount: 156000 }] },
])

const printOrders = reactive<Order[]>([
  { id: 4, orderNum: 'JD20260630004', buyerName: '赵六', buyerMobile: '137****3456', platform: '京东', amount: 45000, orderTime: '2026-06-30 09:12', address: '浙江省杭州市西湖区文三路478号', urgent: true, items: [{ skuName: '红色 小号', goodsName: '保温杯', quantity: 5, amount: 45000 }] },
  { id: 5, orderNum: 'PDD20260630005', buyerName: '孙七', buyerMobile: '158****7890', platform: '拼多多', amount: 2500, orderTime: '2026-06-30 10:33', address: '四川省成都市武侯区科华北路99号', items: [{ skuName: '随机色', goodsName: '钥匙扣', quantity: 10, amount: 2500 }] },
  { id: 6, orderNum: 'TB20260630006', buyerName: '周八', buyerMobile: '186****2345', platform: '淘宝', amount: 12800, orderTime: '2026-06-30 11:55', address: '湖北省武汉市洪山区珞瑜路1037号', items: [{ skuName: '深灰 XL', goodsName: '卫衣', quantity: 1, amount: 12800 }] },
  { id: 7, orderNum: 'XHS20260630007', buyerName: '吴九', buyerMobile: '152****6789', platform: '小红书', amount: 6800, orderTime: '2026-06-30 13:18', address: '江苏省南京市鼓楼区汉口路22号', items: [{ skuName: '玫瑰味', goodsName: '香薰蜡烛', quantity: 2, amount: 6800 }] },
])

const pushOrders = reactive<Order[]>([
  { id: 8, orderNum: 'JD20260628008', buyerName: '郑十', buyerMobile: '187****0123', platform: '京东', amount: 32900, orderTime: '2026-06-28 08:05', address: '福建省厦门市思明区环岛南路', items: [{ skuName: '银色 14寸', goodsName: '笔记本电脑包', quantity: 1, amount: 32900 }] },
  { id: 9, orderNum: 'TB20260628009', buyerName: '陈一', buyerMobile: '153****4567', platform: '淘宝', amount: 99900, orderTime: '2026-06-28 09:30', address: '山东省青岛市市南区香港中路78号', items: [{ skuName: 'Pro 版本', goodsName: '机械键盘', quantity: 1, amount: 99900 }] },
])

function passAudit() {
  if (!selected.value) return
  loading.value = true
  setTimeout(() => {
    const idx = auditOrders.findIndex(o => o.id === selected.value.id)
    if (idx !== -1) {
      auditOrders.splice(idx, 1)
      printOrders.push({ ...selected.value })
      pendingAudit.value = auditOrders.length
      pendingPrint.value = printOrders.length
    }
    selected.value = null
    loading.value = false
    ElMessage.success('审核通过，已转入待打印')
  }, 600)
}

function rejectAudit() {
  if (!selected.value) return
  ElMessage.warning(`订单 ${selected.value.orderNum} 已驳回（此处弹出驳回原因对话框）`)
}

function printWaybill() {
  if (!selected.value) return
  loading.value = true
  setTimeout(() => {
    const idx = printOrders.findIndex(o => o.id === selected.value.id)
    if (idx !== -1) {
      printOrders.splice(idx, 1)
      pushOrders.push({ ...selected.value })
      pendingPrint.value = printOrders.length
      pendingPush.value = pushOrders.length
    }
    selected.value = null
    loading.value = false
    ElMessage.success('面单打印成功，已转入待推送仓库')
  }, 800)
}

function pushToWarehouse() {
  if (!selected.value) return
  loading.value = true
  setTimeout(() => {
    const idx = pushOrders.findIndex(o => o.id === selected.value.id)
    if (idx !== -1) {
      pushOrders.splice(idx, 1)
      pendingPush.value = pushOrders.length
    }
    selected.value = null
    loading.value = false
    ElMessage.success('已推送至仓库 WMS 系统')
  }, 600)
}

function printAgain() { ElMessage.info('重新打印电子面单') }
</script>

<style scoped>
.ws-header {
  display: flex; justify-content: space-between; align-items: center;
  background: #fff; border-bottom: 1px solid #eee;
  padding: 0 20px; height: 56px;
}
.ws-brand { display: flex; align-items: center; gap: 12px; }
.ws-title { font-weight: 600; font-size: 16px; }
.ws-meta { display: flex; gap: 8px; }
.ws-sidebar { background: #f8f9fb; border-right: 1px solid #eee; overflow-y: auto; }
.sidebar-tabs { padding: 12px; }
.order-card {
  background: #fff; border-radius: 8px; padding: 12px; margin-bottom: 8px;
  cursor: pointer; border: 2px solid transparent; transition: all 0.15s;
}
.order-card:hover { border-color: #d9e1ec; }
.order-card.active { border-color: #409eff; background: #f0f7ff; }
.card-top { display: flex; justify-content: space-between; font-weight: 600; margin-bottom: 4px; }
.order-num { font-size: 13px; color: #333; }
.amount { color: #e6a23c; }
.card-mid { font-size: 12px; color: #666; margin-bottom: 6px; }
.card-bot { display: flex; gap: 6px; align-items: center; }
.time { font-size: 11px; color: #999; margin-left: auto; }
.empty-state { text-align: center; padding: 40px 0; color: #67c23a; font-size: 14px; }
.ws-detail { background: #fff; padding: 24px; overflow-y: auto; }
.ws-empty { display: flex; align-items: center; justify-content: center; }
.empty-hint { text-align: center; color: #999; }
.empty-icon { font-size: 32px; }
.detail-section { margin-top: 16px; }
.action-bar { display: flex; gap: 12px; margin-top: 20px; padding: 16px 0; border-top: 1px solid #f0f0f0; flex-wrap: wrap; }
.map-collapse { margin-top: 12px; }
.map-placeholder { background: #f5f7fa; border-radius: 6px; padding: 16px; text-align: center; min-height: 80px; display: flex; flex-direction: column; justify-content: center; }
</style>
