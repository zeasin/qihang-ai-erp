<template>
  <div class="page-card">
    <div class="page-header">
      <h3>🤝 供应商档案</h3>
      <el-button type="primary" size="small" @click="showDialog(null)">新增供应商</el-button>
    </div>

    <!-- 搜索 -->
    <el-form :model="query" size="small" inline style="margin-bottom:12px">
      <el-form-item label="名称"><el-input v-model="query.name" placeholder="供应商名称" clearable @keyup.enter="fetch" /></el-form-item>
      <el-form-item label="编码"><el-input v-model="query.number" placeholder="供应商编码" clearable @keyup.enter="fetch" /></el-form-item>
      <el-form-item label="联系人"><el-input v-model="query.linkMan" placeholder="联系人" clearable @keyup.enter="fetch" /></el-form-item>
      <el-form-item label="代发">
        <el-select v-model="query.isShipper" clearable placeholder="是否代发" @change="fetch" style="width:130px">
          <el-option label="支持代发" :value="1" /><el-option label="不支持代发" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" @click="fetch">搜索</el-button>
        <el-button size="small" @click="query={name:'',number:'',linkMan:'',isShipper:null};fetch()">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" stripe border size="small">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="供应商名称" min-width="140" />
      <el-table-column prop="number" label="编码" width="100" />
      <el-table-column label="代发" width="100" align="center">
        <template #default="{ row }"><el-tag :type="row.isShipper===1?'success':'info'" size="small">{{ row.isShipper===1?'支持':'不支持' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="linkMan" label="联系人" width="90" />
      <el-table-column prop="contact" label="联系电话" width="120" />
      <el-table-column prop="address" label="地址" min-width="160" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="140" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="editRow(row)">修改</el-button>
          <el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/修改 -->
    <el-dialog v-model="dlg.visible" :title="dlg.isNew?'新增供应商':'修改供应商'" width="700px" top="5vh">
      <el-form :model="dlg.form" label-width="110px" size="small">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="供应商名称"><el-input v-model="dlg.form.name" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商编码"><el-input v-model="dlg.form.number" /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="联系人"><el-input v-model="dlg.form.linkMan" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话"><el-input v-model="dlg.form.contact" /></el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="联系地址"><el-input v-model="dlg.form.address" /></el-form-item>
        <el-form-item label="是否支持代发">
          <el-radio-group v-model="dlg.form.isShipper">
            <el-radio :value="1">支持代发</el-radio><el-radio :value="0">不支持代发</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="社会信用代码"><el-input v-model="dlg.form.usci" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="法人"><el-input v-model="dlg.form.blFaren" /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="开户银行"><el-input v-model="dlg.form.bank" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开户名"><el-input v-model="dlg.form.bankAccountName" /></el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="银行账号"><el-input v-model="dlg.form.bankAccount" /></el-form-item>
        <el-form-item label="分管采购员"><el-input v-model="dlg.form.purchaserName" /></el-form-item>
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
const query = reactive({ name:'', number:'', linkMan:'', isShipper:null as number|null })
const dlg = reactive({ visible:false, isNew:true, form:{id:null as number|null, name:'', number:'', isShipper:0, linkMan:'', contact:'', address:'', usci:'', blFaren:'', bank:'', bankAccountName:'', bankAccount:'', purchaserName:'', remark:''} })

async function fetch() {
  const r:any = await request.get(api.supplierList, { params:query })
  list.value = r.data || []
}
function showDialog(row:any) { dlg.isNew=true; dlg.form={id:null, name:'', number:'', isShipper:0, linkMan:'', contact:'', address:'', usci:'', blFaren:'', bank:'', bankAccountName:'', bankAccount:'', purchaserName:'', remark:''}; dlg.visible=true }
function editRow(row:any) { dlg.isNew=false; dlg.form={...row, isShipper:row.isShipper||0}; dlg.visible=true }
async function save() { saving.value=true; try { await request.post(api.supplierSave, dlg.form); ElMessage.success(dlg.isNew?'已创建':'已更新'); dlg.visible=false; await fetch() } catch(e:any) { ElMessage.error(e.message) } finally { saving.value=false } }
async function deleteRow(row:any) { try { await ElMessageBox.confirm(`确定删除「${row.name}」？`,'确认'); await request.delete(api.supplierDelete(row.id)); ElMessage.success('已删除'); await fetch() } catch {} }
onMounted(fetch)
</script>

<style scoped>
.page-card { background:#fff; border-radius:8px; padding:20px; }
.page-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
.page-header h3 { margin:0; }
</style>