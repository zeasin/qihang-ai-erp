<template>
  <div class="page-card">
    <div class="page-header">
      <h3>📁 菜单管理</h3>
      <el-button type="primary" size="small" @click="showDialog(null)">新增菜单</el-button>
    </div>
    <el-table :data="menus" row-key="menuId" border stripe default-expand-all size="small"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
      <el-table-column prop="menuName" label="菜单名称" min-width="180" />
      <el-table-column prop="icon" label="图标" width="60" align="center" />
      <el-table-column prop="perms" label="权限标识" min-width="160" />
      <el-table-column prop="path" label="路由地址" min-width="140" />
      <el-table-column prop="menuType" label="类型" width="80" align="center">
        <template #default="{ row }">
          <el-tag v-if="row.menuType === 'M'" size="small">目录</el-tag>
          <el-tag v-else-if="row.menuType === 'C'" size="small" type="primary">菜单</el-tag>
          <el-tag v-else size="small" type="info">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" label="排序" width="60" align="center" />
      <el-table-column label="操作" width="200" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="showDialog(row)">编辑</el-button>
          <el-button size="small" type="primary" link @click="showDialog(null, row.menuId)">新增子菜单</el-button>
          <el-button size="small" type="danger" link @click="deleteMenu(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialog.visible" :title="dialog.isNew ? '新增菜单' : '编辑菜单'" width="500px">
      <el-form :model="dialog.form" label-width="80px" size="small">
        <el-form-item label="上级菜单">
          <el-tree-select v-model="dialog.form.parentId" :data="menuTree" :props="{ label: 'menuName', value: 'menuId' }"
            placeholder="顶级菜单" clearable filterable style="width:100%" />
        </el-form-item>
        <el-form-item label="菜单名称">
          <el-input v-model="dialog.form.menuName" />
        </el-form-item>
        <el-form-item label="类型">
          <el-radio-group v-model="dialog.form.menuType">
            <el-radio value="M">目录</el-radio>
            <el-radio value="C">菜单</el-radio>
            <el-radio value="F">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="路由地址" v-if="dialog.form.menuType !== 'F'">
          <el-input v-model="dialog.form.path" placeholder="如：/workspace/order" />
        </el-form-item>
        <el-form-item label="权限标识">
          <el-input v-model="dialog.form.perms" placeholder="如：workspace:order:view" />
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="dialog.form.icon" placeholder="如：📋" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="dialog.form.orderNum" :min="0" :max="999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="saveMenu" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'

const menus = ref<any[]>([])
const saving = ref(false)

interface MenuForm {
  menuId: number | null; menuName: string; parentId: number | null;
  path: string; perms: string; icon: string; menuType: string; orderNum: number
}

const dialog = reactive({
  visible: false,
  isNew: true,
  form: { menuId: null, menuName: '', parentId: null, path: '', perms: '', icon: '', menuType: 'C', orderNum: 0 } as MenuForm,
})

const menuTree = computed(() => menus.value)

async function fetchMenus() {
  try {
    const res: any = await request.get('/sys-api/system/menu/tree')
    menus.value = res.data || []
  } catch {}
}

function showDialog(row: any, parentId?: number) {
  if (row) {
    dialog.isNew = false
    dialog.form = { ...row } as MenuForm
  } else {
    dialog.isNew = true
    dialog.form = { menuId: null, menuName: '', parentId: parentId ?? null, path: '', perms: '', icon: '', menuType: 'C', orderNum: 0 }
  }
  dialog.visible = true
}

async function saveMenu() {
  saving.value = true
  try {
    await request.post('/sys-api/system/menu/save', dialog.form)
    ElMessage.success(dialog.isNew ? '菜单已创建' : '菜单已更新')
    dialog.visible = false
    await fetchMenus()
  } catch (e: any) {
    ElMessage.error(e.message)
  } finally {
    saving.value = false
  }
}

async function deleteMenu(row: any) {
  try {
    await ElMessageBox.confirm(`确定删除菜单「${row.menuName}」？`, '确认')
    await request.delete(`/sys-api/system/menu/${row.menuId}`)
    ElMessage.success('菜单已删除')
    await fetchMenus()
  } catch {}
}

onMounted(fetchMenus)
</script>

<style scoped>
.page-card { background: #fff; border-radius: 8px; padding: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { margin: 0; }
</style>
