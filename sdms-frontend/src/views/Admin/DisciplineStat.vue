<template>
  <div style="padding:24px;">
    <el-card>
      <div style="font-size:18px;font-weight:bold;margin-bottom:16px;">违纪统计分析</div>
      <el-form :inline="true" style="margin-bottom:16px;">
        <el-form-item label="统计维度">
          <el-select v-model="statType" style="width:140px">
            <el-option label="按学院" value="college" />
            <el-option label="按类型" value="type" />
            <el-option label="按时间" value="date" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="statType==='date'" label="时间区间">
          <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" />
        </el-form-item>
        <el-button type="primary" @click="fetchData">查询</el-button>
        <el-button @click="exportExcel">导出Excel</el-button>
        <el-button @click="exportPDF">导出PDF</el-button>
      </el-form>
      <div ref="chartRef" style="width:100%;height:400px;margin-bottom:16px;"></div>
      <el-table :data="statData" style="width:100%;" empty-text="暂无数据">
        <el-table-column v-if="statType==='college'" prop="name" label="学院" />
        <el-table-column v-if="statType==='type'" prop="name" label="类型" />
        <el-table-column v-if="statType==='date'" prop="date" label="月份" />
        <el-table-column prop="count" label="违纪次数" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getDisciplineStat, getDisciplineStatByType, getDisciplineStatByDate } from '@/api/discipline'
import * as echarts from 'echarts'
import * as XLSX from 'xlsx'
import jsPDF from 'jspdf'
import 'jspdf-autotable'

const statType = ref('college')
const dateRange = ref([])
const statData = ref([])
const chartRef = ref(null)
let chartInstance = null

function fetchData() {
  if (statType.value === 'college') {
    getDisciplineStat().then(res => {
      statData.value = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
      renderChart()
    })
  } else if (statType.value === 'type') {
    getDisciplineStatByType().then(res => {
      statData.value = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
      renderChart()
    })
  } else if (statType.value === 'date') {
    const [start, end] = dateRange.value || []
    if (!start || !end) {
      statData.value = []
      renderChart()
      return
    }
    getDisciplineStatByDate({ start: start, end: end }).then(res => {
      statData.value = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
      renderChart()
    })
  }
}

function renderChart() {
  if (!chartInstance) {
    chartInstance = echarts.init(chartRef.value)
  }
  let option = {}
  if (statType.value === 'college' || statType.value === 'type') {
    option = {
      title: { text: statType.value === 'college' ? '各学院违纪数量统计' : '各类型违纪数量统计' },
      tooltip: {},
      xAxis: { type: 'category', data: statData.value.map(i => i.name) },
      yAxis: { type: 'value' },
      series: [{ data: statData.value.map(i => i.count), type: 'bar' }]
    }
  } else if (statType.value === 'date') {
    option = {
      title: { text: '违纪数量趋势' },
      tooltip: {},
      xAxis: { type: 'category', data: statData.value.map(i => i.date) },
      yAxis: { type: 'value' },
      series: [{ data: statData.value.map(i => i.count), type: 'line' }]
    }
  }
  chartInstance.setOption(option)
}

function exportExcel() {
  const ws = XLSX.utils.json_to_sheet(statData.value)
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '统计报表')
  XLSX.writeFile(wb, '违纪统计.xlsx')
}

function exportPDF() {
  const doc = new jsPDF()
  doc.text('违纪统计报表', 10, 10)
  let head = []
  if (statType.value === 'college') head = [['学院', '违纪次数']]
  else if (statType.value === 'type') head = [['类型', '违纪次数']]
  else if (statType.value === 'date') head = [['月份', '违纪次数']]
  const body = statData.value.map(i => [i.name || i.date, i.count])
  doc.autoTable({ head, body })
  doc.save('违纪统计.pdf')
}

onMounted(() => {
  fetchData()
})

watch([statType, dateRange], () => {
  fetchData()
})
</script> 