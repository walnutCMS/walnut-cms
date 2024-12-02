import { login, register, getUserInfo, updateUserInfo, updatePassword, logout, updateUserAvatar } from '@/api/user'

const state = {
    accessToken: localStorage.getItem('accessToken') || '',
    refreshToken: localStorage.getItem('refreshToken') || '',
    tokenType: localStorage.getItem('tokenType') || 'Bearer',
    expiresIn: localStorage.getItem('expiresIn') || 0,
    userInfo: JSON.parse(localStorage.getItem('userInfo')) || null,
    roles: []
}

const mutations = {
    SET_TOKEN: (state, tokenData) => {
        const { accessToken, refreshToken, tokenType, expiresIn } = tokenData
        state.accessToken = accessToken
        state.refreshToken = refreshToken
        state.tokenType = tokenType
        state.expiresIn = expiresIn
        localStorage.setItem('accessToken', accessToken)
        localStorage.setItem('refreshToken', refreshToken)
        localStorage.setItem('tokenType', tokenType)
        localStorage.setItem('expiresIn', expiresIn)
    },
    SET_USER_INFO: (state, userInfo) => {
        state.userInfo = userInfo
        state.roles = [userInfo.role]
        localStorage.setItem('userInfo', JSON.stringify(userInfo))
    },
    CLEAR_USER: (state) => {
        state.accessToken = ''
        state.refreshToken = ''
        state.tokenType = 'Bearer'
        state.expiresIn = 0
        state.userInfo = null
        state.roles = []
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
        localStorage.removeItem('tokenType')
        localStorage.removeItem('expiresIn')
        localStorage.removeItem('userInfo')
    }
}

const actions = {
    // 用户登录
    login({ commit, dispatch }, userInfo) {
        return new Promise((resolve, reject) => {
            login(userInfo).then(async response => {
                const { code, data } = response
                if (code === 200) {
                    const { accessToken, refreshToken, tokenType, expiresIn, userId, username, nickname, avatar, email, phone, role } = data
                    // 1. 保存token
                    commit('SET_TOKEN', { accessToken, refreshToken, tokenType, expiresIn })
                    // 2. 保存用户信息
                    const userInfo = { id: userId, username, nickname, avatar, email, phone, role }
                    commit('SET_USER_INFO', userInfo)
                    // 3. 生成权限路由
                    await dispatch('permission/generateRoutes', [role], { root: true })
                    resolve(data)
                } else {
                    reject(new Error(response.message))
                }
            }).catch(error => {
                reject(error)
            })
        })
    },

    // 获取用户信息
    getUserInfo({ commit, dispatch }) {
        return new Promise((resolve, reject) => {
            getUserInfo().then(async response => {
                const { code, data } = response
                if (code === 200) {
                    if (!data) {
                        reject(new Error('获取用户信息失败，请重新登录'))
                        return
                    }
                    // 1. 保存用户信息
                    commit('SET_USER_INFO', data)
                    // 2. 生成权限路由
                    await dispatch('permission/generateRoutes', [data.role], { root: true })
                    resolve(data)
                } else {
                    reject(new Error(response.message))
                }
            }).catch(error => {
                reject(error)
            })
        })
    },

    // 退出登录
    logout({ commit, dispatch }) {
        return new Promise((resolve, reject) => {
            logout().then(() => {
                commit('CLEAR_USER')
                // 重置路由
                dispatch('permission/resetRoutes', null, { root: true })
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    // 重置token
    resetToken({ commit }) {
        return new Promise(resolve => {
            commit('CLEAR_USER')
            resolve()
        })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
