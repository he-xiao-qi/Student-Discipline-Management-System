<template>
  <div style="padding:24px;">
    <el-button type="primary" @click="onAdd">新增违纪记录</el-button>
    <el-card style="margin-top: 16px;">
      <div style="font-size:18px;font-weight:bold;margin-bottom:16px;">本院违纪处分</div>
      <div style="display:flex;align-items:center;gap:8px;margin-bottom:16px;">
        <el-input v-model="search" placeholder="请输入学生姓名/学号" style="width:250px;" />
        <el-button type="primary" @click="onSearch">查询</el-button>
      </div>
      <el-table :data="collegeDisciplineData" style="width:100%;" empty-text="暂无数据">
        <el-table-column prop="studentName" label="学生姓名" />
        <el-table-column prop="studentId" label="学号" />
        <el-table-column prop="type" label="违纪类型" />
        <el-table-column prop="date" label="日期" :formatter="formatDate" />
        <el-table-column prop="punishment" label="处分缘由" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="onEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="onDelete(scope.row)">删除</el-button>
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="editForm" label-width="90px">
        <el-form-item label="学生姓名" required>
          <el-input v-model="editForm.studentName" />
        </el-form-item>
        <el-form-item label="学号" required>
          <el-input v-model="editForm.studentId" />
        </el-form-item>
        <el-form-item label="违纪类型" required>
          <el-select v-model="editForm.type" placeholder="请选择类型">
            <el-option v-for="item in typeOptions" :key="item.id" :label="item.name" :value="item.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期" required>
          <el-date-picker v-model="editForm.date" type="date" placeholder="选择日期" style="width:100%;" />
        </el-form-item>
        <el-form-item label="处分">
          <el-input v-model="editForm.punishment" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDisciplinePage, saveDiscipline, deleteDiscipline } from '@/api/discipline'
import { useUserStore } from '@/store/user'
import { ElMessageBox, ElMessage } from 'element-plus'
import { getPunishmentTypeList } from '@/api/punishmentType'

const userStore = useUserStore()
const collegeDisciplineData = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const search = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('添加记录')
const editForm = ref({ id: null, studentName: '', studentId: '', type: '', date: '', punishment: '' })
const typeOptions = ref([])

async function fetchData() {
  const params = {
    page: page.value,
    size: pageSize.value,
    college: userStore.college
  }
  
  // 添加搜索关键词
  if (search.value) {
    params.keyword = search.value
  }
  
  const res = await getDisciplinePage(params)
  let data = res.data && res.data.data ? res.data.data : res.data
  // 字段兼容与去除空格
  collegeDisciplineData.value = (data.records || []).map(item => ({
    ...item,
    studentName: item.studentName || item.student_name,
    studentId: item.studentId || item.student_id,
    college: (item.college || '').trim(),
    type: item.type,
    date: item.date,
    punishment: item.punishment
  }))
  total.value = data.total || 0
}

function fetchTypes() {
  getPunishmentTypeList().then(res => {
    typeOptions.value = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
  })
}

onMounted(() => {
  fetchData()
  fetchTypes()
})

function onEdit(row) {
  dialogTitle.value = '编辑记录'
  editForm.value = { ...row }
  dialogVisible.value = true
}
function onSave() {
  editForm.value.college = userStore.college
  const data = { ...editForm.value, userId: userStore.id };
  saveDiscipline(data).then(() => {
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
  })
}
function onDelete(row) {
  ElMessageBox.confirm('确定要删除该记录吗？', '提示', { type: 'warning' })
    .then(() => {
      deleteDiscipline(row.id).then(() => {
        ElMessage.success('删除成功')
        fetchData()
      })
    })
}

function formatDate(row) {
  if (!row.date) return ''
  return row.date.split('T')[0]
}

function onAdd() {
  dialogTitle.value = '添加记录'
  editForm.value = { id: null, studentName: '', studentId: '', type: '', date: '', punishment: '' }
  dialogVisible.value = true
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

function onSearch() {
  page.value = 1  // 搜索时重置到第一页
  fetchData()
}
</script> 