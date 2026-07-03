<template>
  <div class="page-card">
    <div class="page-header">
      <h3>🏬 仓库管理</h3>
      <el-button type="primary" size="small" @click="showDialog(null)">新增仓库</el-button>
    </div>
    <el-table :data="list" stripe border size="small">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="warehouseNo" label="仓库编码" width="120" />
      <el-table-column prop="warehouseName" label="仓库名称" min-width="160" />
      <el-table-column label="类型" width="80" align="center">
        <template #default="{ row }"><el-tag size="small">{{ row.type===1?'本地仓':'云仓' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="状态" width="70" align="center">
        <template #default="{ row }"><el-tag :type="row.status==='0'?'success':'danger'" size="small">{{ row.status==='0'?'启用':'禁用' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="address" label="地址" min-width="180" show-overflow-tooltip />
      <el-table-column prop="contacts" label="联系人" width="90" />
      <el-table-column prop="phone" label="电话" width="120" />
      <el-table-column label="操作" width="140" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="editRow(row)">修改</el-button>
          <el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dlg.visible" :title="dlg.isNew?'新增仓库':'修改仓库'" width="650px" top="5vh">
      <el-form :model="dlg.form" label-width="90px" size="small">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="仓库名称"><el-input v-model="dlg.form.warehouseName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="仓库编码"><el-input v-model="dlg.form.warehouseNo" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="类型">
            <el-radio-group v-model="dlg.form.type"><el-radio :value="1">本地仓</el-radio><el-radio :value="2">云仓</el-radio></el-radio-group>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态">
            <el-radio-group v-model="dlg.form.status"><el-radio value="0">启用</el-radio><el-radio value="1">禁用</el-radio></el-radio-group>
          </el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="8"><el-form-item label="省"><el-input v-model="dlg.form.province" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="市"><el-input v-model="dlg.form.city" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="区"><el-input v-model="dlg.form.county" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="详细地址"><el-input v-model="dlg.form.address" /></el-form-item>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="联系人"><el-input v-model="dlg.form.contacts" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="联系电话"><el-input v-model="dlg.form.phone" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="备注"><el-input v-model="dlg.form.remark" type="textarea" :rows="2" /></el-form-item>
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
const dlg = reactive({ visible:false, isNew:true, form:{id:null as number|null, warehouseNo:'', warehouseName:'', type:1, status:'0', province:'', city:'', county:'', address:'', contacts:'', phone:'', remark:''} })

async function fetch() { const r:any = await request.get(api.warehouseList); list.value = r.data || [] }
function showDialog(row:any) { dlg.isNew=true; dlg.form={id:null, warehouseNo:'', warehouseName:'', type:1, status:'0', province:'', city:'', county:'', address:'', contacts:'', phone:'', remark:''}; dlg.visible=true }
function editRow(row:any) { dlg.isNew=false; dlg.form={...row}; dlg.visible=true }
async function save() { saving.value=true; try { await request.post(api.warehouseSave, dlg.form); ElMessage.success(dlg.isNew?'已创建':'已更新'); dlg.visible=false; await fetch() } catch(e:any) { ElMessage.error(e.message) } finally { saving.value=false } }
async function deleteRow(row:any) { try { await ElMessageBox.confirm(`确定删除「${row.warehouseName}」？`,'确认'); await request.delete(api.warehouseDelete(row.id)); ElMessage.success('已删除'); await fetch() } catch {} }
onMounted(fetch)
</script>

<style scoped>
.page-card { background:#fff; border-radius:8px; padding:20px; }
.page-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
.page-header h3 { margin:0; }
</style>