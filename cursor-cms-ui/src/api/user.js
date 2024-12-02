import request from '@/utils/request'

/**
 * 获取验证码
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: {
 *     image: string,  // base64格式的图片
 *     key: string     // 验证码key
 *   }
 * }>} 验证码数据
 */
export function getCaptcha() {
    return request({
        url: '/api/captcha',
        method: 'get'
    })
}

/**
 * 用户注册
 * @param {Object} data 注册信息
 * @param {string} data.username 用户名
 * @param {string} data.nickname 昵称
 * @param {string} data.password 密码
 * @param {string} data.captcha 验证码
 * @param {string} data.captchaKey 验证码key
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: {
 *     id: number
 *   }
 * }>} 注册结果
 */
export function register(data) {
    return request({
        url: '/api/user/register',
        method: 'post',
        data
    })
}

/**
 * 用户登录
 * @param {Object} data 登录信息
 * @param {string} data.username 用户名
 * @param {string} data.password 密码
 * @param {string} data.captcha 验证码
 * @param {string} data.captchaKey 验证码key
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: {
 *     token: string,
 *     userInfo: {
 *       id: number,
 *       username: string,
 *       nickname: string,
 *       avatar: string,
 *       role: string
 *     }
 *   }
 * }>} 登录结果
 */
export function login(data) {
    return request({
        url: '/api/user/login',
        method: 'post',
        data
    })
}

/**
 * 获取用户信息
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: {
 *     id: number,
 *     username: string,
 *     nickname: string,
 *     avatar: string,
 *     role: string,
 *     createTime: string
 *   }
 * }>} 用户信息
 */
export function getUserInfo() {
    return request({
        url: '/api/user/info',
        method: 'get'
    })
}

/**
 * 更新用户信息
 * @param {Object} data 用户信息
 * @param {string} [data.nickname] 昵称
 * @param {string} [data.avatar] 头像URL
 * @returns {Promise<{
 *   code: number,
 *   message: string
 * }>} 更新结果
 */
export function updateUserInfo(data) {
    return request({
        url: '/api/user/info',
        method: 'put',
        data
    })
}

/**
 * 修改密码
 * @param {Object} data 密码信息
 * @param {string} data.oldPassword 旧密码
 * @param {string} data.newPassword 新密码
 * @returns {Promise<{
 *   code: number,
 *   message: string
 * }>} 修改结果
 */
export function updatePassword(data) {
    return request({
        url: '/api/user/password',
        method: 'put',
        data
    })
}


/**
 * 更新用户密码
 * @param {Object} data 密码信息
 * @param {string} data.oldPassword 旧密码
 * @param {string} data.newPassword 新密码
 * @returns {Promise<{
 *   code: number,
 *   message: string
 * }>} 更新结果
 */
export function updateUserPassword(data) {
    return request({
        url: '/user/password',
        method: 'put',
        data
    })
}

/**
 * 退出登录
 * @returns {Promise<{
 *   code: number,
 *   message: string
 * }>} 退出结果
 */
export function logout() {
    return request({
        url: '/api/user/logout',
        method: 'post'
    })
}

