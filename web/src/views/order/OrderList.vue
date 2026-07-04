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
      <el-table-column label="操作" width="160" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="showDetail(row)">详情</el-button>
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