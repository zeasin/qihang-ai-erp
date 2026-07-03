<template>
  <div class="page-card">
    <div class="page-header"><h3>🏢 商户管理</h3><el-button type="primary" size="small" @click="showDialog(null)">新增商户</el-button></div>
    <el-table :data="list" stripe size="small">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="商户名称" min-width="140" />
      <el-table-column prop="number" label="编码" width="100" />
      <el-table-column prop="loginName" label="登录账号" width="120" />
      <el-table-column prop="mobile" label="手机号" width="120" />
      <el-table-column prop="linkMan" label="联系人" width="100" />
      <el-table-column prop="usci" label="信用代码" min-width="160" />
      <el-table-column label="操作" width="160" align="center">
        <template #default="{row}"><el-button size="small" type="primary" link @click="showDialog(row)">编辑</el-button><el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dlg.visible" :title="dlg.isNew?'新增商户':'编辑商户'" width="500px">
      <el-form :model="dlg.form" label-width="100px" size="small">
        <el-form-item label="商户名称"><el-input v-model="dlg.form.name" /></el-form-item>
        <el-form-item label="编码"><el-input v-model="dlg.form.number" /></el-form-item>
        <el-form-item label="登录账号"><el-input v-model="dlg.form.loginName" /></el-form-item>
        <el-form-item label="联系人"><el-input v-model="dlg.form.linkMan" /></el-form-item>
        <el-form-item label="手机号"><el-input v-model="dlg.form.mobile" /></el-form-item>
        <el-form-item label="信用代码"><el-input v-model="dlg.form.usci" /></el-form-item>
        <el-form-item label="地址"><el-input v-model="dlg.form.address" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dlg.visible=false">取消</el-button><el-button type="primary" @click="save" :loading="saving">保存</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'
const list = ref<any[]>([]); const saving = ref(false)
const dlg = reactive({ visible:false, isNew:true, form:{id:null, name:'', number:'', loginName:'', linkMan:'', mobile:'', usci:'', address:''} })
async function fetch() { const r:any = await request.get('/sys-api/channel/merchant/list'); list.value = r.data || [] }
function showDialog(row:any) { dlg.isNew=!row; dlg.form=row?{...row}:{id:null, name:'', number:'', loginName:'', linkMan:'', mobile:'', usci:'', address:''}; dlg.visible=true }
async function save() { saving.value=true; try { await request.post('/sys-api/channel/merchant/save', dlg.form); ElMessage.success(dlg.isNew?'已创建':'已更新'); dlg.visible=false; await fetch() } catch(e:any) { ElMessage.error(e.message) } finally { saving.value=false } }
async function deleteRow(row:any) { try { await ElMessageBox.confirm('确定删除？','确认'); await request.delete(`/sys-api/channel/merchant/${row.id}`); ElMessage.success('已删除'); await fetch() } catch {} }
onMounted(fetch)
</script>
<style scoped>
.page-card { background:#fff; border-radius:8px; padding:20px; }
.page-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
.page-header h3 { margin:0; }
</style>