<template>
  <div class="page-card">
    <div class="page-header">
      <h3>📋 商品列表</h3>
      <el-button type="primary" size="small" @click="$router.push('/goods/create')">新增商品</el-button>
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
          <el-button size="small" type="primary" link @click="$router.push('/goods/edit?id='+row.id)">编辑</el-button>
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
const categories = ref<any[]>([])
const brands = ref<any[]>([])

const query = reactive({ name: '', categoryId: undefined as any, brandId: undefined as any, status: undefined as any })

function search() { pageNum.value = 1; fetchData() }
function resetQuery() {
  query.name = ''; query.categoryId = undefined; query.brandId = undefined; query.status = undefined
  search()
}

async function fetchData() {
  loading.value = true
  try {
    const res: any = await request.get(api.goodsList, {
      params: { pageNum: pageNum.value, pageSize: pageSize.value, ...query }
    })
    list.value = res.rows || []
    total.value = res.total || 0
  } finally { loading.value = false }
}

async function loadOptions() {
  try {
    const [catRes, brandRes] = await Promise.all([
      request.get(api.categoryList),
      request.get(api.brandList),
    ])
    categories.value = catRes.data || []
    brands.value = brandRes.data || []
  } catch { /* ignore */ }
}

async function deleteRow(row: any) {
  try {
    await ElMessageBox.confirm('确认删除该商品？', '提示', { type: 'warning' })
    const res: any = await request.delete(api.goodsDelete(row.id))
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
