import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const http = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截：自动附带 JWT
http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截：直接返回 ApiResponse 数据体，统一处理错误
http.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const status = error.response?.status
    const message = error.response?.data?.message || error.message || '请求失败'
    if (status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      if (router.currentRoute.value.name !== 'login') {
        ElMessage.error(message || '登录已过期，请重新登录')
        router.push('/login')
      } else {
        ElMessage.error(message)
      }
    } else {
      ElMessage.error(message)
    }
    return Promise.reject(error)
  }
)

export default http
