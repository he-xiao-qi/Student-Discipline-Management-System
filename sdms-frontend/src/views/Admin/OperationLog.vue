<template>
  <div style="padding:24px;">
    <el-card>
      <div style="font-size:18px;font-weight:bold;margin-bottom:16px;">操作日志</div>
      <el-table :data="logData" style="width:100%;" empty-text="暂无数据">
        <el-table-column prop="userName" label="用户" />
        <el-table-column prop="action" label="操作内容" />
        <el-table-column prop="time" label="操作时间" :formatter="formatTime" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getOperationLogList } from '@/api/operationLog'

const logData = ref([])

function fetchData() {
  getOperationLogList().then(res => {
    let arr = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
    logData.value = arr
  })
}

function formatTime(row) {
  if (!row.time) return ''
  const d = new Date(row.time)
  return d.getFullYear() + '-' + String(d.getMonth()+1).padStart(2,'0') + '-' + String(d.getDate()).padStart(2,'0') + ' ' + String(d.getHours()).padStart(2,'0') + ':' + String(d.getMinutes()).padStart(2,'0') + ':' + String(d.getSeconds()).padStart(2,'0')
}

onMounted(fetchData)
</script> 