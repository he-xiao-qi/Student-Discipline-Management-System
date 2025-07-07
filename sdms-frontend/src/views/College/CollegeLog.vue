<template>
  <div style="padding:24px;">
    <el-card>
      <div style="font-size:18px;font-weight:bold;margin-bottom:16px;">操作日志</div>
      <el-table :data="collegeLogData" style="width:100%;" empty-text="暂无数据">
        <el-table-column prop="userName" label="用户" />
        <el-table-column prop="action" label="操作内容" />
        <el-table-column prop="time" label="操作时间" :formatter="formatDate" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCollegeLogList } from '@/api/collegeLog'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()
const collegeLogData = ref([])

function fetchData() {
  getCollegeLogList().then(res => {
    let arr = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
    collegeLogData.value = arr
  })
}

function formatDate(row) {
  if (!row.time) return ''
  // 兼容带T和时区的ISO格式
  const d = new Date(row.time)
  return d.getFullYear() + '-' +
    String(d.getMonth() + 1).padStart(2, '0') + '-' +
    String(d.getDate()).padStart(2, '0') + ' ' +
    String(d.getHours()).padStart(2, '0') + ':' +
    String(d.getMinutes()).padStart(2, '0') + ':' +
    String(d.getSeconds()).padStart(2, '0')
}

onMounted(fetchData)
</script> 