<template>
  <el-container>
    <el-aside width="200px" style="background:#206fd7;min-height:100vh;">
      <div style="color:#fff;font-size:20px;font-weight:bold;padding:24px 0 24px 24px;display:flex;align-items:center;">
        <img src="/image/sdms.png" style="width:32px;height:32px;margin-right:8px;" />学生违纪处分
      </div>
      <el-menu :default-active="activeMenu" class="el-menu-vertical-demo" background-color="#206fd7" text-color="#fff" active-text-color="#ffd04b" router>
        <el-menu-item index="/admin">全校违纪处分</el-menu-item>
        <el-menu-item index="/admin/stat">全校违纪统计</el-menu-item>
        <el-menu-item index="/admin/ai">AI智能分析</el-menu-item>
        <el-menu-item index="/admin/setting">违纪设置</el-menu-item>
        <el-menu-item index="/admin/log">操作日志</el-menu-item>
        <el-menu-item index="/admin/member">成员管理</el-menu-item>
        <el-menu-item index="/admin/college">学院管理</el-menu-item>
      </el-menu>
      <div style="position:absolute;bottom:24px;left:24px;color:#fff;font-size:18px;font-weight:bold;">系统管理员</div>
    </el-aside>
    <el-container>
      <el-header style="background:#fff;display:flex;align-items:center;justify-content:space-between;">
        <div style="display:flex;align-items:center;">
          <el-avatar size="32" style="margin-right:8px;">ADMIN</el-avatar>
          <span style="font-weight:bold;">(系统管理员)</span>
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
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const activeMenu = ref('/admin')

function goToLogin() {
  router.push('/')
}
</script> 