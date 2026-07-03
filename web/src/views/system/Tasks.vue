<template>
  <div class="page-card">
    <div class="page-header"><h3>⏰ 定时任务配置</h3></div>
    <el-table :data="list" stripe size="small">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="taskName" label="任务名称" min-width="200" />
      <el-table-column prop="cron" label="Cron表达式" min-width="140" />
      <el-table-column prop="method" label="调用方法" min-width="200" />
      <el-table-column prop="remark" label="说明" min-width="160" />
      <el-table-column label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status===1?'success':'info'" size="small">{{ row.status===1?'运行中':'已停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="showDialog(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dlg.visible" title="编辑任务" width="500px">
      <el-form :model="dlg.form" label-width="100px" size="small">
        <el-form-item label="任务名称"><el-input v-model="dlg.form.taskName" /></el-form-item>
        <el-form-item label="Cron表达式">
          <el-input v-model="dlg.form.cron" placeholder="如：0 0/5 * * * ?" />
          <div style="font-size:11px;color:#999;margin-top:4px">- 表示手动触发，无需定时</div>
        </el-form-item>
        <el-form-item label="调用方法"><el-input v-model="dlg.form.method" /></el-form-item>
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
import { ElMessage } from 'element-plus'
import request from '../../utils/request'
import { api } from '../../utils/api'
const list = ref<any[]>([]); const saving = ref(false)
const dlg = reactive({ visible:false, form:{id:null, taskName:'', cron:'', method:'', remark:''} })
async function fetch() { const r:any = await request.get(api.taskList); list.value = r.data || [] }
function showDialog(row:any) { dlg.form={...row}; dlg.visible=true }
async function save() { saving.value=true; try { await request.put(api.taskUpdate, dlg.form); ElMessage.success('已更新'); dlg.visible=false; await fetch() } catch(e:any) { ElMessage.error(e.message) } finally { saving.value=false } }
onMounted(fetch)
</script>
<style scoped>
.page-card { background:#fff; border-radius:8px; padding:20px; }
.page-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
.page-header h3 { margin:0; }
</style>
