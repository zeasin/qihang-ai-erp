<template>
  <div class="page-card">
    <div class="page-header"><h3>🏪 店铺管理</h3><el-button type="primary" size="small" @click="showDialog(null)">新增店铺</el-button></div>
    <el-table :data="list" stripe size="small">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="店铺名称" min-width="140" />
      <el-table-column prop="sellerId" label="平台店铺ID" min-width="140" />
      <el-table-column prop="type" label="平台" width="100">
        <template #default="{row}"><el-tag size="small">{{ platName(row.type) }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="merchantId" label="商户ID" width="80" align="center" />
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template #default="{row}"><el-tag :type="row.status===1?'success':'info'" size="small">{{ row.status===1?'正常':'停用' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template #default="{row}"><el-button size="small" type="primary" link @click="showDialog(row)">编辑</el-button><el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button></template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dlg.visible" :title="dlg.isNew?'新增店铺':'编辑店铺'" width="500px">
      <el-form :model="dlg.form" label-width="100px" size="small">
        <el-form-item label="店铺名称"><el-input v-model="dlg.form.name" /></el-form-item>
        <el-form-item label="所属平台"><el-select v-model="dlg.form.type" style="width:100%"><el-option v-for="p in platforms" :key="p.id" :label="p.name" :value="p.id" /></el-select></el-form-item>
        <el-form-item label="平台店铺ID"><el-input v-model="dlg.form.sellerId" /></el-form-item>
        <el-form-item label="商户ID"><el-input-number v-model="dlg.form.merchantId" :min="0" /></el-form-item>
        <el-form-item label="联系人"><el-input v-model="dlg.form.contact" /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="dlg.form.phone" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="dlg.form.remark" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dlg.visible=false">取消</el-button><el-button type="primary" @click="save" :loading="saving">保存</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'
const list = ref<any[]>([]); const platforms = ref<any[]>([]); const saving = ref(false)
const dlg = reactive({ visible:false, isNew:true, form:{id:null, name:'', type:null, sellerId:'', merchantId:0, contact:'', phone:'', remark:''} })
function platName(id: number) { return platforms.value.find((p:any) => p.id === id)?.name || id }
async function fetch() { const r:any = await request.get('/sys-api/channel/shop/list'); list.value = r.data || [] }
async function fetchPlats() { const r:any = await request.get('/sys-api/channel/platform/list'); platforms.value = r.data || [] }
function showDialog(row:any) { dlg.isNew=!row; dlg.form=row?{...row}:{id:null, name:'', type:null, sellerId:'', merchantId:0, contact:'', phone:'', remark:''}; dlg.visible=true }
async function save() { saving.value=true; try { await request.post('/sys-api/channel/shop/save', dlg.form); ElMessage.success(dlg.isNew?'已创建':'已更新'); dlg.visible=false; await fetch() } catch(e:any) { ElMessage.error(e.message) } finally { saving.value=false } }
async function deleteRow(row:any) { try { await ElMessageBox.confirm('确定删除？','确认'); await request.delete(`/sys-api/channel/shop/${row.id}`); ElMessage.success('已删除'); await fetch() } catch {} }
onMounted(() => { fetch(); fetchPlats() })
</script>
<style scoped>
.page-card { background:#fff; border-radius:8px; padding:20px; }
.page-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
.page-header h3 { margin:0; }
</style>