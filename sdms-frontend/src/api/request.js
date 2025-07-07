import axios from 'axios'

const service = axios.create({
  baseURL: '', // 可根据需要设置基础路径
  timeout: 5000
})

// 请求拦截器（可选）
service.interceptors.request.use(
  config => {
    // 可在此处添加token等
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器（可选）
service.interceptors.response.use(
  response => response.data,
  error => Promise.reject(error)
)

export default service 