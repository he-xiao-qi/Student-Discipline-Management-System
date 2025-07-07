<template>
  <el-container>
    <el-aside width="200px" style="background:#206fd7;min-height:100vh;">
      <div style="color:#fff;font-size:20px;font-weight:bold;padding:24px 0 24px 24px;display:flex;align-items:center;">
        <img src="/image/sdms.png" style="width:32px;height:32px;margin-right:8px;" />学生违纪处分
      </div>
      <el-menu :default-active="activeMenu" class="el-menu-vertical-demo" background-color="#206fd7" text-color="#fff" active-text-color="#ffd04b" router>
        <el-menu-item index="/college">本院违纪处分</el-menu-item>
        <el-menu-item index="/college/log">操作日志</el-menu-item>
      </el-menu>
      <div style="position:absolute;bottom:24px;left:24px;color:#fff;font-size:18px;font-weight:bold;">学院角色</div>
    </el-aside>
    <el-container>
      <el-header style="background:#fff;display:flex;align-items:center;justify-content:space-between;">
        <div style="display:flex;align-items:center;">
          <el-avatar size="large" style="margin-right:8px;">ADMIN</el-avatar>
          <span style="font-weight:bold;">(学院用户)</span>
        </div>
        <el-link type="primary" @click="goToLogin">返回登录</el-link>
      </el-header>
      <el-main style="background:#f6f6f8;min-height:calc(100vh - 64px);">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getDisciplineList, getDisciplineStat, getDisciplineStatByType } from '@/api/discipline'
import * as echarts from 'echarts'

const activeMenu = ref('/college')
const router = useRouter()
const userStore = useUserStore()

function goToLogin() {
  router.push('/')
}

// 统计卡片数据
const stat = reactive({
  monthCount: 0,
  unhandledCount: 0
})
// 饼图数据
const typePieData = ref([])
const typePieChartRef = ref(null)
// 最近5条记录
const recentRecords = ref([])

function onAddDiscipline() {
  router.push('/college/discipline')
}

async function fetchStat() {
  console.log('统计前学院:', userStore.college)
  const res = await getDisciplineStat({ college: userStore.college })
  if (Array.isArray(res.data)) {
    stat.monthCount = res.data[0]?.monthCount || 0
    stat.unhandledCount = res.data[0]?.unhandledCount || 0
  }
}

async function fetchTypePie() {
  console.log('类型统计前学院:', userStore.college)
  const res = await getDisciplineStatByType({ college: userStore.college })
  if (Array.isArray(res.data)) {
    typePieData.value = res.data.map(item => ({ value: item.count, name: item.name }))
    await nextTick()
    if (typePieChartRef.value) {
      const chart = echarts.init(typePieChartRef.value)
      chart.setOption({
        title: { text: '违纪类型分布', left: 'center' },
        tooltip: { trigger: 'item' },
        legend: { bottom: 10, left: 'center' },
        series: [{
          name: '类型',
          type: 'pie',
          radius: '50%',
          data: typePieData.value
        }]
      })
    }
  }
}

function formatDate(row) {
  if (!row.date) return ''
  // 只显示yyyy-MM-dd
  return typeof row.date === 'string' ? row.date.split('T')[0] : row.date
}

async function fetchRecentRecords() {
  console.log('当前用户学院:', userStore.college)
  const res = await getDisciplineList({ college: userStore.college })
  let arr = Array.isArray(res.data) ? res.data : (res.data && Array.isArray(res.data.data) ? res.data.data : [])
  arr = arr.map(item => ({
    studentName: item.studentName || item.student_name,
    studentId: item.studentId || item.student_id,
    type: item.type,
    date: item.date,
    punishment: item.punishment
  }))
  recentRecords.value = arr.slice(0, 5)
}

onMounted(() => {
  fetchStat()
  fetchTypePie()
  fetchRecentRecords()
})
</script> 