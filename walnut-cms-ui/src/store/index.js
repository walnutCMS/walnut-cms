import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import app from './modules/app'
import user from './modules/user'
import permission from './modules/permission'
import tagsView from './modules/tagsView'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    user,
    permission,
    tagsView
  },
  getters,
  plugins: [
    createPersistedState({
      paths: [
        'app.sidebar',
        'app.device',
        'app.theme',
        'user.accessToken',
        'user.refreshToken',
        'user.tokenType',
        'user.expiresIn',
        'user.userInfo',
        'permission.routes',
        'permission.addRoutes',
        'tagsView.visitedViews'
      ]
    })
  ]
})

export default store