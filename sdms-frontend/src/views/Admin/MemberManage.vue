<template>
  <div style="padding:24px;">
    <el-card>
      <div style="display:flex;align-items:center;gap:8px;margin-bottom:16px;">
        <el-select v-model="filter.status" placeholder="请选择人员状态" style="width:120px;">
          <el-option label="全部" value="" />
          <el-option label="激活" value="active" />
          <el-option label="冻结" value="frozen" />
        </el-select>
        <el-select v-model="filter.role" placeholder="请选择系统角色" style="width:120px;">
          <el-option label="全部" value="" />
          <el-option label="系统管理员" value="admin" />
          <el-option label="学院角色" value="college" />
        </el-select>
        <el-input v-model="filter.keyword" placeholder="请输入姓名/工号/学号/关键字" style="width:200px;" />
        <el-button type="primary" @click="fetchData">查询</el-button>
        <el-button type="primary" @click="onAdd">+添加成员</el-button>
        <el-button @click="onBatchChange" :disabled="!multipleSelection.length">批量变更</el-button>
      </div>
      <el-table :data="memberData" style="width:100%;" empty-text="暂无数据" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="jobId" label="工号/学号" />
        <el-table-column prop="roles" label="系统角色">
          <template #default="scope">
            <el-tag v-for="role in scope.row.roles" :key="role" style="margin-right:4px;">{{ roleMap[role] || role }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="colleges" label="所属单位">
          <template #default="scope">
            <el-tag v-for="c in scope.row.colleges" :key="c" style="margin-right:4px;">{{ c }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="roleCode" label="角色代码" />
        <el-table-column prop="status" label="人员状态">
          <template #default="scope">
            <el-tag :type="scope.row.status==='active'?'success':'info'">{{ scope.row.status==='active'?'激活':'冻结' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="addTime" label="添加时间">
          <template #default="scope">
            {{ scope.row.addTime ? dayjs(scope.row.addTime).format('YYYY-MM-DD HH:mm:ss') : '' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button size="small" @click="onEdit(scope.row)">变更</el-button>
            <el-button size="small" type="danger" @click="onRemove(scope.row)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top:16px;"
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        :page-size="pageSize"
        :current-page="page"
        :page-sizes="[10, 20, 50, 100]"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </el-card>
    <!-- 添加/编辑/批量变更弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="姓名" required>
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="用户名" required>
          <el-input v-model="editForm.username" autocomplete="username" />
        </el-form-item>
        <el-form-item label="密码" required>
          <el-input v-model="editForm.password" type="password" autocomplete="new-password" />
        </el-form-item>
        <el-form-item label="工号/学号" required>
          <el-input v-model="editForm.jobId" />
        </el-form-item>
        <el-form-item label="赋予角色（多选）" required>
          <el-checkbox-group v-model="editForm.roles">
            <el-checkbox label="admin">系统管理员</el-checkbox>
            <el-checkbox label="college">学院角色</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="所属学院（多选）" required>
          <el-select v-model="editForm.colleges" multiple filterable placeholder="请选择管理学院">
            <el-option v-for="item in collegeOptions" :key="item.id" :label="item.name" :value="item.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="人员状态">
          <el-radio-group v-model="editForm.status">
            <el-radio label="active">激活</el-radio>
            <el-radio label="frozen">冻结</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="onSave">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserPage, saveUser, deleteUser } from '@/api/user'
import { getCollegeList } from '@/api/college'
import { ElMessageBox, ElMessage } from 'element-plus'
import dayjs from 'dayjs'

const memberData = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const filter = ref({ status: '', role: '', keyword: '' })
const dialogVisible = ref(false)
const dialogTitle = ref('添加成员')
const editForm = ref({ id: null, name: '', jobId: '', username: '', password: '', roles: [], colleges: [], status: 'active' })
const multipleSelection = ref([])
const collegeOptions = ref([])
const roleMap = { admin: '系统管理员', college: '学院角色' }

function fetchData() {
  getUserPage({
    ...filter.value,
    roles: filter.value.role ? filter.value.role : undefined,
    colleges: filter.value.college ? [filter.value.college] : undefined,
    status: filter.value.status,
    page: page.value,
    size: pageSize.value
  }).then(res => {
    let data = res.data && res.data.data ? res.data.data : res.data
    memberData.value = (data.records || []).map(u => ({
      ...u,
      name: u.name || u.username,
      jobId: u.jobId || '',
      roles: Array.isArray(u.roles) ? u.roles : (u.roles ? u.roles.split(',') : []),
      colleges: Array.isArray(u.colleges) ? u.colleges : (u.colleges ? u.colleges.split(',') : [])
    }))
    total.value = data.total || 0
  })
}
function fetchColleges() {
  getCollegeList().then(res => {
    collegeOptions.value = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
  })
}
onMounted(() => {
  fetchColleges()
  fetchData()
})
function handleSelectionChange(val) {
  multipleSelection.value = val
}
function handlePageChange(val) {
  page.value = val
  fetchData()
}

function handleSizeChange(val) {
  pageSize.value = val
  page.value = 1  // 重置到第一页
  fetchData()
}
function onAdd() {
  dialogTitle.value = '添加成员'
  editForm.value = { id: null, name: '', jobId: '', username: '', password: '', roles: [], colleges: [], status: 'active' }
  dialogVisible.value = true
}
function onEdit(row) {
  dialogTitle.value = '成员变更'
  editForm.value = { ...row, username: row.username || '', password: '' }
  dialogVisible.value = true
}
function onBatchChange() {
  dialogTitle.value = '批量变更'
  // 可批量赋值
  editForm.value = { roles: [], colleges: [], status: 'active' }
  dialogVisible.value = true
}
function onSave() {
  const submitData = {
    ...editForm.value,
    roles: Array.isArray(editForm.value.roles) ? editForm.value.roles.join(',') : editForm.value.roles,
    colleges: Array.isArray(editForm.value.colleges) ? editForm.value.colleges.join(',') : editForm.value.colleges
  }
  if (!submitData.username) {
    submitData.username = submitData.jobId || submitData.name
  }
  // password允许为空（编辑时不修改密码）
  if (!submitData.password) {
    delete submitData.password
  }
  delete submitData.addTime
  if (dialogTitle.value === '批量变更') {
    multipleSelection.value.forEach(user => {
      saveUser({ ...user, ...submitData }).then(() => {})
    })
    ElMessage.success('批量变更成功')
  } else {
    saveUser(submitData).then(() => {
      ElMessage.success('保存成功')
    })
  }
  dialogVisible.value = false
  fetchData()
}
function onRemove(row) {
  ElMessageBox.confirm('确定要移除该成员吗？', '提示', { type: 'warning' })
    .then(() => {
      deleteUser(row.id).then(() => {
        ElMessage.success('移除成功')
        fetchData()
      })
    })
}
</script> 