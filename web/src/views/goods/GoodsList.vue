<template>
  <div class="page-card">
    <div class="page-header">
      <h3>📋 商品库管理</h3>
      <el-button type="primary" size="small" @click="showDialog(null)">新增商品</el-button>
    </div>

    <el-form :model="query" inline size="small" style="margin-bottom:12px">
      <el-form-item label="商品名称">
        <el-input v-model="query.name" placeholder="搜索名称" clearable @keyup.enter="search" />
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="query.categoryId" placeholder="全部" clearable style="width:140px">
          <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="品牌">
        <el-select v-model="query.brandId" placeholder="全部" clearable style="width:140px">
          <el-option v-for="b in brands" :key="b.id" :label="b.name" :value="b.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" placeholder="全部" clearable style="width:100px">
          <el-option label="上架" :value="1" />
          <el-option label="下架" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" stripe border size="small" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="商品名称" min-width="200">
        <template #default="{ row }">
          <div style="display:flex;align-items:center;gap:8px">
            <el-image v-if="row.image" :src="row.image" style="width:40px;height:40px;border-radius:4px" fit="cover" />
            <span>{{ row.name }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="goodsNum" label="商品编号" width="120" />
      <el-table-column prop="categoryName" label="分类" width="100" />
      <el-table-column prop="brandName" label="品牌" width="100" />
      <el-table-column prop="retailPrice" label="零售价" width="100" align="right" />
      <el-table-column label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status===1?'success':'danger'" size="small">{{ row.status===1?'上架':'下架' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="160" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="showDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top:12px;text-align:right">
      <el-pagination
        v-model:page="pageNum" v-model:page-size="pageSize"
        :total="total" :page-sizes="[10,20,50]"
        layout="total,sizes,prev,pager,next" @change="fetchData"
      />
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id?'编辑商品':'新增商品'" width="600px">
      <el-form :model="form" label-width="100px" size="small">
        <el-form-item label="商品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="商品编号">
          <el-input v-model="form.goodsNum" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="请选择" clearable style="width:100%">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="品牌">
          <el-select v-model="form.brandId" placeholder="请选择" clearable style="width:100%">
            <el-option v-for="b in brands" :key="b.id" :label="b.name" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="零售价">
          <el-input-number v-model="form.retailPrice" :precision="2" :min="0" style="width:200px" />
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.unitName" placeholder="如：件、个" style="width:120px" />
        </el-form-item>
        <el-form-item label="商品图片">
          <el-input v-model="form.image" placeholder="图片URL" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import request from '../../utils/request'
import { api } from '../../utils/api'

const loading = ref(false)
const submitting = ref(false)
const list = ref<any[]>([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(20)
const categories = ref<any[]>([])
const brands = ref<any[]>([])

const query = reactive({ name: '', categoryId: undefined as any, brandId: undefined as any, status: undefined as any })

const dialogVisible = ref(false)
const form = reactive<any>({})

function search() { pageNum.value = 1; fetchData() }
function resetQuery() {
  query.name = ''; query.categoryId = undefined; query.brandId = undefined; query.status = undefined
  search()
}

async function fetchData() {
  loading.value = true
  try {
    const res: any = await request.get('/api/sys-api/goods/list', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value, ...query }
    })
    list.value = res.rows || []
    total.value = res.total || 0
  } finally { loading.value = false }
}

async function loadOptions() {
  try {
    const [catRes, brandRes] = await Promise.all([
      request.get('/api/sys-api/basic/category/list'),
      request.get('/api/sys-api/basic/brand/list'),
    ])
    categories.value = catRes.data || []
    brands.value = brandRes.data || []
  } catch { /* ignore */ }
}

function showDialog(row: any) {
  if (row) {
    Object.assign(form, { ...row })
  } else {
    Object.keys(form).forEach(k => delete form[k])
  }
  dialogVisible.value = true
}

async function submitForm() {
  submitting.value = true
  try {
    const res: any = await request.post('/api/sys-api/goods/save', form)
    if (res.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.msg || '保存失败')
    }
  } finally { submitting.value = false }
}

async function deleteRow(row: any) {
  try {
    await ElMessageBox.confirm('确认删除该商品？', '提示', { type: 'warning' })
    const res: any = await request.delete(`/api/sys-api/goods/${row.id}`)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      fetchData()
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch { /* cancelled */ }
}

onMounted(() => { fetchData(); loadOptions() })
</script>
