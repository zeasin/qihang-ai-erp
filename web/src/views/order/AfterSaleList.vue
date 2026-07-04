<template>
  <div class="page-card">
    <div class="page-header">
      <h3>售后单列表</h3>
      <el-button type="primary" size="small" @click="openCreate">新增售后单</el-button>
    </div>

    <el-form :model="query" inline style="margin-bottom:12px">
      <el-form-item label="售后单号">
        <el-input v-model="query.afterNum" placeholder="搜索" clearable style="width:160px" @keyup.enter="search" />
      </el-form-item>
      <el-form-item label="订单号">
        <el-input v-model="query.orderNum" placeholder="搜索" clearable style="width:160px" @keyup.enter="search" />
      </el-form-item>
      <el-form-item label="类型">
        <el-select v-model="query.type" placeholder="全部" clearable style="width:120px">
          <el-option label="仅退款" :value="1" />
          <el-option label="退货退款" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" placeholder="全部" clearable style="width:120px">
          <el-option label="待处理" :value="0" />
          <el-option label="已同意" :value="1" />
          <el-option label="已退款" :value="2" />
          <el-option label="已退货" :value="3" />
          <el-option label="已取消" :value="4" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" stripe border size="small" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="afterNum" label="售后单号" width="160" />
      <el-table-column prop="orderNum" label="订单号" width="160" />
      <el-table-column label="类型" width="80" align="center">
        <template #default="{ row }">{{ row.type === 1 ? '仅退款' : '退货退款' }}</template>
      </el-table-column>
      <el-table-column prop="reason" label="原因" min-width="160" show-overflow-tooltip />
      <el-table-column prop="amount" label="退款金额" width="100" align="right" />
      <el-table-column label="商品" width="200" show-overflow-tooltip>
        <template #default="{ row }">
          <span v-if="row.itemList && row.itemList.length">{{ row.itemList.map((i:any)=>i.goodsName).join(', ') }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="showDetail(row)">详情</el-button>
          <el-button v-if="row.status === 0" size="small" type="success" link @click="approve(row)">审核</el-button>
          <el-button v-if="row.status === 1 && row.type === 1" size="small" type="warning" link @click="refund(row)">退款</el-button>
          <el-button v-if="row.status === 1 && row.type === 2" size="small" type="warning" link @click="openReturnStock(row)">退货入库</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top:12px;text-align:right">
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" :page-sizes="[10,20,50]"
        layout="total,sizes,prev,pager,next" @change="fetchData" />
    </div>

    <el-dialog v-model="detailVisible" title="售后单详情" width="700px">
      <template v-if="detail">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="售后单号" :span="2">{{ detail.afterNum }}</el-descriptions-item>
          <el-descriptions-item label="订单号">{{ detail.orderNum }}</el-descriptions-item>
          <el-descriptions-item label="类型">{{ detail.type === 1 ? '仅退款' : '退货退款' }}</el-descriptions-item>
          <el-descriptions-item label="退款金额">{{ detail.amount }}</el-descriptions-item>
          <el-descriptions-item label="原因" :span="2">{{ detail.reason }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detail.remark }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ statusLabel(detail.status) }}</el-descriptions-item>
        </el-descriptions>
        <el-divider />
        <h4 style="margin:0 0 8px">商品明细</h4>
        <el-table :data="detail.itemList || []" border size="small">
          <el-table-column prop="goodsName" label="商品" min-width="180" />
          <el-table-column prop="skuName" label="规格" width="120" />
          <el-table-column prop="skuCode" label="编码" width="100" />
          <el-table-column prop="quantity" label="数量" width="60" align="center" />
          <el-table-column prop="refundAmount" label="退款金额" width="80" align="right" />
        </el-table>
      </template>
    </el-dialog>

    <el-dialog v-model="createVisible" title="新增售后单" width="700px">
      <el-form :model="createForm" label-width="100px" size="small">
        <el-form-item label="订单号">
          <el-input v-model="createForm.orderNum" placeholder="输入订单号搜索" style="width:200px">
            <template #append><el-button @click="searchOrder">搜索</el-button></template>
          </el-input>
        </el-form-item>
        <el-form-item v-if="selectedOrder" label="订单信息">
          <div style="font-size:13px;line-height:1.6">
            <div>收件人: {{ selectedOrder.receiverName }} {{ selectedOrder.receiverMobile }}</div>
            <div>金额: {{ selectedOrder.payment }} | 状态: {{ selectedOrder.orderStatus === 2 ? '已发货' : selectedOrder.orderStatus === 3 ? '已完成' : '其他' }}</div>
          </div>
        </el-form-item>
        <el-form-item label="售后类型">
          <el-radio-group v-model="createForm.type">
            <el-radio :value="1">仅退款</el-radio>
            <el-radio :value="2">退货退款</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="售后原因">
          <el-input v-model="createForm.reason" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item v-if="selectedOrder" label="退款商品">
          <el-table :data="createItems" border size="small" max-height="300">
            <el-table-column label="商品" min-width="160">
              <template #default="{ row }">
                <div style="display:flex;align-items:center;gap:6px">
                  <el-image v-if="row.goodsImg" :src="row.goodsImg" style="width:28px;height:28px;border-radius:3px" fit="cover" />
                  <div>
                    <div>{{ row.goodsTitle }}</div>
                    <div v-if="row.goodsSpec" style="font-size:12px;color:#999">{{ row.goodsSpec }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="skuNum" label="编码" width="100" />
            <el-table-column label="数量" width="80">
              <template #default="{ row }">
                <el-input-number v-model="row.quantity" :min="0" :max="row._maxQty" size="small" style="width:70px" />
              </template>
            </el-table-column>
            <el-table-column label="退款金额" width="120">
              <template #default="{ row }">
                <el-input-number v-model="row.refundAmount" :precision="2" :min="0" size="small" style="width:100px" />
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item label="退款金额">
          <el-input-number v-model="createForm.amount" :precision="2" :min="0" style="width:200px" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="createForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button size="small" @click="createVisible = false">取消</el-button>
        <el-button size="small" type="primary" @click="submitCreate" :loading="createSubmitting">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="returnVisible" title="退货入库" width="500px" @open="loadWarehouses">
      <el-form :model="returnForm" label-width="100px" size="small">
        <el-form-item label="仓库">
          <el-select v-model="returnForm.warehouseId" placeholder="选择仓库" style="width:200px" @change="whChange">
            <el-option v-for="w in warehouseList" :key="w.id" :label="w.warehouseName" :value="w.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button size="small" @click="returnVisible = false">取消</el-button>
        <el-button size="small" type="primary" @click="submitReturnStock" :loading="returnSubmitting">确认入库</el-button>
      </template>
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

const query = reactive({ afterNum: '', orderNum: '', type: undefined as any, status: undefined as any })

const detailVisible = ref(false)
const detail = ref<any>(null)

const createVisible = ref(false)
const createSubmitting = ref(false)
const createForm = reactive({ orderId: 0, orderNum: '', type: 1, reason: '', amount: 0, remark: '', itemList: [] })
const createItems = ref<any[]>([])
const selectedOrder = ref<any>(null)

const returnVisible = ref(false)
const returnSubmitting = ref(false)
const returnForm = reactive({ afterSaleId: 0, warehouseId: undefined as any, warehouseName: '' })
const warehouseList = ref<any[]>([])

function statusType(s: number) {
  const map: Record<number, string> = { 0: 'danger', 1: 'warning', 2: 'success', 3: 'primary', 4: 'info' }
  return map[s] || 'info'
}
function statusLabel(s: number) {
  const map: Record<number, string> = { 0: '待处理', 1: '已同意', 2: '已退款', 3: '已退货', 4: '已取消' }
  return map[s] || '未知'
}

function search() { pageNum.value = 1; fetchData() }
function resetQuery() { Object.assign(query, { afterNum: '', orderNum: '', type: undefined, status: undefined }); search() }

async function fetchData() {
  loading.value = true
  try {
    const res: any = await request.get(api.afterSaleList, { params: { pageNum: pageNum.value, pageSize: pageSize.value, ...query } })
    list.value = res.rows || []
    total.value = res.total || 0
  } finally { loading.value = false }
}

async function showDetail(row: any) {
  const res: any = await request.get(api.afterSaleGet(row.id))
  detail.value = res.data
  detailVisible.value = true
}

async function openCreate() {
  createForm.orderNum = ''
  createForm.orderId = 0
  createForm.type = 1
  createForm.reason = ''
  createForm.amount = 0
  createForm.remark = ''
  createItems.value = []
  selectedOrder.value = null
  createVisible.value = true
}

async function searchOrder() {
  if (!createForm.orderNum) return ElMessage.warning('请输入订单号')
  try {
    const res: any = await request.get(api.orderList, { params: { orderNum: createForm.orderNum, pageNum: 1, pageSize: 1 } })
    const rows = res.rows || []
    if (!rows.length) return ElMessage.warning('未找到订单')
    const order = rows[0]
    const detailRes: any = await request.get(api.orderGet(order.id))
    selectedOrder.value = detailRes.data
    createForm.orderId = detailRes.data.id
    createForm.orderNum = detailRes.data.orderNum
    createItems.value = (detailRes.data.itemList || []).map((i: any) => ({
      orderItemId: i.id,
      goodsId: i.goodsId,
      skuId: i.goodsSkuId,
      goodsName: i.goodsTitle,
      goodsImage: i.goodsImg,
      goodsNum: i.goodsNum,
      skuCode: i.skuNum,
      skuName: i.goodsSpec,
      quantity: 0,
      _maxQty: i.quantity,
      refundAmount: i.goodsPrice || 0,
    }))
    createForm.amount = detailRes.data.payment || 0
  } catch { ElMessage.error('查询失败') }
}

async function submitCreate() {
  if (!createForm.orderId) return ElMessage.warning('请选择订单')
  createSubmitting.value = true
  try {
    const body = { ...createForm, itemList: createItems.value.filter((i: any) => (i.quantity || 0) > 0) }
    const res: any = await request.post(api.afterSaleCreate, body)
    if (res.code === 200) {
      ElMessage.success('售后单创建成功')
      createVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.msg || '创建失败')
    }
  } finally { createSubmitting.value = false }
}

async function approve(row: any) {
  try {
    await ElMessageBox.confirm('确认通过该售后申请？', '提示', { type: 'info' })
    const res: any = await request.post(api.afterSaleApprove(row.id))
    if (res.code === 200) { ElMessage.success('审核通过'); fetchData() }
    else ElMessage.error(res.msg || '操作失败')
  } catch { }
}

async function refund(row: any) {
  try {
    await ElMessageBox.confirm('确认退款？退款金额: ' + row.amount, '提示', { type: 'warning' })
    const res: any = await request.post(api.afterSaleRefund(row.id))
    if (res.code === 200) { ElMessage.success('退款成功'); fetchData() }
    else ElMessage.error(res.msg || '操作失败')
  } catch { }
}

async function loadWarehouses() {
  try { const res: any = await request.get(api.warehouseList); warehouseList.value = res.data || [] } catch { }
}
function whChange(id: number) {
  const w = warehouseList.value.find(x => x.id === id)
  returnForm.warehouseName = w ? w.warehouseName : ''
}

function openReturnStock(row: any) {
  returnForm.afterSaleId = row.id
  returnForm.warehouseId = undefined
  returnForm.warehouseName = ''
  returnVisible.value = true
}

async function submitReturnStock() {
  if (!returnForm.warehouseId) return ElMessage.warning('请选择仓库')
  returnSubmitting.value = true
  try {
    const res: any = await request.post(api.afterSaleReturnStock(returnForm.afterSaleId), {
      warehouseId: returnForm.warehouseId,
      warehouseName: returnForm.warehouseName,
    })
    if (res.code === 200) { ElMessage.success('退货入库成功'); returnVisible.value = false; fetchData() }
    else ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('请求失败') }
  finally { returnSubmitting.value = false }
}

onMounted(fetchData)
</script>