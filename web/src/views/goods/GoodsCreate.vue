<template>
  <div class="page-card">
    <div style="margin-bottom:16px">
      <el-button text @click="$router.push('/goods/list')">← 返回商品库</el-button>
      <span style="font-size:16px;font-weight:600;margin-left:12px">新增商品</span>
    </div>

    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width:800px">
      <el-form-item label="商品分类" prop="categoryId">
        <el-tree-select v-model="form.categoryId" :data="categoryTree" :props="treeProps"
          placeholder="请选择分类" check-strictly clearable style="width:300px" />
      </el-form-item>
      <el-form-item label="商品名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入商品名称" />
      </el-form-item>
      <el-form-item label="商品编号" prop="goodsNum">
        <el-input v-model="form.goodsNum" placeholder="请输入商品编号" style="width:220px" />
      </el-form-item>
      <el-form-item label="品牌">
        <el-select v-model="form.brandId" placeholder="请选择品牌" clearable style="width:220px">
          <el-option v-for="b in brands" :key="b.id" :label="b.name" :value="b.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="零售价" prop="retailPrice">
        <el-input-number v-model="form.retailPrice" :precision="2" :min="0" style="width:220px" />
      </el-form-item>
      <el-form-item label="单位">
        <el-input v-model="form.unitName" placeholder="如：件、个" style="width:120px" />
      </el-form-item>
      <el-form-item label="商品图片">
        <el-input v-model="form.image" placeholder="图片URL" style="width:400px" />
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="2" />
      </el-form-item>

      <el-divider />

      <el-form-item label="商品规格">
        <div style="width:100%">
          <div style="display:flex;gap:12px;margin-bottom:8px;align-items:flex-start" v-for="(spec, i) in specGroups" :key="i">
            <div style="width:120px">
              <div style="font-size:12px;color:#999;margin-bottom:4px">规格{{ i+1 }}名称</div>
              <el-input v-model="spec.label" size="small" :placeholder="['颜色','尺码','款式'][i]" />
            </div>
            <div style="width:220px">
              <div style="font-size:12px;color:#999;margin-bottom:4px">规格值（回车添加）</div>
              <div class="spec-tag-box">
                <el-tag v-for="(v,vi) in spec.values" :key="vi" closable size="small" @close="removeSpecValue(i,vi)">{{ v }}</el-tag>
                <el-input ref="specInputs" v-model="spec.inputVal" size="small"
                  :placeholder="spec.values.length===0?'输入值后回车':''"
                  class="spec-tag-input"
                  @keyup.enter="addSpecValue(i)" />
              </div>
            </div>
            <el-button v-if="specGroups.length > 1" type="danger" link @click="removeSpecGroup(i)" style="margin-top:18px">移除</el-button>
          </div>
          <el-button size="small" @click="addSpecGroup" v-if="specGroups.length < 3">+ 添加{{ specLabels[specGroups.length] }}</el-button>
        </div>
      </el-form-item>

      <el-form-item label="SKU列表" v-if="skuList.length > 0">
        <el-table :data="skuList" border size="small" style="width:100%">
          <el-table-column label="规格" min-width="120">
            <template #default="{ row }">
              <el-input v-model="row.skuName" size="small" placeholder="规格名" />
            </template>
          </el-table-column>
          <el-table-column label="规格编码" min-width="120">
            <template #default="{ row }">
              <el-input v-model="row.skuCode" size="small" placeholder="编码" />
            </template>
          </el-table-column>
          <el-table-column label="颜色" width="80">
            <template #default="{ row }"><el-input v-model="row.colorValue" size="small" /></template>
          </el-table-column>
          <el-table-column label="尺码" width="80">
            <template #default="{ row }"><el-input v-model="row.sizeValue" size="small" /></template>
          </el-table-column>
          <el-table-column label="款式" width="80">
            <template #default="{ row }"><el-input v-model="row.styleValue" size="small" /></template>
          </el-table-column>
          <el-table-column label="零售价" width="100">
            <template #default="{ row }">
              <el-input-number v-model="row.retailPrice" :precision="2" :min="0" size="small" style="width:90px" />
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm" :loading="submitting" size="default">保存商品</el-button>
        <el-button @click="$router.push('/goods/list')" size="default">取消</el-button>
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
const categories = ref<any[]>([])
const brands = ref<any[]>([])

const form = reactive<any>({
  categoryId: null, name: '', image: '', goodsNum: '', unitName: '',
  brandId: null, retailPrice: 0, remark: '', barCode: ''
})

const rules: any = {
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  name: [{ required: true, message: '商品名称不能为空', trigger: 'blur' }],
  goodsNum: [{ required: true, message: '商品编号不能为空', trigger: 'blur' }],
}

const treeProps = { value: 'id', label: 'name', children: 'children' }
const categoryTree = ref<any[]>([])

function buildTree(list: any[]): any[] {
  const map = new Map(list.map((i: any) => [i.id, { ...i, children: [] }]))
  const roots: any[] = []
  list.forEach((i: any) => {
    if (i.parentId && map.has(i.parentId)) map.get(i.parentId)!.children.push(map.get(i.id))
    else if (!i.parentId || i.parentId === 0) roots.push(map.get(i.id))
  })
  return roots
}

const specInputs = ref<any[]>([])
const specLabels = ['颜色', '尺码', '款式']
const specGroups = ref<any[]>([])

function addSpecGroup() {
  const idx = specGroups.value.length
  if (idx >= 3) return
  specGroups.value.push({ label: specLabels[idx], values: [], inputVal: '' })
}

function removeSpecGroup(index: number) {
  specGroups.value.splice(index, 1)
  onSpecChange()
}

interface SkuRow {
  skuName: string
  skuCode: string
  colorValue: string
  sizeValue: string
  styleValue: string
  retailPrice: number
}

const skuList = ref<SkuRow[]>([])

function focusInput(index: number) {
  setTimeout(() => specInputs.value[index]?.focus(), 50)
}

function addSpecValue(index: number) {
  const spec = specGroups.value[index]
  const val = spec.inputVal?.trim()
  if (!val) return
  if (!spec.values.includes(val)) {
    spec.values.push(val)
  }
  spec.inputVal = ''
  onSpecChange()
}

function removeSpecValue(specIndex: number, valueIndex: number) {
  specGroups.value[specIndex].values.splice(valueIndex, 1)
  onSpecChange()
}

function cartesianProduct(groups: any[][]): any[][] {
  if (groups.length === 0) return [[]]
  const [first, ...rest] = groups
  const restProduct = cartesianProduct(rest)
  const result: any[][] = []
  for (const f of first) {
    for (const rp of restProduct) {
      result.push([f, ...rp])
    }
  }
  return result
}

function onSpecChange() {
  const groups = specGroups.value.filter(g => g.values.length > 0).map(g => g.values)
  if (groups.length === 0) { skuList.value = []; return }

  const product = cartesianProduct(groups)
  const num = form.goodsNum || ''
  skuList.value = product.map((combo, idx) => {
    const seq = String(idx + 1).padStart(2, '0')
    return {
      skuName: combo.join(' '),
      skuCode: num + seq,
      colorValue: specGroups.value[0]?.values.includes(combo[0]) ? combo[0] : '',
      sizeValue: specGroups.value[1] ? combo[1] || '' : '',
      styleValue: specGroups.value[2] ? combo[2] || '' : '',
      retailPrice: form.retailPrice || 0,
    }
  })
}

async function loadOptions() {
  try {
    const [catRes, brandRes] = await Promise.all([
      request.get(api.categoryList),
      request.get(api.brandList),
    ])
    categories.value = catRes.data || []
    brands.value = brandRes.data || []
    categoryTree.value = buildTree(categories.value)
  } catch { /* ignore */ }
}

async function submitForm() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    const body: any = {
      name: form.name,
      image: form.image,
      goodsNum: form.goodsNum,
      unitName: form.unitName,
      categoryId: form.categoryId,
      brandId: form.brandId,
      remark: form.remark,
      retailPrice: form.retailPrice,
      skuList: skuList.value,
    }
    const res: any = await request.post(api.goodsAdd, body)
    if (res.code === 200) {
      ElMessage.success('商品添加成功')
      router.push('/goods/list')
    } else {
      ElMessage.error(res.msg || '添加失败')
    }
  } finally { submitting.value = false }
}

onMounted(() => { loadOptions() })
</script>

<style scoped>
.spec-tag-box {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 5px 8px;
  min-height: 36px;
  align-items: center;
  background: #fff;
}
.spec-tag-box:focus-within {
  border-color: #409eff;
}
.spec-tag-input {
  width: auto;
  min-width: 60px;
  flex: 1;
}
.spec-tag-input .el-input__wrapper {
  box-shadow: none !important;
  padding: 0;
  background: transparent;
}
.spec-tag-input .el-input__inner {
  border: none;
  height: 24px;
  line-height: 24px;
  padding: 0;
}
</style>
