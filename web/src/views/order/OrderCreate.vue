<template>
  <div class="page-card">
    <div style="margin-bottom:16px">
      <el-button text @click="$router.push('/order/list')">← 返回订单列表</el-button>
      <span style="font-size:16px;font-weight:600;margin-left:12px">新增订单</span>
    </div>

    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width:800px">
      <el-form-item label="订单号" prop="orderNum">
        <el-input v-model="form.orderNum" placeholder="自动生成或手动输入" style="width:260px" />
      </el-form-item>
      <el-form-item label="下单时间">
        <el-date-picker v-model="form.orderTime" type="datetime" placeholder="选择时间" value-format="YYYY-MM-DD HH:mm:ss" />
      </el-form-item>

      <el-divider>收件人信息</el-divider>
      <el-form-item label="收件人" prop="receiverName">
        <el-input v-model="form.receiverName" placeholder="收件人姓名" style="width:200px" />
      </el-form-item>
      <el-form-item label="手机号" prop="receiverMobile">
        <el-input v-model="form.receiverMobile" placeholder="手机号" style="width:200px" />
      </el-form-item>
      <el-form-item label="收货地址">
        <el-input v-model="form.province" placeholder="省" style="width:120px;margin-right:8px" />
        <el-input v-model="form.city" placeholder="市" style="width:120px;margin-right:8px" />
        <el-input v-model="form.town" placeholder="区" style="width:120px" />
      </el-form-item>
      <el-form-item label="详细地址">
        <el-input v-model="form.address" placeholder="详细地址" />
      </el-form-item>

      <el-divider>商品信息</el-divider>
      <div style="margin-bottom:12px">
        <el-button size="small" type="primary" @click="addItem">+ 添加商品</el-button>
      </div>
      <el-table :data="form.itemList" border size="small" style="width:100%">
        <el-table-column label="商品" min-width="180">
          <template #default="{ row, $index }">
            <el-select v-model="row.goodsId" filterable remote :remote-method="(q) => searchGoods(q, $index)"
              placeholder="搜索商品" style="width:100%" @change="(v) => goodsChanged(v, $index)">
              <el-option v-for="g in goodsOptions" :key="g.id" :label="g.name" :value="g.id" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="规格" width="120">
          <template #default="{ row }">
            <el-select v-model="row.goodsSkuId" placeholder="规格" style="width:100%" @change="skuChanged($index)">
              <el-option v-for="s in row.skuOptions || []" :key="s.id" :label="s.skuName || s.skuCode" :value="s.id" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="单价" width="80">
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
const goodsOptions = ref<any[]>([])

const form = reactive<any>({
  orderNum: '', orderTime: '',
  receiverName: '', receiverMobile: '', province: '', city: '', town: '', address: '',
  goodsAmount: 0, postFee: 0, discountAmount: 0, payment: 0, remark: '',
  itemList: [],
})

const rules: any = {
  orderNum: [{ required: true, message: '订单号不能为空', trigger: 'blur' }],
  receiverName: [{ required: true, message: '收件人不能为空', trigger: 'blur' }],
  receiverMobile: [{ required: true, message: '手机号不能为空', trigger: 'blur' }],
}

let searchTimer: any = null
function searchGoods(query: string, index: number) {
  clearTimeout(searchTimer)
  if (!query) return
  searchTimer = setTimeout(async () => {
    const res: any = await request.get(api.goodsList, { params: { name: query, pageSize: 20 } })
    goodsOptions.value = res.rows || []
  }, 300)
}

async function goodsChanged(goodsId: number, index: number) {
  const item = form.itemList[index]
  const goods = goodsOptions.value.find((g: any) => g.id === goodsId)
  if (goods) {
    item.goodsTitle = goods.name
    item.goodsNum = goods.goodsNum
    item.goodsImg = goods.image
    item.goodsPrice = goods.retailPrice || 0
    // load SKUs
    const skuRes: any = await request.get(api.goodsList, { params: { pageSize: 200 } })
    const allGoods = skuRes.rows || []
    // just set the goods name for now
  }
  // try to load SKUs via the goods detail
  try {
    const res: any = await request.get(api.goodsGet(goodsId))
    const goodsDetail = res.data
    if (goodsDetail) {
      item.goodsTitle = goodsDetail.name
      item.goodsNum = goodsDetail.goodsNum
      item.goodsImg = goodsDetail.image
      item.goodsPrice = goodsDetail.retailPrice || 0
    }
  } catch { /* ignore */ }
  // load SKU options from a separate endpoint - actually we need SKU list by goods
  try {
    // Use the goods list to find SKUs - for now let's just set a placeholder
    // The goods detail should include SKUs
  } catch { /* ignore */ }
}

function skuChanged(index: number) {
  // SKU selected, update price
}

function addItem() {
  form.itemList.push({
    goodsId: null, goodsSkuId: null, goodsTitle: '', goodsImg: '', goodsNum: '',
    goodsSpec: '', skuNum: '', goodsPrice: 0, quantity: 1, remark: '',
    skuOptions: [],
  })
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
  if (!form.orderNum) {
    form.orderNum = 'ORD' + Date.now()
  }
})
</script>