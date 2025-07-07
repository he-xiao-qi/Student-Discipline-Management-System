<template>
  <div style="padding:24px;">
    <el-card>
      <div style="font-size:18px;font-weight:bold;margin-bottom:16px;">违纪设置</div>
      <el-table :data="typeData" style="width:100%;">
        <el-table-column prop="name" label="违纪类型" />
        <el-table-column prop="description" label="描述" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="onEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="onDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button type="primary" style="margin-top:16px;" @click="onAdd">添加类型</el-button>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="400px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="类型名称" required>
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="editForm.description" />
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
import { getPunishmentTypeList, savePunishmentType, deletePunishmentType } from '@/api/punishmentType'
import { ElMessageBox, ElMessage } from 'element-plus'

const typeData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加类型')
const editForm = ref({ id: null, name: '', description: '' })

function fetchData() {
  getPunishmentTypeList().then(res => {
    let arr = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
    typeData.value = arr
    console.log('typeData.value:', typeData.value, Array.isArray(typeData.value))
  })
}
onMounted(fetchData)

function onAdd() {
  dialogTitle.value = '添加类型'
  editForm.value = { name: '', description: '' }
  dialogVisible.value = true
}
function onEdit(row) {
  dialogTitle.value = '编辑类型'
  editForm.value = { ...row }
  dialogVisible.value = true
}
function onSave() {
  if (!editForm.value.id) {
    delete editForm.value.id
  }
  savePunishmentType(editForm.value).then(() => {
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
  })
}
function onDelete(row) {
  ElMessageBox.confirm('确定要删除该类型吗？', '提示', { type: 'warning' })
    .then(() => {
      deletePunishmentType(row.id).then(res => {
        if (res.code === 0 || res.code === 200) {
          ElMessage.success('删除成功')
          fetchData()
        } else {
          ElMessage.error(res.msg || '删除失败')
        }
      })
    })
}
</script> 