<template>
  <div class="page-card">
    <div class="page-header">
      <h3>📮 快递公司库</h3>
      <el-button type="primary" size="small" @click="showDialog(null)">新增快递公司</el-button>
    </div>
    <el-form :model="query" size="small" inline style="margin-bottom:12px">
      <el-form-item label="快递公司"><el-input v-model="query.name" placeholder="名称搜索" clearable @keyup.enter="fetch" /></el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable placeholder="全部" @change="fetch" style="width:110px">
          <el-option label="启用" :value="1" /><el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" @click="fetch">搜索</el-button>
        <el-button size="small" @click="query={name:'',status:null};fetch()">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="list" stripe border size="small">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="快递公司" min-width="160" />
      <el-table-column prop="code" label="编码" width="120" />
      <el-table-column prop="logisticsId" label="快递平台Id" width="120" />
      <el-table-column label="状态" width="80" align="center">
        <template #default="{ row }"><el-tag :type="row.status===1?'success':'info'" size="small">{{ row.status===1?'启用':'禁用' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
      <el-table-column label="操作" width="140" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="editRow(row)">修改</el-button>
          <el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dlg.visible" :title="dlg.isNew?'新增快递公司':'修改快递公司'" width="500px">
      <el-form :model="dlg.form" label-width="100px" size="small">
        <el-form-item label="快递公司名称"><el-input v-model="dlg.form.name" /></el-form-item>
        <el-form-item label="编码"><el-input v-model="dlg.form.code" /></el-form-item>
        <el-form-item label="快递平台Id"><el-input v-model="dlg.form.logisticsId" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="dlg.form.status">
            <el-radio :value="1">启用</el-radio><el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
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
const query = reactive({ name:'', status:null as number|null })
const dlg = reactive({ visible:false, isNew:true, form:{id:null as number|null, name:'', code:'', logisticsId:'', status:1, remark:''} })

async function fetch() {
  const r:any = await request.get(api.logisticsCompanyList, { params:query })
  list.value = r.data || []
}
function showDialog(row:any) { dlg.isNew=true; dlg.form={id:null, name:'', code:'', logisticsId:'', status:1, remark:''}; dlg.visible=true }
function editRow(row:any) { dlg.isNew=false; dlg.form={...row}; dlg.visible=true }
async function save() { saving.value=true; try { await request.post(api.logisticsCompanySave, dlg.form); ElMessage.success(dlg.isNew?'已创建':'已更新'); dlg.visible=false; await fetch() } catch(e:any) { ElMessage.error(e.message) } finally { saving.value=false } }
async function deleteRow(row:any) { try { await ElMessageBox.confirm(`确定删除「${row.name}」？`,'确认'); await request.delete(api.logisticsCompanyDelete(row.id)); ElMessage.success('已删除'); await fetch() } catch {} }
onMounted(fetch)
</script>

<style scoped>
.page-card { background:#fff; border-radius:8px; padding:20px; }
.page-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
.page-header h3 { margin:0; }
</style>