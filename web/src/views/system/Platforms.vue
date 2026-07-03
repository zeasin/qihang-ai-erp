<template>
  <div class="page-card">
    <div class="page-header"><h3>📡 电商平台设置</h3><el-button type="primary" size="small" @click="showDialog(null)">新增渠道</el-button></div>
    <el-table :data="list" stripe size="small">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="平台名称" min-width="140" />
      <el-table-column prop="code" label="编码" width="100" />
      <el-table-column label="状态" width="80" align="center">
        <template #default="{ row }"><el-tag :type="row.status===0?'success':'danger'" size="small">{{ row.status===0?'启用':'关闭' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="60" align="center" />
      <el-table-column prop="serverUrl" label="接口地址" min-width="280" show-overflow-tooltip />
      <el-table-column label="操作" width="160" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="showDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dlg.visible" :title="dlg.isNew?'新增渠道':'编辑渠道'" width="520px">
      <el-form :model="dlg.form" label-width="100px" size="small">
        <el-form-item label="平台名称"><el-input v-model="dlg.form.name" /></el-form-item>
        <el-form-item label="编码"><el-input v-model="dlg.form.code" placeholder="如：TMALL" /></el-form-item>
        <el-form-item label="AppKey"><el-input v-model="dlg.form.appKey" /></el-form-item>
        <el-form-item label="AppSecret"><el-input v-model="dlg.form.appSecret" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="回调地址"><el-input v-model="dlg.form.redirectUri" /></el-form-item>
        <el-form-item label="接口地址"><el-input v-model="dlg.form.serverUrl" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="dlg.form.sort" :min="0" :max="999" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="dlg.form.status">
            <el-radio :value="0">启用</el-radio><el-radio :value="1">关闭</el-radio>
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
const dlg = reactive({ visible:false, isNew:true, form:{id:null, name:'', code:'', appKey:'', appSecret:'', redirectUri:'', serverUrl:'', sort:0, status:0} })
async function fetch() { const r:any = await request.get(api.platformList); list.value = r.data || [] }
function showDialog(row:any) { dlg.isNew=!row; dlg.form=row?{...row}:{id:null, name:'', code:'', appKey:'', appSecret:'', redirectUri:'', serverUrl:'', sort:0, status:0}; dlg.visible=true }
async function save() { saving.value=true; try { await request.post(api.platformSave, dlg.form); ElMessage.success(dlg.isNew?'已创建':'已更新'); dlg.visible=false; await fetch() } catch(e:any) { ElMessage.error(e.message) } finally { saving.value=false } }
async function deleteRow(row:any) { try { await ElMessageBox.confirm('确定删除？','确认'); await request.delete(api.platformDelete(row.id)); ElMessage.success('已删除'); await fetch() } catch {} }
onMounted(fetch)
</script>
<style scoped>
.page-card { background:#fff; border-radius:8px; padding:20px; }
.page-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
.page-header h3 { margin:0; }
</style>
