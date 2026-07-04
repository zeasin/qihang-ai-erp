<template>
  <div class="page-card">
    <div class="page-header">
      <h3>库存总览</h3>
    </div>

    <el-form :model="query" inline style="margin-bottom:12px">
      <el-form-item label="搜索">
        <el-input v-model="query.keyword" placeholder="商品名称 / SKU编码 / 规格" clearable style="width:240px" @keyup.enter="search" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" stripe border size="small" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="商品" min-width="200">
        <template #default="{ row }">
          <div style="display:flex;align-items:center;gap:8px">
            <el-image v-if="row.goodsImg" :src="row.goodsImg" style="width:36px;height:36px;border-radius:4px" fit="cover" />
            <div>
              <div>{{ row.goodsName }}</div>
              <div v-if="row.skuName" style="font-size:12px;color:#999">{{ row.skuName }}</div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="skuCode" label="SKU编码" width="120" />
      <el-table-column label="当前库存" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="(row.quantity || 0) > 0 ? 'success' : 'danger'" size="small">{{ row.quantity ?? 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="showLog(row)">日志</el-button>
          <el-button size="small" type="warning" link @click="openAdjust(row)">调整</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top:12px;text-align:right">
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" :page-sizes="[10,20,50]"
        layout="total,sizes,prev,pager,next" @change="fetchData" />
    </div>

    <el-dialog v-model="adjustVisible" title="库存调整" width="500px">
      <el-form :model="adjustForm" label-width="100px" size="small">
        <el-form-item label="SKU">
          <div style="font-size:13px">{{ adjustForm.skuName }} ({{ adjustForm.skuCode }})</div>
        </el-form-item>
        <el-form-item label="当前库存">
          <el-tag :type="(adjustForm.currentQty || 0) > 0 ? 'success' : 'danger'" size="small">{{ adjustForm.currentQty ?? 0 }}</el-tag>
        </el-form-item>
        <el-form-item label="调整数量">
          <el-input-number v-model="adjustForm.changeQuantity" :min="-99999" :max="99999" size="small" style="width:160px" />
          <div style="font-size:12px;color:#999;margin-top:4px">正数=增加，负数=减少</div>
        </el-form-item>
        <el-form-item label="调整后库存">
          <span style="font-weight:600;font-size:16px">{{ (adjustForm.currentQty || 0) + (adjustForm.changeQuantity || 0) }}</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="adjustForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button size="small" @click="adjustVisible = false">取消</el-button>
        <el-button size="small" type="primary" @click="submitAdjust" :loading="adjustSubmitting">确认调整</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="logVisible" title="库存变动日志" width="700px">
      <el-table :data="logList" border size="small" v-loading="logLoading" max-height="400">
        <el-table-column label="变动前" width="70" align="center" prop="beforeQuantity" />
        <el-table-column label="变动" width="70" align="center">
          <template #default="{ row }">
            <span :style="{ color: (row.changeQuantity || 0) > 0 ? '#67c23a' : '#f56c6c' }">
              {{ (row.changeQuantity || 0) > 0 ? '+' : '' }}{{ row.changeQuantity }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="变动后" width="70" align="center" prop="afterQuantity" />
        <el-table-column label="类型" width="80" align="center">
          <template #default="{ row }">{{ typeLabel(row.type) }}</template>
        </el-table-column>
        <el-table-column label="来源单号" width="140" prop="sourceNo" />
        <el-table-column label="仓库" width="100" prop="warehouseName" />
        <el-table-column label="备注" min-width="160" prop="remark" show-overflow-tooltip />
        <el-table-column label="操作人" width="80" prop="createBy" />
        <el-table-column label="时间" width="150">
          <template #default="{ row }">{{ row.createTime ? (row.createTime + '').split('.').slice(0,1).join('').replace('T',' ') : '-' }}</template>
        </el-table-column>
      </el-table>
      <div style="margin-top:8px;text-align:right">
        <el-pagination v-model:current-page="logPage" v-model:page-size="logPageSize" :total="logTotal"
          layout="total,sizes,prev,pager,next" small @change="fetchLog" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'
import { api } from '../../utils/api'

const loading = ref(false)
const list = ref<any[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(20)

const query = reactive({ keyword: '' })

const adjustVisible = ref(false)
const adjustSubmitting = ref(false)
const adjustForm = reactive({ skuId: 0, skuName: '', skuCode: '', currentQty: 0, changeQuantity: 0, remark: '' })

const logVisible = ref(false)
const logLoading = ref(false)
const logList = ref<any[]>([])
const logTotal = ref(0)
const logPage = ref(1)
const logPageSize = ref(10)
const logSkuId = ref(0)

function typeLabel(t: number) {
  const map: Record<number, string> = { 1: '入库', 2: '出库', 3: '手动调整', 4: '退货入库' }
  return map[t] || '其他'
}

function search() { pageNum.value = 1; fetchData() }
function resetQuery() { query.keyword = ''; search() }

async function fetchData() {
  loading.value = true
  try {
    const res: any = await request.get(api.stockList, { params: { pageNum: pageNum.value, pageSize: pageSize.value, ...query } })
    list.value = res.rows || []
    total.value = res.total || 0
  } finally { loading.value = false }
}

function openAdjust(row: any) {
  adjustForm.skuId = row.skuId
  adjustForm.skuName = row.skuName || row.goodsName
  adjustForm.skuCode = row.skuCode
  adjustForm.currentQty = row.quantity || 0
  adjustForm.changeQuantity = 0
  adjustForm.remark = ''
  adjustVisible.value = true
}

async function submitAdjust() {
  if (adjustForm.changeQuantity === 0) return ElMessage.warning('调整数量不能为0')
  await ElMessageBox.confirm(
    `确认将 ${adjustForm.skuName} 的库存从 ${adjustForm.currentQty} 调整为 ${adjustForm.currentQty + adjustForm.changeQuantity}？`,
    '提示', { type: 'warning' }
  )
  adjustSubmitting.value = true
  try {
    const res: any = await request.post(api.stockAdjust, {
      skuId: adjustForm.skuId,
      changeQuantity: adjustForm.changeQuantity,
      remark: adjustForm.remark,
    })
    if (res.code === 200) { ElMessage.success('调整成功'); adjustVisible.value = false; fetchData() }
    else ElMessage.error(res.msg || '调整失败')
  } catch { ElMessage.error('请求失败') }
  finally { adjustSubmitting.value = false }
}

async function showLog(row: any) {
  logSkuId.value = row.skuId
  logPage.value = 1
  logVisible.value = true
  await fetchLog()
}

async function fetchLog() {
  logLoading.value = true
  try {
    const res: any = await request.get(api.stockLog, { params: { skuId: logSkuId.value, pageNum: logPage.value, pageSize: logPageSize.value } })
    logList.value = res.rows || []
    logTotal.value = res.total || 0
  } finally { logLoading.value = false }
}

onMounted(fetchData)
</script>