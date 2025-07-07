<template>
  <div style="padding:24px;">
    <el-card>
      <div style="font-size:18px;font-weight:bold;margin-bottom:16px;">AI智能分析</div>
      <el-tabs v-model="tab">
        <el-tab-pane label="高发时间" name="time">
          <el-radio-group v-model="period" style="margin-bottom:12px;">
            <el-radio-button label="month">按月</el-radio-button>
            <el-radio-button label="weekday">按周</el-radio-button>
          </el-radio-group>
          <div ref="timeChart" style="width:100%;height:400px;"></div>
        </el-tab-pane>
        <el-tab-pane label="高发群体" name="group">
          <div style="display:flex;gap:32px;flex-wrap:wrap;">
            <div style="flex:1;min-width:300px;">
              <div style="font-weight:bold;margin-bottom:8px;">违纪学生TOP10</div>
              <el-table :data="topStudents" size="small">
                <el-table-column prop="name" label="学生姓名" />
                <el-table-column prop="count" label="违纪次数" />
              </el-table>
            </div>
            <div style="flex:1;min-width:300px;">
              <div style="font-weight:bold;margin-bottom:8px;">违纪学号TOP10</div>
              <el-table :data="topStudentIds" size="small">
                <el-table-column prop="name" label="学号" />
                <el-table-column prop="count" label="违纪次数" />
              </el-table>
            </div>
            <div style="flex:1;min-width:300px;">
              <div style="font-weight:bold;margin-bottom:8px;">违纪学院TOP10</div>
              <el-table :data="topColleges" size="small">
                <el-table-column prop="name" label="学院" />
                <el-table-column prop="count" label="违纪次数" />
              </el-table>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="智能预警" name="warning">
          <el-alert v-for="(w, i) in warnings" :key="i" :title="w" type="warning" show-icon style="margin-bottom:8px;" />
          <el-empty v-if="!warnings.length" description="暂无预警" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getHotTime, getHotGroup, getAiWarnings } from '@/api/discipline'
import * as echarts from 'echarts'

const tab = ref('time')
const period = ref('month')
const timeChart = ref(null)
const timeData = ref([])
const topStudents = ref([])
const topStudentIds = ref([])
const topColleges = ref([])
const warnings = ref([])
let chartInstance = null

function fetchTime() {
  getHotTime(period.value).then(res => {
    timeData.value = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
    renderTimeChart()
  })
}
function fetchGroup() {
  getHotGroup().then(res => {
    const d = res.data || res.data.data || {}
    topStudents.value = d.topStudents || []
    topStudentIds.value = d.topStudentIds || []
    topColleges.value = d.topColleges || []
  })
}
function fetchWarnings() {
  getAiWarnings().then(res => {
    warnings.value = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
  })
}
function renderTimeChart() {
  if (!chartInstance) chartInstance = echarts.init(timeChart.value)
  let option = {}
  if (period.value === 'month') {
    option = {
      title: { text: '违纪高发月份' },
      tooltip: {},
      xAxis: { type: 'category', data: timeData.value.map(i => i.date) },
      yAxis: { type: 'value' },
      series: [{ data: timeData.value.map(i => i.count), type: 'bar' }]
    }
  } else {
    option = {
      title: { text: '违纪高发星期' },
      tooltip: {},
      xAxis: { type: 'category', data: timeData.value.map(i => ['','日','一','二','三','四','五','六'][i.name]) },
      yAxis: { type: 'value' },
      series: [{ data: timeData.value.map(i => i.count), type: 'bar' }]
    }
  }
  chartInstance.setOption(option)
}

onMounted(() => {
  fetchTime()
  fetchGroup()
  fetchWarnings()
})
watch(period, fetchTime)
</script> 