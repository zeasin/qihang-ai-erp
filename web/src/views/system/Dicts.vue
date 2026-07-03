<template>
  <div class="page-card">
    <div class="page-header">
      <h3>📖 字典管理</h3>
      <el-button type="primary" size="small" @click="showTypeDialog(null)">新增字典</el-button>
    </div>

    <el-tabs v-model="activeTab" @tab-change="refreshData">
      <el-tab-pane label="字典类型" name="types">
        <el-table :data="types" stripe size="small" @row-click="selectType">
          <el-table-column prop="dictId" label="ID" width="60" />
          <el-table-column prop="dictName" label="字典名称" min-width="140" />
          <el-table-column prop="dictType" label="字典类型" min-width="160" />
          <el-table-column prop="remark" label="说明" min-width="200" />
          <el-table-column prop="status" label="状态" width="80" align="center">
            <template #default="{ row }">
              <el-tag v-if="row.status === '0'" size="small" type="success">正常</el-tag>
              <el-tag v-else size="small" type="danger">停用</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" align="center">
            <template #default="{ row }">
              <el-button size="small" type="primary" link @click.stop="showTypeDialog(row)">编辑</el-button>
              <el-button size="small" type="primary" link @click.stop="viewData(row)">字典数据</el-button>
              <el-button size="small" type="danger" link @click.stop="deleteType(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="字典数据" name="data">
        <template #label>
          <span>字典数据</span>
          <el-tag v-if="currentType" size="small" style="margin-left:6px">{{ currentType.dictType }}</el-tag>
        </template>
        <div class="data-toolbar">
          <span v-if="currentType" style="font-size:13px;color:#666">
            当前字典：<strong>{{ currentType.dictName }}</strong> ({{ currentType.dictType }})
          </span>
          <span v-else style="font-size:13px;color:#999">请先在「字典类型」中选择一个字典</span>
          <el-button v-if="currentType" type="primary" size="small" @click="showDataDialog(null)">新增数据</el-button>
        </div>
        <el-table :data="dataList" stripe size="small">
          <el-table-column prop="dictCode" label="编码" width="60" />
          <el-table-column prop="dictLabel" label="标签" min-width="120" />
          <el-table-column prop="dictValue" label="值" min-width="120" />
          <el-table-column prop="dictSort" label="排序" width="60" align="center" />
          <el-table-column prop="isDefault" label="默认" width="60" align="center">
            <template #default="{ row }">
              <el-tag v-if="row.isDefault === 'Y'" size="small" type="success">是</el-tag>
              <span v-else style="color:#999">否</span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="说明" min-width="160" />
          <el-table-column label="操作" width="140" align="center">
            <template #default="{ row }">
              <el-button size="small" type="primary" link @click="showDataDialog(row)">编辑</el-button>
              <el-button size="small" type="danger" link @click="deleteData(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 字典类型对话框 -->
    <el-dialog v-model="typeDlg.visible" :title="typeDlg.isNew ? '新增字典' : '编辑字典'" width="420px">
      <el-form :model="typeDlg.form" label-width="80px" size="small">
        <el-form-item label="字典名称"><el-input v-model="typeDlg.form.dictName" /></el-form-item>
        <el-form-item label="字典类型"><el-input v-model="typeDlg.form.dictType" placeholder="如：sys_user_sex" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="typeDlg.form.status">
            <el-radio value="0">正常</el-radio><el-radio value="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="说明"><el-input v-model="typeDlg.form.remark" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="typeDlg.visible = false">取消</el-button>
        <el-button type="primary" @click="saveType" :loading="saving">保存</el-button>
      </template>
    </el-dialog>

    <!-- 字典数据对话框 -->
    <el-dialog v-model="dataDlg.visible" :title="dataDlg.isNew ? '新增数据' : '编辑数据'" width="420px">
      <el-form :model="dataDlg.form" label-width="80px" size="small">
        <el-form-item label="标签"><el-input v-model="dataDlg.form.dictLabel" placeholder="如：男" /></el-form-item>
        <el-form-item label="值"><el-input v-model="dataDlg.form.dictValue" placeholder="如：0" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="dataDlg.form.dictSort" :min="0" :max="999" /></el-form-item>
        <el-form-item label="默认">
          <el-radio-group v-model="dataDlg.form.isDefault">
            <el-radio value="Y">是</el-radio><el-radio value="N">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="说明"><el-input v-model="dataDlg.form.remark" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dataDlg.visible = false">取消</el-button>
        <el-button type="primary" @click="saveData" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'
import { api } from '../../utils/api'

const activeTab = ref('types')
const types = ref<any[]>([])
const dataList = ref<any[]>([])
const currentType = ref<any>(null)
const saving = ref(false)

const typeDlg = reactive({ visible: false, isNew: true, form: { dictId: null, dictName: '', dictType: '', status: '0', remark: '' } })
const dataDlg = reactive({ visible: false, isNew: true, form: { dictCode: null, dictLabel: '', dictValue: '', dictSort: 0, isDefault: 'N', dictType: '', remark: '' } })

async function fetchTypes() {
  const r: any = await request.get(api.dictTypeList)
  types.value = r.data || []
}

async function fetchData() {
  if (!currentType.value) { dataList.value = []; return }
  const r: any = await request.get(api.dictDataList, { params: { dictType: currentType.value.dictType } })
  dataList.value = r.data || []
}

function refreshData() {
  if (activeTab.value === 'types') fetchTypes()
  else fetchData()
}

function selectType(row: any) { currentType.value = row }

function viewData(row: any) {
  currentType.value = row
  activeTab.value = 'data'
  fetchData()
}

function showTypeDialog(row: any) {
  typeDlg.isNew = !row
  typeDlg.form = row ? { ...row } : { dictId: null, dictName: '', dictType: '', status: '0', remark: '' }
  typeDlg.visible = true
}

async function saveType() {
  saving.value = true
  try {
    await request.post(api.dictTypeSave, typeDlg.form)
    ElMessage.success(typeDlg.isNew ? '字典已创建' : '字典已更新')
    typeDlg.visible = false; await fetchTypes()
  } catch (e: any) { ElMessage.error(e.message) }
  finally { saving.value = false }
}

async function deleteType(row: any) {
  try {
    await ElMessageBox.confirm(`确定删除字典「${row.dictName}」及其所有数据？`, '确认')
    await request.delete(api.dictTypeDelete(row.dictId))
    ElMessage.success('字典已删除'); await fetchTypes()
  } catch {}
}

function showDataDialog(row: any) {
  dataDlg.isNew = !row
  dataDlg.form = row ? { ...row } : { dictCode: null, dictLabel: '', dictValue: '', dictSort: 0, isDefault: 'N', dictType: currentType.value?.dictType || '', remark: '' }
  dataDlg.visible = true
}

async function saveData() {
  saving.value = true
  try {
    await request.post(api.dictDataSave, dataDlg.form)
    ElMessage.success(dataDlg.isNew ? '数据已创建' : '数据已更新')
    dataDlg.visible = false; await fetchData()
  } catch (e: any) { ElMessage.error(e.message) }
  finally { saving.value = false }
}

async function deleteData(row: any) {
  try {
    await ElMessageBox.confirm(`确定删除数据「${row.dictLabel}」？`, '确认')
    await request.delete(api.dictDataDelete(row.dictCode))
    ElMessage.success('数据已删除'); await fetchData()
  } catch {}
}

onMounted(fetchTypes)
</script>

<style scoped>
.page-card { background: #fff; border-radius: 8px; padding: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { margin: 0; }
.data-toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
</style>
