import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import AdminHome from '@/views/Admin/AdminHome.vue'
import MemberManage from '../views/Admin/MemberManage.vue'
import DisciplineList from '../views/Admin/DisciplineList.vue'
import DisciplineStat from '../views/Admin/DisciplineStat.vue'
import DisciplineSetting from '../views/Admin/DisciplineSetting.vue'
import OperationLog from '../views/Admin/OperationLog.vue'
import CollegeHome from '../views/College/CollegeHome.vue'
import CollegeDiscipline from '../views/College/CollegeDiscipline.vue'
import CollegeLog from '../views/College/CollegeLog.vue'
import AdminCollegeManage from '../views/Admin/AdminCollegeManage.vue'
import AiAnalysis from '../views/Admin/AiAnalysis.vue'

const routes = [
  { path: '/login', component: Login },
  {
    path: '/admin',
    component: AdminHome,
    children: [
      { path: '', component: DisciplineList },
      { path: 'discipline', component: DisciplineList },
      { path: 'stat', component: DisciplineStat },
      { path: 'ai', component: AiAnalysis },
      { path: 'setting', component: DisciplineSetting },
      { path: 'log', component: OperationLog },
      { path: 'member', component: MemberManage },
      { path: 'college', component: AdminCollegeManage }
    ]
  },
  {
    path: '/college',
    component: CollegeHome,
    children: [
      { path: '', component: CollegeDiscipline },
      { path: 'log', component: CollegeLog }
    ]
  },
  { path: '/', redirect: '/login' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：未登录强制跳转/login
router.beforeEach((to, from, next) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.username && to.path !== '/login') {
    next('/login')
  } else if (to.path.startsWith('/admin') && !(user.roles && (Array.isArray(user.roles) ? user.roles.includes('admin') : user.roles.includes('admin')))) {
    next('/login')
  } else if (to.path.startsWith('/college') && !(user.roles && (Array.isArray(user.roles) ? user.roles.includes('college') : user.roles.includes('college')))) {
    next('/login')
  } else {
    next()
  }
})

export default router 