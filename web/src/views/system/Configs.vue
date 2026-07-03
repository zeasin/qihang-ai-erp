<template>
  <div class="page-card">
    <div class="page-header"><h3>⚙️ 系统参数配置</h3><el-button type="primary" size="small" @click="showDialog(null)">新增</el-button></div>
    <el-table :data="list" stripe size="small">
      <el-table-column prop="configId" label="ID" width="60" />
      <el-table-column prop="configName" label="参数名称" min-width="140" />
      <el-table-column prop="configKey" label="参数键名" min-width="160" />
      <el-table-column prop="configValue" label="参数键值" min-width="200" />
      <el-table-column prop="configType" label="内置" width="60" align="center">
        <template #default="{ row }"><el-tag v-if="row.configType==='Y'" size="small">是</el-tag><span v-else>否</span></template>
      </el-table-column>
      <el-table-column prop="remark" label="说明" min-width="160" />
      <el-table-column label="操作" width="160" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="showDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dlg.visible" :title="dlg.isNew?'新增配置':'编辑配置'" width="500px">
      <el-form :model="dlg.form" label-width="100px" size="small">
        <el-form-item label="参数名称"><el-input v-model="dlg.form.configName" /></el-form-item>
        <el-form-item label="参数键名"><el-input v-model="dlg.form.configKey" placeholder="如：sys.name" /></el-form-item>
        <el-form-item label="参数键值"><el-input v-model="dlg.form.configValue" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="系统内置">
          <el-radio-group v-model="dlg.form.configType"><el-radio value="Y">是</el-radio><el-radio value="N">否</el-radio></el-radio-group>
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
const dlg = reactive({ visible:false, isNew:true, form:{configId:null, configName:'', configKey:'', configValue:'', configType:'N', remark:''} })
async function fetch() { const r:any = await request.get(api.configList); list.value = r.data || [] }
function showDialog(row:any) { dlg.isNew=!row; dlg.form=row?{...row}:{configId:null, configName:'', configKey:'', configValue:'', configType:'N', remark:''}; dlg.visible=true }
async function save() { saving.value=true; try { await request.post(api.configSave, dlg.form); ElMessage.success(dlg.isNew?'已创建':'已更新'); dlg.visible=false; await fetch() } catch(e:any) { ElMessage.error(e.message) } finally { saving.value=false } }
async function deleteRow(row:any) { try { await ElMessageBox.confirm('确定删除？','确认'); await request.delete(api.configDelete(row.configId)); ElMessage.success('已删除'); await fetch() } catch {} }
onMounted(fetch)
</script>
<style scoped>
.page-card { background:#fff; border-radius:8px; padding:20px; }
.page-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
.page-header h3 { margin:0; }
</style>
