import request from './request'

// 获取学院列表
export function getCollegeList(params) {
  return request({
    url: '/api/college/list', // 如有专用接口请替换
    method: 'get',
    params
  })
}

// 新增/编辑学院
export function saveCollege(data) {
  return request({
    url: '/api/college/save',
    method: 'post',
    data
  })
}

// 删除学院
export function deleteCollege(id) {
  return request({
    url: `/api/college/delete/${id}`,
    method: 'delete'
  })
} 