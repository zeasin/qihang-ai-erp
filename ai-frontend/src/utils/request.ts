import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const request = axios.create({
  baseURL: '/api',
  timeout: 60000,
})

// 请求拦截器 — 自动携带 token
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器 — 统一错误处理
request.interceptors.response.use(
  (response) => {
    const res = response.data as any
    if (res.code && res.code !== 200) {
      // 401 且不是登录接口 → 跳转登录
      if (res.code === 401 && !response.config.url?.includes('/login')) {
        localStorage.removeItem('token')
        router.push('/login')
      }
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
    return res
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      router.push('/login')
    }
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request
