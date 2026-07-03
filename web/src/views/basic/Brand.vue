<template>
  <div class="page-card">
    <div class="page-header">
      <h3>✨ 商品品牌管理</h3>
      <el-button type="primary" size="small" @click="showDialog(null)">新增品牌</el-button>
    </div>
    <el-table :data="list" stripe border size="small">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="品牌名" min-width="160" />
      <el-table-column prop="num" label="品牌编码" width="120" />
      <el-table-column label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status===1?'success':'danger'" size="small">{{ row.status===1?'已生效':'已失效' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="editRow(row)">修改</el-button>
          <el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dlg.visible" :title="dlg.isNew?'新增品牌':'修改品牌'" width="460px">
      <el-form :model="dlg.form" label-width="80px" size="small">
        <el-form-item label="品牌名">
          <el-input v-model="dlg.form.name" placeholder="请输入品牌名" />
        </el-form-item>
        <el-form-item label="品牌编码">
          <el-input v-model="dlg.form.num" placeholder="请输入品牌编码" />
        </el-form-item>
        <el-form-item label="状态" v-if="!dlg.isNew">
          <el-radio-group v-model="dlg.form.status">
            <el-radio :value="1">已生效</el-radio>
            <el-radio :value="0">已失效</el-radio>
          </el-radio-group>
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

const list = ref<any[]>([]); const saving = ref(false)
const dlg = reactive({ visible:false, isNew:true, form:{id:null, name:'', num:'', status:1} })

async function fetch() { const r:any = await request.get(api.brandList); list.value = r.data || [] }
function showDialog(row:any) { dlg.isNew=true; dlg.form={id:null, name:'', num:'', status:1}; dlg.visible=true }
function editRow(row:any) { dlg.isNew=false; dlg.form={...row}; dlg.visible=true }
async function save() {
  saving.value=true; try { await request.post(api.brandSave, dlg.form); ElMessage.success(dlg.isNew?'已创建':'已更新'); dlg.visible=false; await fetch() } catch(e:any) { ElMessage.error(e.message) } finally { saving.value=false }
}
async function deleteRow(row:any) { try { await ElMessageBox.confirm(`确定删除「${row.name}」？`,'确认'); await request.delete(api.brandDelete(row.id)); ElMessage.success('已删除'); await fetch() } catch {} }
onMounted(fetch)
</script>

<style scoped>
.page-card { background:#fff; border-radius:8px; padding:20px; }
.page-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
.page-header h3 { margin:0; }
</style>