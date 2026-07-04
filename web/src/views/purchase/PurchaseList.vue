<template>
  <div class="page-card">
    <div class="page-header">
      <h3>采购订单</h3>
      <el-button type="primary" size="small" @click="$router.push('/purchase/create')">新增采购单</el-button>
    </div>

    <el-form :model="query" inline style="margin-bottom:12px">
      <el-form-item label="采购单号">
        <el-input v-model="query.orderNum" placeholder="搜索" clearable style="width:160px" @keyup.enter="search" />
      </el-form-item>
      <el-form-item label="供应商">
        <el-input v-model="query.supplierName" placeholder="搜索" clearable style="width:160px" @keyup.enter="search" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" placeholder="全部" clearable style="width:120px">
          <el-option label="待入库" :value="0" />
          <el-option label="已完成" :value="2" />
          <el-option label="已取消" :value="11" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" stripe border size="small" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="orderNum" label="采购单号" width="160" />
      <el-table-column prop="supplierName" label="供应商" width="150" />
      <el-table-column label="商品" width="300">
        <template #default="{ row }">
          <div v-if="row.itemList && row.itemList.length" style="display:flex;flex-direction:column;gap:2px">
            <div v-for="(item, idx) in row.itemList.slice(0,2)" :key="idx" style="display:flex;align-items:center;gap:8px;padding:2px 0;font-size:13px">
              <el-image :src="item.goodsImage || '/logo.png'" style="width:32px;height:32px;border-radius:4px;flex-shrink:0" fit="cover" />
              <div style="flex:1;min-width:0;line-height:1.3">
                <div class="text-ellipsis">{{ item.goodsName }}</div>
                <div v-if="item.skuName" style="color:#999;font-size:12px">{{ item.skuName }}</div>
              </div>
              <span style="color:#666;white-space:nowrap">x{{ item.quantity }}</span>
            </div>
            <span v-if="row.itemList.length > 2" style="color:#409eff;font-size:12px;cursor:pointer" @click="showDetail(row)">...共 {{ row.itemList.length }} 项</span>
          </div>
          <span v-else style="color:#999">-</span>
        </template>
      </el-table-column>
      <el-table-column label="采购金额" width="100" align="right">
        <template #default="{ row }">{{ row.totalAmount }}</template>
      </el-table-column>
      <el-table-column label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="采购时间" width="150">
        <template #default="{ row }">{{ row.orderTime ? (row.orderTime + '').split('.').slice(0,1).join('').replace('T',' ') : '-' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="showDetail(row)">详情</el-button>
          <el-button v-if="row.status === 0" size="small" type="success" link @click="openStockIn(row)">入库</el-button>
          <el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top:12px;text-align:right">
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" :page-sizes="[10,20,50]"
        layout="total,sizes,prev,pager,next" @change="fetchData" />
    </div>

    <el-dialog v-model="detailVisible" title="采购订单详情" width="700px">
      <template v-if="detail">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="采购单号" :span="2">{{ detail.orderNum }}</el-descriptions-item>
          <el-descriptions-item label="供应商">{{ detail.supplierName }}</el-descriptions-item>
          <el-descriptions-item label="采购金额">{{ detail.totalAmount }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ statusLabel(detail.status) }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detail.remark }}</el-descriptions-item>
        </el-descriptions>
        <el-divider />
        <h4 style="margin:0 0 8px">商品明细</h4>
        <el-table :data="detail.itemList || []" border size="small">
          <el-table-column prop="goodsName" label="商品" min-width="180" />
          <el-table-column prop="skuName" label="规格" width="120" />
          <el-table-column prop="skuCode" label="编码" width="100" />
          <el-table-column prop="purchasePrice" label="采购价" width="80" align="right" />
          <el-table-column prop="quantity" label="数量" width="60" align="center" />
          <el-table-column prop="inQuantity" label="已入库" width="60" align="center" />
          <el-table-column prop="totalAmount" label="小计" width="80" align="right" />
        </el-table>
      </template>
    </el-dialog>

    <el-dialog v-model="stockInVisible" title="采购入库" width="600px" @open="loadWarehouses">
      <el-form :model="stockInForm" label-width="100px" size="small">
        <el-form-item label="仓库">
          <el-select v-model="stockInForm.warehouseId" placeholder="选择仓库" style="width:200px" @change="whChange">
            <el-option v-for="w in warehouseList" :key="w.id" :label="w.warehouseName" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="入库商品">
          <el-table :data="stockInItems" border size="small" max-height="300">
            <el-table-column label="商品" min-width="160">
              <template #default="{ row }">
                <div style="display:flex;align-items:center;gap:6px">
                  <el-image v-if="row.goodsImage" :src="row.goodsImage" style="width:28px;height:28px;border-radius:3px" fit="cover" />
                  <div>
                    <div>{{ row.goodsName }}</div>
                    <div v-if="row.skuName" style="font-size:12px;color:#999">{{ row.skuName }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="skuCode" label="编码" width="100" />
            <el-table-column label="采购数量" width="60" align="center" prop="quantity" />
            <el-table-column label="已入库" width="60" align="center" prop="inQuantity" />
            <el-table-column label="本次入库" width="100">
              <template #default="{ row }">
                <el-input-number v-model="row._inQty" :min="0" :max="(row.quantity || 0) - (row.inQuantity || 0)" size="small" style="width:80px" />
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button size="small" @click="stockInVisible = false">取消</el-button>
        <el-button size="small" type="primary" @click="submitStockIn" :loading="stockInSubmitting">确认入库</el-button>
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

const query = reactive({ orderNum: '', supplierName: '', status: undefined as any })

const detailVisible = ref(false)
const detail = ref<any>(null)

const stockInVisible = ref(false)
const stockInSubmitting = ref(false)
const stockInForm = reactive({ purchaseOrderId: 0, warehouseId: undefined as any, warehouseName: '' })
const stockInItems = ref<any[]>([])
const warehouseList = ref<any[]>([])

async function loadWarehouses() {
  try { const res: any = await request.get(api.warehouseList); warehouseList.value = res.data || [] } catch { }
}
function whChange(id: number) {
  const w = warehouseList.value.find(x => x.id === id)
  stockInForm.warehouseName = w ? w.warehouseName : ''
}

function statusType(s: number) {
  const map: Record<number, string> = { 0: 'warning', 2: 'success', 11: 'info' }
  return map[s] || 'info'
}
function statusLabel(s: number) {
  const map: Record<number, string> = { 0: '待入库', 2: '已完成', 11: '已取消' }
  return map[s] || '未知'
}

function search() { pageNum.value = 1; fetchData() }
function resetQuery() { Object.assign(query, { orderNum: '', supplierName: '', status: undefined }); search() }

async function fetchData() {
  loading.value = true
  try {
    const res: any = await request.get(api.purchaseList, { params: { pageNum: pageNum.value, pageSize: pageSize.value, ...query } })
    list.value = res.rows || []
    total.value = res.total || 0
  } finally { loading.value = false }
}

async function showDetail(row: any) {
  const res: any = await request.get(api.purchaseGet(row.id))
  detail.value = res.data
  detailVisible.value = true
}

function openStockIn(row: any) {
  stockInForm.purchaseOrderId = row.id
  stockInForm.warehouseId = undefined
  stockInForm.warehouseName = ''
  stockInItems.value = (row.itemList || []).map((i: any) => ({
    ...i, _inQty: (i.quantity || 0) - (i.inQuantity || 0),
  }))
  stockInVisible.value = true
}

async function submitStockIn() {
  if (!stockInForm.warehouseId) return ElMessage.warning('请选择仓库')
  const submitItems = stockInItems.value.filter((i: any) => (i._inQty || 0) > 0)
  if (!submitItems.length) return ElMessage.warning('没有可入库的商品')
  
  stockInSubmitting.value = true
  try {
    const res: any = await request.post(api.purchaseStockIn(stockInForm.purchaseOrderId), {
      warehouseId: stockInForm.warehouseId,
      warehouseName: stockInForm.warehouseName,
    })
    if (res.code === 200) {
      ElMessage.success('入库成功')
      stockInVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.msg || '入库失败')
    }
  } catch { ElMessage.error('请求失败') }
  finally { stockInSubmitting.value = false }
}

async function deleteRow(row: any) {
  try {
    await ElMessageBox.confirm('确认删除该采购订单？', '提示', { type: 'warning' })
    const res: any = await request.delete(api.purchaseDelete(row.id))
    if (res.code === 200) { ElMessage.success('删除成功'); fetchData() }
    else ElMessage.error(res.msg || '删除失败')
  } catch { }
}

onMounted(fetchData)
</script>