<template>
  <div style="padding:24px;">
    <el-card>
      <div style="display:flex;align-items:center;gap:8px;margin-bottom:16px;">
        <el-input v-model="search" placeholder="请输入学生姓名/学号/类型" style="width:250px;" />
        <el-button type="primary" @click="onSearch">查询</el-button>
        <el-button type="primary" @click="onAdd">+添加记录</el-button>
        <el-button @click="showImport=true">批量导入</el-button>
        <el-button :disabled="!selectedRows.length" @click="onBatchDelete">批量删除</el-button>
        <el-button @click="exportExcel">批量导出</el-button>
      </div>
      <el-table :data="recordData" style="width:100%;" empty-text="暂无数据" @selection-change="onSelectionChange" ref="tableRef">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="studentName" label="学生姓名" />
        <el-table-column prop="studentId" label="学号" />
        <el-table-column prop="college" label="学院" />
        <el-table-column prop="type" label="违纪类型" />
        <el-table-column prop="date" label="日期" :formatter="formatDate" />
        <el-table-column prop="punishment" label="处分缘由" />
        <el-table-column label="操作" width="160">
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
    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="editForm" label-width="90px">
        <el-form-item label="学生姓名" required>
          <el-input v-model="editForm.studentName" />
        </el-form-item>
        <el-form-item label="学号" required>
          <el-input v-model="editForm.studentId" />
        </el-form-item>
        <el-form-item label="学院" required>
          <el-select v-model="editForm.college" placeholder="请选择学院">
            <el-option v-for="item in collegeOptions" :key="item.id" :label="item.name" :value="item.name" />
          </el-select>
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
          <el-input v-model="editForm.punishment" placeholder="请输入处分" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
    <!-- 批量导入弹窗 -->
    <el-dialog v-model="showImport" title="批量导入" width="600px">
      <input type="file" @change="onFileChange" accept=".xlsx,.xls" />
      <el-table :data="importPreview" v-if="importPreview.length" style="margin-top:16px;">
        <el-table-column prop="studentName" label="学生姓名" />
        <el-table-column prop="studentId" label="学号" />
        <el-table-column prop="college" label="学院" />
        <el-table-column prop="type" label="类型" />
        <el-table-column prop="date" label="日期" />
        <el-table-column prop="punishment" label="处分" />
      </el-table>
      <template #footer>
        <el-button @click="showImport=false">取消</el-button>
        <el-button type="primary" @click="submitImport" :disabled="!importPreview.length">确认导入</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDisciplinePage, saveDiscipline, deleteDiscipline, importDisciplineBatch, batchDeleteDiscipline } from '@/api/discipline'
import { ElMessageBox, ElMessage } from 'element-plus'
import { getCollegeList } from '@/api/college'
import { getPunishmentTypeList } from '@/api/punishmentType'
import * as XLSX from 'xlsx'
import { useUserStore } from '@/store/user'

const recordData = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(10)
const search = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('添加记录')
const editForm = ref({ id: null, studentName: '', studentId: '', college: '', type: '', date: '', punishment: '' })
const collegeOptions = ref([])
const typeOptions = ref([])
const selectedRows = ref([])
const showImport = ref(false)
const importPreview = ref([])
const tableRef = ref(null)
const userStore = useUserStore()

function fetchData() {
  const params = {
    page: page.value,
    size: pageSize.value,
    college: userStore.college
  }
  
  // 添加搜索关键词
  if (search.value) {
    params.keyword = search.value
  }
  
  getDisciplinePage(params).then(res => {
    let data = res.data && res.data.data ? res.data.data : res.data
    recordData.value = (data.records || []).map(item => ({
      ...item,
      studentName: item.studentName || item.student_name,
      studentId: item.studentId || item.student_id,
    }))
    total.value = data.total || 0
  })
}

function fetchColleges() {
  getCollegeList().then(res => {
    collegeOptions.value = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
  })
}

function fetchTypes() {
  getPunishmentTypeList().then(res => {
    typeOptions.value = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
  })
}

onMounted(() => {
  // 自动恢复userStore
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (user && user.college) {
    userStore.college = user.college
  } else if (user && user.colleges) {
    if (Array.isArray(user.colleges)) {
      userStore.college = user.colleges[0]
    } else if (typeof user.colleges === 'string') {
      userStore.college = user.colleges.split(',')[0]
    }
  }
  fetchColleges()
  fetchTypes()
  fetchData()
})

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

function onAdd() {
  dialogTitle.value = '添加记录'
  editForm.value = { id: null, studentName: '', studentId: '', college: '', type: '', date: '', punishment: '' }
  dialogVisible.value = true
}

function onEdit(row) {
  dialogTitle.value = '编辑记录'
  editForm.value = { ...row }
  dialogVisible.value = true
}

function onSave() {
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

function onSelectionChange(val) {
  selectedRows.value = val
}

function onBatchDelete() {
  ElMessageBox.confirm('确定要批量删除选中记录吗？', '提示', { type: 'warning' })
    .then(() => {
      batchDeleteDiscipline(selectedRows.value.map(i => i.id)).then(() => {
        ElMessage.success('批量删除成功')
        fetchData()
        if (tableRef.value) tableRef.value.clearSelection()
      })
    })
}

function exportExcel() {
  const ws = XLSX.utils.json_to_sheet(selectedRows.value.length ? selectedRows.value : recordData.value)
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '违纪记录')
  XLSX.writeFile(wb, '违纪记录.xlsx')
}

function excelDateToString(serial) {
  if (!serial) return '';
  if (typeof serial === 'string') {
    // 兼容 2025/7/4 或 2025-7-4
    const match = serial.match(/^(\d{4})[\/-](\d{1,2})[\/-](\d{1,2})/);
    if (match) {
      return match[1] + '-' +
        String(match[2]).padStart(2, '0') + '-' +
        String(match[3]).padStart(2, '0');
    }
    // 已是标准日期字符串
    if (/^\d{4}-\d{2}-\d{2}/.test(serial)) return serial;
  }
  if (serial instanceof Date) {
    return serial.getFullYear() + '-' +
      String(serial.getMonth() + 1).padStart(2, '0') + '-' +
      String(serial.getDate()).padStart(2, '0');
  }
  // Excel序列号
  const utc_days = Math.floor(serial - 25569);
  const utc_value = utc_days * 86400;
  const date_info = new Date(utc_value * 1000);
  return date_info.getFullYear() + '-' +
    String(date_info.getMonth() + 1).padStart(2, '0') + '-' +
    String(date_info.getDate()).padStart(2, '0');
}

function onFileChange(e) {
  const file = e.target.files[0]
  const reader = new FileReader()
  reader.onload = (evt) => {
    const data = new Uint8Array(evt.target.result)
    const workbook = XLSX.read(data, { type: 'array' })
    const sheet = workbook.Sheets[workbook.SheetNames[0]]
    importPreview.value = XLSX.utils.sheet_to_json(sheet).map(row => {
      const rawDate = row['日期'] || row['date'];
      console.log('原始日期字段：', rawDate, typeof rawDate);
      return {
        studentName: row['学生姓名'] || row['studentName'],
        studentId: row['学号'] || row['studentId'],
        college: row['学院'] || row['college'],
        type: row['类型'] || row['type'],
        date: excelDateToString(rawDate),
        punishment: row['处分'] || row['punishment']
      }
    })
  }
  reader.readAsArrayBuffer(file)
}

function submitImport() {
  importDisciplineBatch(importPreview.value).then(() => {
    ElMessage.success('批量导入成功')
    showImport.value = false
    importPreview.value = []
    fetchData()
  })
}

function formatDate(row) {
  if (!row.date) return ''
  return row.date.split('T')[0]
}

function formatPunished(row) {
  if (row.punished === true) return '已处分'
  if (row.punished === false) return '未处分'
  return ''
}
</script> 