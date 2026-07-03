<template>
  <div class="page-card">
    <div class="page-header"><h3>🔑 开放接口授权</h3><el-button type="primary" size="small" @click="showDialog(null)">新增</el-button></div>
    <el-table :data="list" stripe size="small">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="appKey" label="AppKey" min-width="180" />
      <el-table-column prop="appSecret" label="AppSecret" min-width="180" />
      <el-table-column prop="type" label="类型" width="100" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.type===10" size="small">回传配置</el-tag>
          <el-tag v-else-if="row.type===20" size="small" type="primary">开放平台</el-tag>
          <el-tag v-else size="small" type="info">其他</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status===1?'success':'danger'" size="small">{{ row.status===1?'启用':'禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="showDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dlg.visible" :title="dlg.isNew?'新增授权':'编辑授权'" width="480px">
      <el-form :model="dlg.form" label-width="90px" size="small">
        <el-form-item label="AppKey"><el-input v-model="dlg.form.appKey" /></el-form-item>
        <el-form-item label="AppSecret"><el-input v-model="dlg.form.appSecret" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="类型">
          <el-select v-model="dlg.form.type" style="width:100%">
            <el-option :value="10" label="回传配置" /><el-option :value="20" label="开放平台" /><el-option :value="99" label="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="白名单"><el-input v-model="dlg.form.whiteList" placeholder="IP白名单，多个用逗号分隔" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="dlg.form.status"><el-radio :value="1">启用</el-radio><el-radio :value="0">禁用</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="说明"><el-input v-model="dlg.form.remark" type="textarea" :rows="2" /></el-form-item>
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
const dlg = reactive({ visible:false, isNew:true, form:{id:null, appKey:'', appSecret:'', type:10, whiteList:'', status:1, remark:''} })
async function fetch() { const r:any = await request.get(api.openAuthList); list.value = r.data || [] }
function showDialog(row:any) { dlg.isNew=!row; dlg.form=row?{...row}:{id:null, appKey:'', appSecret:'', type:10, whiteList:'', status:1, remark:''}; dlg.visible=true }
async function save() { saving.value=true; try { await request.post(api.openAuthSave, dlg.form); ElMessage.success(dlg.isNew?'已创建':'已更新'); dlg.visible=false; await fetch() } catch(e:any) { ElMessage.error(e.message) } finally { saving.value=false } }
async function deleteRow(row:any) { try { await ElMessageBox.confirm('确定删除？','确认'); await request.delete(api.openAuthDelete(row.id)); ElMessage.success('已删除'); await fetch() } catch {} }
onMounted(fetch)
</script>
<style scoped>
.page-card { background:#fff; border-radius:8px; padding:20px; }
.page-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
.page-header h3 { margin:0; }
</style>
