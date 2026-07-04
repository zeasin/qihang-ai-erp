<template>
  <div class="page-card">
    <div style="margin-bottom:16px">
      <el-button text @click="$router.push('/order/list')">← 返回订单列表</el-button>
      <span style="font-size:16px;font-weight:600;margin-left:12px">新增订单</span>
    </div>

    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width:800px">
      <el-form-item label="订单号" prop="orderNum">
        <el-input v-model="form.orderNum" style="width:260px" />
      </el-form-item>
      <el-form-item label="下单时间">
        <el-date-picker v-model="form.orderTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
      </el-form-item>

      <el-divider>收件人信息</el-divider>
      <el-form-item label="收件人" prop="receiverName">
        <el-input v-model="form.receiverName" style="width:200px" />
      </el-form-item>
      <el-form-item label="手机号" prop="receiverMobile">
        <el-input v-model="form.receiverMobile" style="width:200px" />
      </el-form-item>
      <el-form-item label="省市区" prop="region">
        <el-cascader v-model="regionVal" :options="regionOptions" filterable clearable placeholder="搜索选择"
          style="width:300px" @change="regionChange" />
      </el-form-item>
      <el-form-item label="详细地址" prop="address">
        <el-input v-model="form.address" />
      </el-form-item>

      <el-divider>商品信息</el-divider>
      <div style="margin-bottom:12px">
        <el-button size="small" type="primary" @click="showSkuSelector = true">+ 选择商品</el-button>
      </div>
      <el-table :data="form.itemList" border size="small" style="width:100%">
        <el-table-column label="商品" min-width="200">
          <template #default="{ row }">
            <div style="display:flex;align-items:center;gap:8px">
              <el-image v-if="row.goodsImg" :src="row.goodsImg" style="width:36px;height:36px;border-radius:4px" fit="cover" />
              <div>
                <div>{{ row.goodsTitle }}</div>
                <div v-if="row.goodsSpec" style="font-size:12px;color:#999">{{ row.goodsSpec }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="SKU编码" width="120">
          <template #default="{ row }">{{ row.skuNum || '-' }}</template>
        </el-table-column>
        <el-table-column label="单价" width="90">
          <template #default="{ row }">
            <el-input-number v-model="row.goodsPrice" :precision="2" :min="0" size="small" style="width:80px" />
          </template>
        </el-table-column>
        <el-table-column label="数量" width="70">
          <template #default="{ row }">
            <el-input-number v-model="row.quantity" :min="1" size="small" style="width:60px" />
          </template>
        </el-table-column>
        <el-table-column label="小计" width="80" align="right">
          <template #default="{ row }">¥{{ ((row.goodsPrice || 0) * (row.quantity || 1)).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="50" align="center">
          <template #default="{ $index }">
            <el-button size="small" type="danger" link @click="form.itemList.splice($index,1)">×</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-divider>费用信息</el-divider>
      <el-form-item label="商品金额">
        <el-input-number v-model="form.goodsAmount" :precision="2" :min="0" style="width:200px" />
      </el-form-item>
      <el-form-item label="运费">
        <el-input-number v-model="form.postFee" :precision="2" :min="0" style="width:200px" />
      </el-form-item>
      <el-form-item label="折扣">
        <el-input-number v-model="form.discountAmount" :precision="2" :min="0" style="width:200px" />
      </el-form-item>
      <el-form-item label="实付金额">
        <el-input-number v-model="form.payment" :precision="2" :min="0" style="width:200px" />
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="2" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm" :loading="submitting" size="default">保存订单</el-button>
        <el-button @click="$router.push('/order/list')" size="default">取消</el-button>
      </el-form-item>
    </el-form>

    <el-dialog v-model="showSkuSelector" title="选择商品SKU" width="800px" top="5vh">
      <div style="display:flex;gap:12px;margin-bottom:12px">
        <el-input v-model="skuKeyword" placeholder="搜索商品名称 / SKU编码 / 商品编号" clearable style="flex:1"
          @keyup.enter="searchSku" />
        <el-button type="primary" @click="searchSku">搜索</el-button>
      </div>
      <el-table :data="skuList" border size="small" v-loading="skuLoading" max-height="420"
        @row-click="selectSku" highlight-current-row>
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
        <el-table-column label="零售价" width="80" align="right" prop="retailPrice" />
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
import { pcaTextArr } from 'element-china-area-data'
import request from '../../utils/request'
import { api } from '../../utils/api'

const router = useRouter()
const formRef = ref()
const submitting = ref(false)

const form = reactive<any>({
  orderNum: '', orderTime: '',
  receiverName: '', receiverMobile: '', province: '', city: '', town: '', address: '',
  goodsAmount: 0, postFee: 0, discountAmount: 0, payment: 0, remark: '',
  itemList: [], region: '',
})

const rules: any = {
  orderNum: [{ required: true, message: '订单号不能为空', trigger: 'blur' }],
  receiverName: [{ required: true, message: '收件人不能为空', trigger: 'blur' }],
  receiverMobile: [{ required: true, message: '手机号不能为空', trigger: 'blur' }],
  region: [{ validator: (_rule: any, _value: any, callback: Function) => {
    regionVal.value.length > 0 ? callback() : callback(new Error('请选择省市区'))
  }, trigger: 'change' }],
  address: [{ required: true, message: '详细地址不能为空', trigger: 'blur' }],
}

const showSkuSelector = ref(false)
const skuKeyword = ref('')
const skuList = ref<any[]>([])
const skuTotal = ref(0)
const skuPage = ref(1)
const skuPageSize = ref(10)
const skuLoading = ref(false)

const regionOptions = pcaTextArr
const regionVal = ref<string[]>([])

function regionChange(val: string[]) {
  if (val && val.length >= 1) form.province = val[0]
  if (val && val.length >= 2) form.city = val[1]
  if (val && val.length >= 3) form.town = val[2]
}

async function searchSku() {
  skuLoading.value = true
  try {
    const res: any = await request.get(api.goodsSkuList, {
      params: { keyword: skuKeyword.value, pageNum: skuPage.value, pageSize: skuPageSize.value }
    })
    skuList.value = res.rows || []
    skuTotal.value = res.total || 0
  } finally { skuLoading.value = false }
}

function selectSku(sku: any) {
  const spec = [sku.colorValue, sku.sizeValue, sku.styleValue].filter(Boolean).join(' / ')
  const item = {
    goodsId: sku.goodsId,
    goodsSkuId: sku.id,
    goodsTitle: sku.goodsName,
    goodsImg: sku.goodsImg,
    goodsNum: sku.goodsNum,
    goodsSpec: spec || sku.skuName,
    skuNum: sku.skuCode,
    goodsPrice: sku.retailPrice || 0,
    quantity: 1,
    remark: '',
  }
  form.itemList.push(item)
  showSkuSelector.value = false
  recalcAmount()
}

function recalcAmount() {
  form.goodsAmount = form.itemList.reduce((s: number, i: any) => s + (i.goodsPrice || 0) * (i.quantity || 1), 0)
}

async function submitForm() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    const res: any = await request.post(api.orderSave, form)
    if (res.code === 200) {
      ElMessage.success('订单创建成功')
      router.push('/order/list')
    } else {
      ElMessage.error(res.msg || '创建失败')
    }
  } finally { submitting.value = false }
}

onMounted(() => {
  if (!form.orderNum) form.orderNum = 'ORD' + Date.now()
  if (!form.orderTime) form.orderTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
})
</script>