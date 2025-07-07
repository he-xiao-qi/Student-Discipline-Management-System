import axios from 'axios'

export function getPunishmentTypeList() {
  return axios.get('/api/punishment-type/list')
}

export function savePunishmentType(type) {
  return axios.post('/api/punishment-type/save', type)
}

export function deletePunishmentType(id) {
  return axios.delete(`/api/punishment-type/delete/${id}`)
} 