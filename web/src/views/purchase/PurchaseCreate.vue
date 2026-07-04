<template>
  <div class="page-card">
    <div style="margin-bottom:16px">
      <el-button text @click="$router.push('/purchase/list')">← 返回采购订单列表</el-button>
      <span style="font-size:16px;font-weight:600;margin-left:12px">新增采购订单</span>
    </div>

    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width:800px">
      <el-form-item label="采购日期">
        <el-date-picker v-model="form.orderTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
      </el-form-item>
      <el-form-item label="供应商" prop="supplierName">
        <el-select v-model="form.supplierId" placeholder="选择供应商" filterable clearable style="width:260px" @change="supplierChange">
          <el-option v-for="s in supplierList" :key="s.id" :label="s.name" :value="s.id" />
        </el-select>
      </el-form-item>

      <el-divider>商品信息</el-divider>
      <div style="margin-bottom:12px">
        <el-button size="small" type="primary" @click="showSkuSelector = true">+ 选择商品</el-button>
      </div>
      <el-table :data="form.itemList" border size="small" style="width:100%">
        <el-table-column label="商品" min-width="200">
          <template #default="{ row }">
            <div style="display:flex;align-items:center;gap:8px">
              <el-image v-if="row.goodsImage" :src="row.goodsImage" style="width:36px;height:36px;border-radius:4px" fit="cover" />
              <div>
                <div>{{ row.goodsName }}</div>
                <div v-if="row.skuName" style="font-size:12px;color:#999">{{ row.skuName }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="SKU编码" width="120">
          <template #default="{ row }">{{ row.skuCode || '-' }}</template>
        </el-table-column>
        <el-table-column label="采购价" width="140">
          <template #default="{ row }">
            <el-input-number v-model="row.purchasePrice" :precision="2" :min="0" size="small" style="width:120px" @change="recalcAmount" />
          </template>
        </el-table-column>
        <el-table-column label="数量" width="100">
          <template #default="{ row }">
            <el-input-number v-model="row.quantity" :min="1" size="small" style="width:80px" @change="recalcAmount" />
          </template>
        </el-table-column>
        <el-table-column label="小计" width="80" align="right">
          <template #default="{ row }">¥{{ ((row.purchasePrice || 0) * (row.quantity || 1)).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="50" align="center">
          <template #default="{ $index }">
            <el-button size="small" type="danger" link @click="removeItem($index)">×</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-divider>费用信息</el-divider>
      <el-form-item label="采购金额">
        <el-input :value="form.totalAmount.toFixed(2)" disabled style="width:200px" />
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="2" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm" :loading="submitting" size="default">保存采购单</el-button>
        <el-button @click="$router.push('/purchase/list')" size="default">取消</el-button>
      </el-form-item>
    </el-form>

    <el-dialog v-model="showSkuSelector" title="选择商品SKU" width="800px" top="5vh">
      <div style="display:flex;gap:12px;margin-bottom:12px">
        <el-input v-model="skuKeyword" placeholder="搜索商品名称 / SKU编码 / 商品编号" clearable style="flex:1" @keyup.enter="searchSku" />
        <el-button type="primary" @click="searchSku">搜索</el-button>
      </div>
      <el-table :data="skuList" border size="small" v-loading="skuLoading" max-height="420" @row-click="selectSku" highlight-current-row>
        <el-table-column label="商品" min-width="180">
          <template #default="{ row }">
            <div style="display:flex;align-items:center;gap:8px">
              <el-image v-if="row.goodsImg" :src="row.goodsImg" style="width:36px;height:36px;border-radius:4px" fit="cover" />
              <span>{{ row.goodsName }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="规格" width="120">
          <template #default="{ row }">
            <div>{{ row.skuName || '-' }}</div>
            <div style="font-size:12px;color:#999">
              <span v-if="row.colorValue">{{ row.colorValue }}</span>
              <span v-if="row.sizeValue"> / {{ row.sizeValue }}</span>
              <span v-if="row.styleValue"> / {{ row.styleValue }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="SKU编码" width="120" prop="skuCode" />
        <el-table-column label="操作" width="60" align="center">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click.stop="selectSku(row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align:right;margin-top:8px">
        <el-pagination v-model:current-page="skuPage" v-model:page-size="skuPageSize" :total="skuTotal"
          :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" small @change="searchSku" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../../utils/request'
import { api } from '../../utils/api'

const router = useRouter()
const formRef = ref()
const submitting = ref(false)

const form = reactive<any>({
  orderTime: '', supplierId: null, supplierName: '', totalAmount: 0, remark: '', itemList: [],
})

const rules: any = {}

const supplierList = ref<any[]>([])

const showSkuSelector = ref(false)
const skuKeyword = ref('')
const skuList = ref<any[]>([])
const skuTotal = ref(0)
const skuPage = ref(1)
const skuPageSize = ref(10)
const skuLoading = ref(false)

async function loadSuppliers() {
  try {
    const res: any = await request.get(api.supplierList)
    supplierList.value = res.data || []
  } catch { }
}

function supplierChange(val: number) {
  const s = supplierList.value.find(x => x.id === val)
  form.supplierName = s ? s.name : ''
}

async function searchSku() {
  skuLoading.value = true
  try {
    const res: any = await request.get(api.goodsSkuList, { params: { keyword: skuKeyword.value, pageNum: skuPage.value, pageSize: skuPageSize.value } })
    skuList.value = res.rows || []
    skuTotal.value = res.total || 0
  } finally { skuLoading.value = false }
}

function selectSku(sku: any) {
  const spec = [sku.colorValue, sku.sizeValue, sku.styleValue].filter(Boolean).join(' / ')
  const item = {
    goodsId: sku.goodsId,
    skuId: sku.id,
    goodsName: sku.goodsName,
    goodsImage: sku.goodsImg,
    goodsNum: sku.goodsNum,
    skuCode: sku.skuCode,
    skuName: spec || sku.skuName,
    quantity: 1,
    purchasePrice: 0,
    remark: '',
  }
  form.itemList.push(item)
  showSkuSelector.value = false
  recalcAmount()
}

function removeItem(index: number) {
  form.itemList.splice(index, 1)
  recalcAmount()
}

function recalcAmount() {
  form.totalAmount = form.itemList.reduce((s: number, i: any) => s + (i.purchasePrice || 0) * (i.quantity || 1), 0)
}

async function submitForm() {
  if (!form.itemList.length) return ElMessage.warning('请至少选择一件商品')
  submitting.value = true
  try {
    const res: any = await request.post(api.purchaseSave, form)
    if (res.code === 200) {
      ElMessage.success('采购单创建成功')
      router.push('/purchase/list')
    } else {
      ElMessage.error(res.msg || '创建失败')
    }
  } finally { submitting.value = false }
}

onMounted(() => {
  if (!form.orderTime) form.orderTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
  loadSuppliers()
})
</script>