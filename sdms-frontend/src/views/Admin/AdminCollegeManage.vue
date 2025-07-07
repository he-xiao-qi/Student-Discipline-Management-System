<template>
  <el-card>
    <el-button type="primary" @click="onAdd" style="margin-bottom:16px;">+添加学院</el-button>
    <el-table :data="collegeData" style="width:100%;">
      <el-table-column prop="name" label="学院名称" />
      <el-table-column label="操作" width="160">
        <template #default="scope">
          <el-button size="small" @click="onEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="onDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="400px">
      <el-form :model="editForm" label-width="90px">
        <el-form-item label="学院名称" required>
          <el-input v-model="editForm.name" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { getCollegeList, saveCollege, deleteCollege } from '@/api/college'
const collegeData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加学院')
const editForm = ref({ id: null, name: '' })
function fetchData() {
  getCollegeList().then(res => {
    collegeData.value = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
  })
}
onMounted(fetchData)
function onAdd() {
  dialogTitle.value = '添加学院'
  editForm.value = { id: null, name: '' }
  dialogVisible.value = true
}
function onEdit(row) {
  dialogTitle.value = '编辑学院'
  editForm.value = { ...row }
  dialogVisible.value = true
}
function onSave() {
  const data = { ...editForm.value }
  if (!data.id) delete data.id // 新增时不传id
  saveCollege(data).then(() => {
    dialogVisible.value = false
    fetchData()
  })
}
function onDelete(row) {
  deleteCollege(row.id).then(() => fetchData())
}
</script> 