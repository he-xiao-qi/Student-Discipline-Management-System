<template>
  <div class="login-container">
    <el-form :model="form" @submit.prevent="onLogin">
      <h2 style="text-align:center;margin-bottom:24px;">学生违纪处分系统</h2>
      <el-form-item label="用户名" required>
        <el-input v-model="form.username" autocomplete="username" />
      </el-form-item>
      <el-form-item label="密码" required>
        <el-input v-model="form.password" type="password" autocomplete="current-password" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" style="width:100%;" @click="onLogin">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/user'
import { useUserStore } from '@/store/user'

const form = ref({ username: '', password: '' })
const router = useRouter()
const userStore = useUserStore()

function onLogin() {
  if (!form.value.username || !form.value.password) {
    ElMessage.error('请输入用户名和密码')
    return
  }
  login(form.value).then(res => {
    if (res.data && (res.data.code === 200 || res.data.code === 0)) {
      localStorage.setItem('user', JSON.stringify(res.data.data))
      const roles = res.data.data.roles
      if (roles && (Array.isArray(roles) ? roles.includes('admin') : roles.includes('admin'))) {
        router.push('/admin')
      } else if (roles && (Array.isArray(roles) ? roles.includes('college') : roles.includes('college'))) {
        router.push('/college')
      } else {
        router.push('/')
      }
      userStore.college = ''
      if (res.data.data.colleges) {
        if (Array.isArray(res.data.data.colleges)) {
          userStore.college = res.data.data.colleges[0]
        } else if (typeof res.data.data.colleges === 'string') {
          userStore.college = res.data.data.colleges.split(',')[0]
        }
      } else if (res.data.data.college) {
        userStore.college = res.data.data.college
      }
      console.log('登录返回数据:', res.data.data)
      console.log('保存时userId:', userStore.id)
    } else {
      ElMessage.error(res.data?.msg || '登录失败')
    }
  }).catch(() => {
    ElMessage.error('登录失败')
  })
}
</script>

<style scoped>
.login-container {
  width: 350px;
  margin: 120px auto;
  padding: 32px 32px 24px 32px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
}
</style> 