import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import NProgress from 'nprogress'

// 创建 axios 实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 5000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    'X-Requested-With': 'XMLHttpRequest'
  }
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    NProgress.start()
    const accessToken = store.state.user.accessToken
    const tokenType = store.state.user.tokenType
    if (accessToken && tokenType) {
      config.headers['Authorization'] = `${tokenType} ${accessToken}`
    }
    return config
  },
  error => {
    NProgress.done()
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    NProgress.done()
    return response.data
  },
  error => {
    NProgress.done()
    console.error('Response error:', error)
    Message({
      message: error.message || '请求失败',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service 