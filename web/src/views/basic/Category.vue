<template>
  <div class="page-card">
    <div class="page-header">
      <h3>🏷️ 商品分类管理</h3>
      <el-button type="primary" size="small" @click="showDialog(null)">新增分类</el-button>
    </div>
    <el-table :data="categoryList" row-key="id" border stripe size="small"
      :tree-props="{ children: 'children' }" default-expand-all>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="分类名称" min-width="160" />
      <el-table-column prop="number" label="分类编码" width="120" />
      <el-table-column prop="sort" label="排序" width="70" align="center" />
      <el-table-column prop="remark" label="备注" min-width="160" />
      <el-table-column label="操作" width="220" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="$router.push('/basic/attribute?categoryId='+row.id+'&categoryName='+row.name)" v-if="row.parentId === 0">规格属性</el-button>
          <el-button size="small" type="primary" link @click="showDialog(row)">新增</el-button>
          <el-button size="small" type="primary" link @click="editRow(row)">修改</el-button>
          <el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dlg.visible" :title="dlg.isNew ? '新增分类' : '修改分类'" width="460px">
      <el-form :model="dlg.form" label-width="80px" size="small">
        <el-form-item label="上级分类">
          <el-input v-model="dlg.form.parentName" disabled />
        </el-form-item>
        <el-form-item label="分类编码" prop="number">
          <el-input v-model="dlg.form.number" placeholder="请输入分类编码" />
        </el-form-item>
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="dlg.form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="dlg.form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="排序值">
          <el-input-number v-model="dlg.form.sort" :min="0" :max="999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dlg.visible=false">取消</el-button>
        <el-button type="primary" @click="save" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'
import { api } from '../../utils/api'

const saving = ref(false)
interface Category { id: number | null; number: string; name: string; remark: string; parentId: number; path: string; sort: number; image: string; isDelete: number; merchantId: number; createBy: string; }

function buildTree(list: any[], parentId: number): any[] {
  const tree: any[] = []
  for (const item of list) {
    if (item.parentId === parentId) {
      const children = buildTree(list, item.id)
      tree.push({
        ...item,
        children: children.length > 0 ? children : undefined
      })
    }
  }
  return tree.sort((a, b) => (a.sort || 0) - (b.sort || 0))
}

const rawList = ref<any[]>([])
const categoryList = ref<any[]>([])

const dlg = reactive({
  visible: false,
  isNew: true,
  form: {
    id: null as number | null,
    parentName: '一级分类',
    number: '',
    name: '',
    remark: '',
    parentId: 0,
    sort: 0,
  }
})

async function fetch() {
  const r: any = await request.get(api.categoryList)
  rawList.value = r.data || []
  categoryList.value = buildTree(rawList.value, 0)
}

function showDialog(row: any) {
  dlg.isNew = true
  dlg.form = {
    id: null,
    number: '',
    name: '',
    remark: '',
    parentId: row ? row.id : 0,
    parentName: row ? row.name : '一级分类',
    sort: 0,
  }
  dlg.visible = true
}

function editRow(row: any) {
  dlg.isNew = false
  dlg.form = {
    id: row.id,
    number: row.number || '',
    name: row.name || '',
    remark: row.remark || '',
    parentId: row.parentId,
    parentName: '',
    sort: row.sort || 0,
  }
  dlg.visible = true
}

async function save() {
  saving.value = true
  try {
    await request.post(api.categorySave, dlg.form)
    ElMessage.success(dlg.isNew ? '已创建' : '已更新')
    dlg.visible = false
    await fetch()
  } catch (e: any) { ElMessage.error(e.message) }
  finally { saving.value = false }
}

async function deleteRow(row: any) {
  try {
    await ElMessageBox.confirm(`确定删除「${row.name}」？`, '确认')
    await request.delete(api.categoryDelete(row.id))
    ElMessage.success('已删除')
    await fetch()
  } catch {}
}

onMounted(fetch)
</script>

<style scoped>
.page-card { background: #fff; border-radius: 8px; padding: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { margin: 0; }
</style>