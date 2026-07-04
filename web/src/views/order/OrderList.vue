<template>
  <div class="page-card">
    <div class="page-header">
      <h3>📋 订单列表</h3>
      <el-button type="primary" size="small" @click="$router.push('/order/create')">新增订单</el-button>
    </div>

    <el-form :model="query" inline style="margin-bottom:12px">
      <el-form-item label="订单号">
        <el-input v-model="query.orderNum" placeholder="搜索" clearable style="width:160px" @keyup.enter="search" />
      </el-form-item>
      <el-form-item label="收件人">
        <el-input v-model="query.receiverName" placeholder="搜索" clearable style="width:120px" @keyup.enter="search" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.orderStatus" placeholder="全部" clearable style="width:120px">
          <el-option label="待付款" :value="0" />
          <el-option label="待发货" :value="1" />
          <el-option label="已发货" :value="2" />
          <el-option label="已完成" :value="3" />
          <el-option label="已取消" :value="11" />
        </el-select>
      </el-form-item>
      <el-form-item label="时间">
        <el-date-picker v-model="timeRange" type="datetimerange" range-separator="~" start-placeholder="开始" end-placeholder="结束"
          value-format="YYYY-MM-DD HH:mm:ss" style="width:340px" @change="timeRangeChange" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" stripe border size="small" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="orderNum" label="订单号" width="160" />
      <el-table-column label="商品" width="380">
        <template #default="{ row }">
          <div v-if="row.itemList && row.itemList.length" style="display:flex;flex-direction:column;gap:2px">
            <div v-for="(item, idx) in row.itemList.slice(0,2)" :key="idx" style="display:flex;align-items:flex-start;gap:8px;padding:4px 0;font-size:13px">
              <el-image :src="item.goodsImg || '/logo.png'" style="width:36px;height:36px;border-radius:4px;flex-shrink:0" fit="cover">
                <template #error>
                  <div style="width:36px;height:36px;background:#f5f5f5;display:flex;align-items:center;justify-content:center;font-size:12px;color:#ccc">图</div>
                </template>
              </el-image>
              <div style="flex:1;min-width:0;line-height:1.4">
                <div class="text-ellipsis" style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ item.goodsTitle }}</div>
                <div v-if="item.goodsSpec" style="color:#999;font-size:12px">{{ item.goodsSpec }}</div>
              </div>
              <span style="color:#666;white-space:nowrap">x{{ item.quantity }}</span>
            </div>
            <span v-if="row.itemList.length > 2" style="color:#409eff;font-size:12px;cursor:pointer" @click="showDetail(row)">...共 {{ row.itemList.length }} 件商品，点击查看</span>
          </div>
          <span v-else style="color:#999">-</span>
        </template>
      </el-table-column>
      <el-table-column label="商品金额" width="100" align="right">
        <template #default="{ row }">{{ row.goodsAmount }}</template>
      </el-table-column>
      <el-table-column label="实付金额" width="100" align="right">
        <template #default="{ row }">{{ row.payment }}</template>
      </el-table-column>
      <el-table-column prop="receiverName" label="收件人" width="100" />
      <el-table-column prop="receiverMobile" label="手机号" width="120" />
      <el-table-column label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="statusType(row.orderStatus)" size="small">{{ statusLabel(row.orderStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" width="160">
          <template #default="{ row }">{{ row.orderTime ? (row.orderTime + '').split('.').slice(0,1).join('').replace('T',' ') : '-' }}</template>
        </el-table-column>
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="showDetail(row)">详情</el-button>
          <el-button v-if="row.orderStatus === 0" size="small" type="success" link @click="confirmPay(row)">付款确认</el-button>
          <el-button v-if="row.orderStatus === 1" size="small" type="success" link @click="openDelivery(row)">发货</el-button>
          <el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top:12px;text-align:right">
      <el-pagination
        v-model:current-page="pageNum" v-model:page-size="pageSize"
        :total="total" :page-sizes="[10,20,50]"
        layout="total,sizes,prev,pager,next" @change="fetchData"
      />
    </div>

    <el-dialog v-model="detailVisible" title="订单详情" width="700px">
      <template v-if="detail">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="订单号" :span="2">{{ detail.orderNum }}</el-descriptions-item>
          <el-descriptions-item label="商品金额">{{ detail.goodsAmount }}</el-descriptions-item>
          <el-descriptions-item label="实付金额">{{ detail.payment }}</el-descriptions-item>
          <el-descriptions-item label="运费">{{ detail.postFee }}</el-descriptions-item>
          <el-descriptions-item label="折扣">{{ detail.discountAmount }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ statusLabel(detail.orderStatus) }}</el-descriptions-item>
          <el-descriptions-item label="收件人">{{ detail.receiverName }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ detail.receiverMobile }}</el-descriptions-item>
          <el-descriptions-item label="收货地址" :span="2">{{ detail.province }}{{ detail.city }}{{ detail.town }}{{ detail.address }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detail.remark }}</el-descriptions-item>
        </el-descriptions>
        <el-divider />
        <h4 style="margin:0 0 8px">商品明细</h4>
        <el-table :data="detail.itemList || []" border size="small">
          <el-table-column prop="goodsTitle" label="商品" min-width="180" />
          <el-table-column prop="goodsSpec" label="规格" width="120" />
          <el-table-column prop="skuNum" label="编码" width="100" />
          <el-table-column prop="goodsPrice" label="单价" width="80" align="right" />
          <el-table-column prop="quantity" label="数量" width="60" align="center" />
          <el-table-column prop="itemAmount" label="小计" width="80" align="right" />
        </el-table>
      </template>
    </el-dialog>

    <el-dialog v-model="deliveryVisible" title="发货" width="600px" @open="loadWarehouses">
      <el-form :model="deliveryForm" label-width="100px" size="small">
        <el-form-item label="仓库">
          <el-select v-model="deliveryForm.warehouseId" placeholder="选择仓库" style="width:200px" @change="whChange">
            <el-option v-for="w in warehouseList" :key="w.id" :label="w.warehouseName" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="物流公司">
          <el-input v-model="deliveryForm.logisticsCompany" placeholder="输入物流公司" style="width:200px" />
        </el-form-item>
        <el-form-item label="物流单号">
          <el-input v-model="deliveryForm.logisticsNo" placeholder="输入物流单号" style="width:200px" />
        </el-form-item>
        <el-form-item label="发货商品">
          <el-table :data="deliveryItems" border size="small" max-height="300">
            <el-table-column label="商品" min-width="160">
              <template #default="{ row }">
                <div style="display:flex;align-items:center;gap:6px">
                  <el-image v-if="row.goodsImg" :src="row.goodsImg" style="width:28px;height:28px;border-radius:3px" fit="cover">
                    <template #error><div style="width:28px;height:28px;background:#f5f5f5;display:flex;align-items:center;justify-content:center;font-size:10px;color:#ccc">图</div></template>
                  </el-image>
                  <div>
                    <div>{{ row.goodsTitle }}</div>
                    <div v-if="row.goodsSpec" style="font-size:12px;color:#999">{{ row.goodsSpec }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="skuNum" label="编码" width="100" />
            <el-table-column label="库存" width="70" align="center">
              <template #default="{ row }">
                <span :style="{ color: row._stockQty < row.quantity ? 'red' : '#67c23a' }">{{ row._stockQty ?? '-' }}</span>
              </template>
            </el-table-column>
            <el-table-column label="发货数量" width="100">
              <template #default="{ row }">
                <el-input-number v-model="row.quantity" :min="1" :max="row._maxQty" size="small" style="width:80px" />
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="deliveryForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button size="small" @click="deliveryVisible = false">取消</el-button>
        <el-button size="small" type="primary" @click="submitDelivery" :loading="deliverySubmitting">确认发货</el-button>
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

const query = reactive({ orderNum: '', receiverName: '', orderStatus: undefined as any, startTime: '', endTime: '' })

const detailVisible = ref(false)
const detail = ref<any>(null)
const timeRange = ref<any>(null)

const deliveryVisible = ref(false)
const deliverySubmitting = ref(false)
const deliveryForm = reactive({ orderId: 0, orderNum: '', warehouseId: undefined as any, warehouseName: '',
  logisticsCompany: '', logisticsNo: '', remark: '',
  receiverName: '', receiverMobile: '', receiverAddress: '' })
const deliveryItems = ref<any[]>([])
const warehouseList = ref<any[]>([])

async function loadWarehouses() {
  try {
    const res: any = await request.get(api.warehouseList)
    warehouseList.value = res.data || []
  } catch { /* ignore */ }
}
function whChange(id: number) {
  const w = warehouseList.value.find(x => x.id === id)
  deliveryForm.warehouseName = w ? w.warehouseName : ''
  if (!id) return
  deliveryItems.value.forEach((item: any) => {
    item._stockQty = 0
    item._maxQty = item.quantity
    request.get(api.deliveryStockCheck, { params: { skuId: item.goodsSkuId } }).then((res: any) => {
      const stock = res.data
      if (stock && stock.quantity !== undefined) {
        item._stockQty = stock.quantity
        item._maxQty = Math.min(item.quantity, stock.quantity)
      }
    }).catch(() => {})
  })
}
function openDelivery(row: any) {
  deliveryForm.orderId = row.id
  deliveryForm.orderNum = row.orderNum
  deliveryForm.warehouseId = undefined
  deliveryForm.warehouseName = ''
  deliveryForm.logisticsCompany = ''
  deliveryForm.logisticsNo = ''
  deliveryForm.remark = ''
  deliveryForm.receiverName = row.receiverName || ''
  deliveryForm.receiverMobile = row.receiverMobile || ''
  deliveryForm.receiverAddress = [row.province, row.city, row.town, row.address].filter(Boolean).join('')
  deliveryItems.value = (row.itemList || []).map((i: any) => ({
    orderItemId: i.id,
    goodsId: i.goodsId,
    goodsSkuId: i.goodsSkuId,
    goodsTitle: i.goodsTitle,
    goodsImg: i.goodsImg,
    goodsSpec: i.goodsSpec,
    skuNum: i.skuNum,
    quantity: i.quantity,
    _maxQty: i.quantity,
  }))
  deliveryVisible.value = true
}
async function submitDelivery() {
  if (!deliveryForm.warehouseId) return ElMessage.warning('请选择仓库')
  if (!deliveryForm.logisticsCompany) return ElMessage.warning('请输入物流公司')
  if (!deliveryForm.logisticsNo) return ElMessage.warning('请输入物流单号')
  if (!deliveryItems.value.length) return ElMessage.warning('没有可发货的商品')

  deliverySubmitting.value = true
  try {
    const body = {
      orderId: deliveryForm.orderId, orderNum: deliveryForm.orderNum,
      warehouseId: deliveryForm.warehouseId, warehouseName: deliveryForm.warehouseName,
      logisticsCompany: deliveryForm.logisticsCompany, logisticsNo: deliveryForm.logisticsNo,
      receiverName: deliveryForm.receiverName, receiverMobile: deliveryForm.receiverMobile,
      receiverAddress: deliveryForm.receiverAddress, remark: deliveryForm.remark,
      itemList: deliveryItems.value.map((i: any) => ({
        orderItemId: i.orderItemId, goodsId: i.goodsId, goodsSkuId: i.goodsSkuId,
        goodsTitle: i.goodsTitle, goodsImg: i.goodsImg, goodsSpec: i.goodsSpec,
        skuNum: i.skuNum, quantity: i.quantity,
      })),
    }
    const res: any = await request.post(api.deliveryCreate, body)
    if (res.code === 200) {
      ElMessage.success('发货成功')
      deliveryVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.msg || '发货失败')
    }
  } catch { ElMessage.error('请求失败') }
  finally { deliverySubmitting.value = false }
}

function timeRangeChange(val: string[] | null) {
  query.startTime = val ? val[0] : ''
  query.endTime = val ? val[1] : ''
}

function search() { pageNum.value = 1; fetchData() }
function resetQuery() {
  Object.assign(query, { orderNum: '', receiverName: '', orderStatus: undefined, startTime: '', endTime: '' })
  timeRange.value = null
  search()
}

function statusType(s: number) {
  const map: Record<number, string> = { 0: 'warning', 1: 'danger', 2: 'primary', 3: 'success', 11: 'info' }
  return map[s] || 'info'
}
function statusLabel(s: number) {
  const map: Record<number, string> = { 0: '待付款', 1: '待发货', 2: '已发货', 3: '已完成', 11: '已取消' }
  return map[s] || '未知'
}

async function fetchData() {
  loading.value = true
  try {
    const res: any = await request.get(api.orderList, {
      params: { pageNum: pageNum.value, pageSize: pageSize.value, ...query }
    })
    list.value = res.rows || []
    total.value = res.total || 0
  } finally { loading.value = false }
}

async function showDetail(row: any) {
  const res: any = await request.get(api.orderGet(row.id))
  detail.value = res.data
  detailVisible.value = true
}

async function confirmPay(row: any) {
  try {
    await ElMessageBox.confirm('确认该订单已付款？', '提示', { type: 'info' })
    const res: any = await request.post(api.orderPay(row.id))
    if (res.code === 200) { ElMessage.success('付款确认成功'); fetchData() }
    else ElMessage.error(res.msg || '操作失败')
  } catch { /* cancelled */ }
}

async function deleteRow(row: any) {
  try {
    await ElMessageBox.confirm('确认删除该订单？', '提示', { type: 'warning' })
    const res: any = await request.delete(api.orderDelete(row.id))
    if (res.code === 200) { ElMessage.success('删除成功'); fetchData() }
    else ElMessage.error(res.msg || '删除失败')
  } catch { /* cancelled */ }
}

onMounted(fetchData)
</script>