import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => {
    // 初始化时从localStorage恢复
    let user = {}
    try {
      user = JSON.parse(localStorage.getItem('user') || '{}')
    } catch (e) {}
    return {
      id: user.id || '',
      username: user.username || '',
      role: user.role || '', // admin/college
      college: user.college || (user.colleges ? (Array.isArray(user.colleges) ? user.colleges[0] : (typeof user.colleges === 'string' ? user.colleges.split(',')[0] : '')) : '')
    }
  },
  actions: {
    setUser(user) {
      this.id = user.id
      this.username = user.username
      this.role = user.role
      this.college = user.college || (user.colleges ? (Array.isArray(user.colleges) ? user.colleges[0] : (typeof user.colleges === 'string' ? user.colleges.split(',')[0] : '')) : '')
      // 持久化到localStorage
      localStorage.setItem('user', JSON.stringify({
        id: this.id,
        username: this.username,
        role: this.role,
        college: this.college
      }))
    }
  }
}) 