import axios from 'axios'
import request from './request'

export function getDisciplineList(params) {
  return axios.get('/api/discipline/list', { params })
}

// 分页获取违纪记录
export function getDisciplinePage(params) {
  return axios.get('/api/discipline/page', { params })
}

export function saveDiscipline(record) {
  return axios.post('/api/discipline/save', record)
}

export function deleteDiscipline(id) {
  return axios.delete(`/api/discipline/delete/${id}`)
}

// 获取全校违纪统计
export function getDisciplineStat(params) {
  return request({
    url: '/api/discipline/stat', // 改为和后端一致
    method: 'get',
    params
  })
}

// 获取按类型统计
export function getDisciplineStatByType(params) {
  return request({
    url: '/api/discipline/stat/type',
    method: 'get',
    params
  })
}

// 获取按时间段统计
export function getDisciplineStatByDate(params) {
  return request({
    url: '/api/discipline/stat/date',
    method: 'get',
    params
  })
}

// 批量导入违纪记录
export function importDisciplineBatch(data) {
  return request({
    url: '/api/discipline/import',
    method: 'post',
    data
  })
}

// 批量删除违纪记录
export function batchDeleteDiscipline(ids) {
  return request({
    url: '/api/discipline/batchDelete',
    method: 'post',
    data: ids
  })
}

// 智能分析：高发时间
export function getHotTime(period) {
  return request({
    url: '/api/discipline/ai/hotTime',
    method: 'get',
    params: { period }
  })
}

// 智能分析：高发群体
export function getHotGroup() {
  return request({
    url: '/api/discipline/ai/hotGroup',
    method: 'get'
  })
}

// 智能分析：预警
export function getAiWarnings() {
  return request({
    url: '/api/discipline/ai/warning',
    method: 'get'
  })
} 