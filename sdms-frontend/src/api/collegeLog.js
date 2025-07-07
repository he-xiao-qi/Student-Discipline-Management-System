import request from './request'

// 获取本院操作日志列表
export function getCollegeLogList(params) {
  return request({
    url: '/api/operation-log/list', // 如有专用接口请替换
    method: 'get',
    params
  })
} 