import axios from 'axios'
import request from './request'

// 分页获取用户列表，支持多条件
export function getUserPage(params) {
  return axios.get('/api/user/page', { params })
}

// 兼容原有获取全部用户（不分页）
export function getUserList(params) {
  return axios.get('/api/user/list', { params })
}

export function saveUser(user) {
  return axios.post('/api/user/save', user)
}

export function deleteUser(id) {
  return axios.delete(`/api/user/delete/${id}`)
}

export function login(data) {
  // 直接返回axios Promise，便于res.data.code判断
  return axios.post('/api/auth/login', data)
} 