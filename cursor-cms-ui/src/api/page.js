import request from '@/utils/request'

/**
 * 获取页面列表
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: Array<{
 *     id: number,
 *     name: string,
 *     path: string,
 *     templateId: number,
 *     createTime: string,
 *     updateTime: string
 *   }>
 * }>} 页面列表数据
 */
export function getPageList() {
  return request({
    url: '/api/page/list',
    method: 'get'
  })
}

/**
 * 获取页面详情
 * @param {number} id 页面ID
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: {
 *     id: number,
 *     name: string,
 *     path: string,
 *     templateId: number,
 *     content: Object,
 *     createTime: string,
 *     updateTime: string
 *   }
 * }>} 页面详情数据
 */
export function getPage(id) {
  return request({
    url: `/api/page/${id}`,
    method: 'get'
  })
}

/**
 * 创建页面
 * @param {Object} data 页面数据
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: {
 *     id: number
 *   }
 * }>} 创建结果
 */
export function createPage(data) {
  return request({
    url: '/api/page',
    method: 'post',
    data
  })
}

/**
 * 更新页面
 * @param {Object} data 页面数据
 * @returns {Promise<{
 *   code: number,
 *   message: string
 * }>} 更新结果
 */
export function updatePage(data) {
  return request({
    url: '/api/page',
    method: 'put',
    data
  })
}

/**
 * 删除页面
 * @param {number} id 页面ID
 * @returns {Promise<{
 *   code: number,
 *   message: string
 * }>} 删除结果
 */
export function deletePage(id) {
  return request({
    url: `/api/page/${id}`,
    method: 'delete'
  })
} 