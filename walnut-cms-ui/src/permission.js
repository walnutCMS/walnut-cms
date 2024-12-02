import router from './router'
import store from './store'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { Message } from 'element-ui'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/register']

router.beforeEach(async (to, from, next) => {
  NProgress.start()

  const hasToken = store.state.user.accessToken

  if (hasToken) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      const hasUserInfo = store.state.user.userInfo
      const hasRoles = store.state.user.roles && store.state.user.roles.length > 0
      
      if (hasUserInfo && hasRoles) {
        next()
      } else {
        try {
          // 获取用户信息
          await store.dispatch('user/getUserInfo')
          
          // 生成可访问路由表
          const accessRoutes = store.state.permission.addRoutes
          
          // 动态添加可访问路由表
          accessRoutes.forEach(route => {
            router.addRoute(route)
          })

          // hack方法 确保addRoutes已完成
          next({ ...to, replace: true })
        } catch (error) {
          // 移除 token 并跳转登录页面
          await store.dispatch('user/resetToken')
          Message.error(error.message || '出现错误，请重新登录')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
}) 