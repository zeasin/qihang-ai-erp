<template>
  <div class="role-manage">
    <el-container style="height: 100vh">
      <el-header class="rm-header">
        <el-button text @click="$router.push('/')">← 首页</el-button>
        <span class="rm-title">⚙️ 角色管理</span>
        <el-button type="primary" @click="showRoleDialog(null)">+ 新增角色</el-button>
      </el-header>

      <el-main class="rm-main">
        <!-- 角色列表 -->
        <el-card shadow="never">
          <el-table :data="roles" stripe style="width: 100%">
            <el-table-column prop="roleId" label="ID" width="60" />
            <el-table-column prop="roleName" label="角色名称" width="140" />
            <el-table-column prop="roleKey" label="角色标识" width="120" />
            <el-table-column prop="remark" label="说明" />
            <el-table-column label="用户数" width="80" align="center">
              <template #default="{ row }">
                <el-tag size="small" type="info">{{ userCountByRole(row.roleId) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="260" align="center">
              <template #default="{ row }">
                <el-button size="small" type="primary" @click="showPermissionDialog(row)">分配权限</el-button>
                <el-button size="small" @click="showRoleDialog(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteRole(row)" :disabled="row.roleId === 1">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <!-- 用户管理 -->
        <el-card shadow="never" style="margin-top: 20px">
          <template #header><span>👤 用户角色分配</span></template>
          <el-table :data="users" stripe>
            <el-table-column prop="userName" label="账号" width="120" />
            <el-table-column prop="nickName" label="姓名" width="120" />
            <el-table-column label="当前角色" min-width="200">
              <template #default="{ row }">
                <el-tag v-for="rid in (row.roleIds || [])" :key="rid" size="small" style="margin-right:4px">
                  {{ roleName(rid) }}
                </el-tag>
                <span v-if="!row.roleIds || row.roleIds.length === 0" style="color:#999">未分配</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="160" align="center">
              <template #default="{ row }">
                <el-button size="small" @click="showUserRoleDialog(row)">分配角色</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-main>
    </el-container>

    <!-- 角色编辑对话框 -->
    <el-dialog v-model="roleDialog.visible" :title="roleDialog.isNew ? '新增角色' : '编辑角色'" width="420px">
      <el-form :model="roleDialog.form" label-width="80px">
        <el-form-item label="角色名称">
          <el-input v-model="roleDialog.form.roleName" placeholder="如：夜班拣货员" />
        </el-form-item>
        <el-form-item label="角色标识">
          <el-input v-model="roleDialog.form.roleKey" placeholder="如：night-picker" />
        </el-form-item>
        <el-form-item label="说明">
          <el-input v-model="roleDialog.form.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="roleDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="saveRole" :loading="saving">保存</el-button>
      </template>
    </el-dialog>

    <!-- 权限分配对话框 -->
    <el-dialog v-model="permDialog.visible" :title="'分配权限 - ' + permDialog.roleName" width="500px">
      <el-tree
        ref="menuTreeRef"
        :data="menuTree"
        show-checkbox
        node-key="menuId"
        :props="{ label: 'menuName', children: 'children' }"
        default-expand-all
        highlight-current
      />
      <template #footer>
        <el-button @click="permDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="savePermissions" :loading="saving">保存</el-button>
      </template>
    </el-dialog>

    <!-- 用户角色分配对话框 -->
    <el-dialog v-model="userRoleDialog.visible" :title="'分配角色 - ' + userRoleDialog.userName" width="420px">
      <el-checkbox-group v-model="userRoleDialog.selectedRoles">
        <el-checkbox v-for="r in roles" :key="r.roleId" :label="r.roleId" style="display:flex;margin-bottom:12px">
          <div>
            <div style="font-weight:600">{{ r.roleName }}</div>
            <div style="font-size:12px;color:#999">{{ r.remark || r.roleKey }}</div>
          </div>
        </el-checkbox>
      </el-checkbox-group>
      <template #footer>
        <el-button @click="userRoleDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="saveUserRoles" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../utils/request'
import { api } from '../../utils/api'

const roles = ref<any[]>([])
const menuTree = ref<any[]>([])
const users = ref<any[]>([])
const saving = ref(false)
const menuTreeRef = ref<any>(null)

const roleDialog = reactive({
  visible: false,
  isNew: true,
  form: { roleId: null, roleName: '', roleKey: '', remark: '' },
})

const permDialog = reactive({
  visible: false,
  roleId: null as number | null,
  roleName: '',
})

const userRoleDialog = reactive({
  visible: false,
  userId: null as number | null,
  userName: '',
  selectedRoles: [] as number[],
})

function roleName(roleId: number) {
  return roles.value.find(r => r.roleId === roleId)?.roleName || `#${roleId}`
}

function userCountByRole(roleId: number) {
  return users.value.filter((u: any) => (u.roleIds || []).includes(roleId)).length
}

async function fetchData() {
  const [r, m, u] = await Promise.all([
    request.get(api.roleList),
    request.get(api.menuTree),
    request.get(api.userList),
  ])
  roles.value = (r as any).data || []
  menuTree.value = (m as any).data || []
  users.value = (u as any).data || []
}

function showRoleDialog(row: any) {
  if (row) {
    roleDialog.isNew = false
    roleDialog.form = { ...row }
  } else {
    roleDialog.isNew = true
    roleDialog.form = { roleId: null, roleName: '', roleKey: '', remark: '' }
  }
  roleDialog.visible = true
}

async function saveRole() {
  saving.value = true
  try {
    await request.post(api.roleSave, roleDialog.form)
    ElMessage.success(roleDialog.isNew ? '角色已创建' : '角色已更新')
    roleDialog.visible = false
    await fetchData()
  } catch (e: any) {
    ElMessage.error(e.message)
  } finally {
    saving.value = false
  }
}

async function deleteRole(row: any) {
  try {
    await ElMessageBox.confirm(`确定删除角色「${row.roleName}」？`, '确认')
    await request.delete(api.roleDelete(row.roleId))
    ElMessage.success('角色已删除')
    await fetchData()
  } catch {}
}

async function showPermissionDialog(row: any) {
  permDialog.roleId = row.roleId
  permDialog.roleName = row.roleName
  permDialog.visible = true
  // 等待树渲染，再设置勾选
  await new Promise(r => setTimeout(r, 100))
  const res: any = await request.get(api.roleMenus(row.roleId))
  const checkedIds = res.data || []
  if (menuTreeRef.value) {
    menuTreeRef.value.setCheckedKeys(checkedIds)
  }
}

async function savePermissions() {
  if (!permDialog.roleId) return
  saving.value = true
  try {
    const checkedIds = menuTreeRef.value?.getCheckedKeys() || []
    const halfIds = menuTreeRef.value?.getHalfCheckedKeys() || []
    await request.put(api.roleUpdateMenus(permDialog.roleId), [...checkedIds, ...halfIds])
    ElMessage.success('权限已更新')
    permDialog.visible = false
  } catch (e: any) {
    ElMessage.error(e.message)
  } finally {
    saving.value = false
  }
}

function showUserRoleDialog(row: any) {
  userRoleDialog.userId = row.userId
  userRoleDialog.userName = row.nickName || row.userName
  userRoleDialog.selectedRoles = [...(row.roleIds || [])]
  userRoleDialog.visible = true
}

async function saveUserRoles() {
  if (!userRoleDialog.userId) return
  saving.value = true
  try {
    await request.put(api.userRoles(userRoleDialog.userId), userRoleDialog.selectedRoles)
    ElMessage.success('用户角色已更新')
    userRoleDialog.visible = false
    await fetchData()
  } catch (e: any) {
    ElMessage.error(e.message)
  } finally {
    saving.value = false
  }
}

onMounted(fetchData)
</script>

<style scoped>
.rm-header {
  display: flex; align-items: center; gap: 12px;
  background: #fff; border-bottom: 1px solid #eee;
  padding: 0 20px; height: 56px;
}
.rm-title { font-weight: 600; font-size: 16px; margin-right: auto; }
.rm-main { background: #f5f7fa; padding: 20px; }
</style>
