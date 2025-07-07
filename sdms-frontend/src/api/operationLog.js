import axios from 'axios'

export function getOperationLogList() {
  return axios.get('/api/operation-log/list')
}

export function saveOperationLog(log) {
  return axios.post('/api/operation-log/save', log)
}

export function deleteOperationLog(id) {
  return axios.delete(`/api/operation-log/delete/${id}`)
} 