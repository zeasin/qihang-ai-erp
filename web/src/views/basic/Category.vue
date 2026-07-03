<template>
  <div class="page-card">
    <div class="page-header">
      <h3>🏷️ 商品分类管理</h3>
      <el-button type="primary" size="small" @click="showDialog(null)">新增分类</el-button>
    </div>
    <el-table :data="categoryList" row-key="id" border stripe size="small"
      :tree-props="{ children: 'children' }" default-expand-all>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="分类名称" min-width="160" />
      <el-table-column prop="number" label="分类编码" width="120" />
      <el-table-column prop="sort" label="排序" width="70" align="center" />
      <el-table-column prop="remark" label="备注" min-width="160" />
      <el-table-column label="操作" width="280" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="showAttrDialog(row)" v-if="row.parentId === 0">规格属性</el-button>
          <el-button size="small" type="primary" link @click="showDialog(row)">新增</el-button>
          <el-button size="small" type="primary" link @click="editRow(row)">修改</el-button>
          <el-button size="small" type="danger" link @click="deleteRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑分类 -->
    <el-dialog v-model="dlg.visible" :title="dlg.isNew ? '新增分类' : '修改分类'" width="460px">
      <el-form :model="dlg.form" label-width="80px" size="small">
        <el-form-item label="上级分类"><el-input v-model="dlg.form.parentName" disabled /></el-form-item>
        <el-form-item label="分类编码" prop="number"><el-input v-model="dlg.form.number" placeholder="请输入分类编码" /></el-form-item>
        <el-form-item label="分类名称" prop="name"><el-input v-model="dlg.form.name" placeholder="请输入分类名称" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="dlg.form.remark" type="textarea" placeholder="请输入备注" /></el-form-item>
        <el-form-item label="排序值"><el-input-number v-model="dlg.form.sort" :min="0" :max="999" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dlg.visible=false">取消</el-button>
        <el-button type="primary" @click="save" :loading="saving">保存</el-button>
      </template>
    </el-dialog>

    <!-- 规格属性弹窗 -->
    <el-dialog v-model="attrDlg.visible" :title="'📐 规格属性 — ' + attrDlg.categoryName" width="760px" top="5vh">
      <el-table :data="attrDlg.attributes" stripe border size="small" row-key="id"
        :tree-props="{ children: 'values' }" default-expand-all>
        <el-table-column prop="id" label="ID" width="50" />
        <el-table-column prop="title" label="属性名" width="140" />
        <el-table-column label="类型" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.type===1?'':'info'" size="small">{{ row.type===1?'规格':'属性' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="属性值" min-width="260">
          <template #default="{ row }">
            <div style="display:flex;flex-wrap:wrap;gap:4px">
              <el-tag v-for="v in row.values" :key="v.id" closable size="small" :disable-transitions="true" @close="deleteAttrValue(v)">
                {{ v.value }}<span v-if="v.skuCode" style="color:#999;margin-left:4px">({{ v.skuCode }})</span>
              </el-tag>
              <el-button size="small" type="primary" link @click="showAttrValueDlg(row)">+值</el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template #default="{ row }">
            <el-button size="small" type="primary" link @click="editAttr(row)">修改</el-button>
            <el-button size="small" type="danger" link @click="deleteAttr(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top:12px">
        <el-button type="primary" size="small" @click="showAttrDlg = true">新增属性</el-button>
      </div>

      <!-- 新增/编辑属性 -->
      <el-dialog v-model="showAttrDlg" :title="attrForm.id?'修改属性':'新增属性'" width="420px" append-to-body>
        <el-form :model="attrForm" label-width="80px" size="small">
          <el-form-item label="属性名"><el-input v-model="attrForm.title" placeholder="如：瓦数、颜色" /></el-form-item>
          <el-form-item label="类型">
            <el-radio-group v-model="attrForm.type">
              <el-radio :value="0">属性</el-radio><el-radio :value="1">规格</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="属性值类型">
            <el-select v-model="attrForm.code">
              <el-option label="color 颜色" value="color" /><el-option label="size 尺码" value="size" /><el-option label="style 款式" value="style" />
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showAttrDlg=false">取消</el-button>
          <el-button type="primary" @click="saveAttr" :loading="saving">保存</el-button>
        </template>
      </el-dialog>

      <!-- 新增属性值 -->
      <el-dialog v-model="showValDlg" :title="'新增属性值 — ' + valForm.attrTitle" width="420px" append-to-body>
        <el-form :model="valForm" label-width="100px" size="small">
          <el-form-item label="属性值文本"><el-input v-model="valForm.value" placeholder="如：15W" /></el-form-item>
          <el-form-item label="SKU编码"><el-input v-model="valForm.skuCode" placeholder="如：15W" /></el-form-item>
          <el-form-item label="排序"><el-input-number v-model="valForm.orderNum" :min="0" :max="999" /></el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showValDlg=false">取消</el-button>
          <el-button type="primary" @click="saveAttrValue" :loading="saving">保存</el-button>
        </template>
      </el-dialog>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'
import { api } from '../../utils/api'

const saving = ref(false)

function buildTree(list: any[], parentId: number): any[] {
  const tree: any[] = []
  for (const item of list) {
    if (item.parentId === parentId) {
      const children = buildTree(list, item.id)
      tree.push({ ...item, children: children.length > 0 ? children : undefined })
    }
  }
  return tree.sort((a, b) => (a.sort || 0) - (b.sort || 0))
}

// ─── 分类管理 ───
const rawList = ref<any[]>([])
const categoryList = ref<any[]>([])
const dlg = reactive({ visible:false, isNew:true, form:{id:null as number|null, parentName:'一级分类', number:'', name:'', remark:'', parentId:0, sort:0} })

async function fetch() { const r:any = await request.get(api.categoryList); rawList.value = r.data || []; categoryList.value = buildTree(rawList.value, 0) }
function showDialog(row:any) { dlg.isNew=true; dlg.form={id:null, number:'', name:'', remark:'', parentId:row?row.id:0, parentName:row?row.name:'一级分类', sort:0}; dlg.visible=true }
function editRow(row:any) { dlg.isNew=false; dlg.form={id:row.id, number:row.number||'', name:row.name||'', remark:row.remark||'', parentId:row.parentId, parentName:'', sort:row.sort||0}; dlg.visible=true }
async function save() { saving.value=true; try { await request.post(api.categorySave, dlg.form); ElMessage.success(dlg.isNew?'已创建':'已更新'); dlg.visible=false; await fetch() } catch(e:any) { ElMessage.error(e.message) } finally { saving.value=false } }
async function deleteRow(row:any) { try { await ElMessageBox.confirm(`确定删除「${row.name}」？`,'确认'); await request.delete(api.categoryDelete(row.id)); ElMessage.success('已删除'); await fetch() } catch {} }

// ─── 规格属性管理 ───
const attrDlg = reactive({ visible:false, categoryId:0, categoryName:'', attributes:[] as any[] })
const showAttrDlg = ref(false)
const attrForm = reactive({ id:null as number|null, title:'', type:1, code:'color' })
const showValDlg = ref(false)
const valForm = reactive({ categoryAttributeId:0, attrTitle:'', value:'', skuCode:'', orderNum:0 })

async function showAttrDialog(row:any) {
  attrDlg.categoryId = row.id
  attrDlg.categoryName = row.name
  attrDlg.visible = true
  await loadAttributes()
}

async function loadAttributes() {
  const r:any = await request.get(api.categoryAttrList, { params:{ categoryId: attrDlg.categoryId } })
  const attrs = r.data || []
  // 加载每个属性的值
  for (const a of attrs) {
    const vr:any = await request.get(api.categoryAttrValueList, { params:{ categoryAttributeId: a.id } })
    a.values = vr.data || []
  }
  attrDlg.attributes = attrs
}

function showAttrDlgFn() { attrForm.id=null; attrForm.title=''; attrForm.type=1; attrForm.code='color'; showAttrDlg.value=true }
function editAttr(row:any) { attrForm.id=row.id; attrForm.title=row.title; attrForm.type=row.type; attrForm.code=row.code; showAttrDlg.value=true }

async function saveAttr() {
  saving.value=true
  try {
    await request.post(api.categoryAttrSave, { id:attrForm.id, categoryId:attrDlg.categoryId, title:attrForm.title, type:attrForm.type, code:attrForm.code })
    ElMessage.success(attrForm.id?'已更新':'已创建')
    showAttrDlg.value=false
    await loadAttributes()
  } catch(e:any) { ElMessage.error(e.message) } finally { saving.value=false }
}

async function deleteAttr(row:any) {
  try { await ElMessageBox.confirm(`确定删除属性「${row.title}」？`,'确认'); await request.delete(api.categoryAttrDelete(row.id)); ElMessage.success('已删除'); await loadAttributes() } catch {}
}

function showAttrValueDlg(row:any) { valForm.categoryAttributeId=row.id; valForm.attrTitle=row.title; valForm.value=''; valForm.skuCode=''; valForm.orderNum=0; showValDlg.value=true }

async function saveAttrValue() {
  saving.value=true
  try {
    await request.post(api.categoryAttrValueSave, { categoryAttributeId:valForm.categoryAttributeId, value:valForm.value, skuCode:valForm.skuCode, orderNum:valForm.orderNum, isDelete:0 })
    ElMessage.success('已创建')
    showValDlg.value=false
    await loadAttributes()
  } catch(e:any) { ElMessage.error(e.message) } finally { saving.value=false }
}

async function deleteAttrValue(row:any) {
  try { await ElMessageBox.confirm(`确定删除属性值「${row.value}」？`,'确认'); await request.delete(api.categoryAttrValueDelete(row.id)); ElMessage.success('已删除'); await loadAttributes() } catch {}
}

onMounted(fetch)
</script>

<style scoped>
.page-card { background: #fff; border-radius: 8px; padding: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { margin: 0; }
</style>