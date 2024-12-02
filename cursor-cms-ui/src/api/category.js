import request from '@/utils/request'

/**
 * 获取分类列表
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: Array<{
 *     id: number,
 *     name: string,
 *     parentId: number,
 *     children: Array,
 *     articleCount: number,
 *     createTime: string
 *   }>
 * }>} 分类树形数据
 */
export function getCategoryList(params) {
  return request({
    url: '/api/category/list',
    method: 'get',
    params
  })
}

/**
 * 获取分类详情
 * @param {number} id 分类ID
 * @returns {Promise} 分类详情数据
 */
export function getCategory(id) {
  return request({
    url: `/api/category/${id}`,
    method: 'get'
  })
}

/**
 * 创建分类
 * @param {Object} data 分类数据
 * @param {string} data.name 分类名称
 * @param {number} data.parentId 父分类ID
 * @returns {Promise} 创建结果
 */
export function createCategory(data) {
  return request({
    url: '/api/category',
    method: 'post',
    data
  })
}

/**
 * 更新分类
 * @param {number} id 分类ID
 * @param {Object} data 分类数据
 * @param {string} data.name 分类名称
 * @param {number} data.parentId 父分类ID
 * @returns {Promise} 更新结果
 */
export function updateCategory(id, data) {
  return request({
    url: `/api/category/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除分类
 * @param {number} id 分类ID
 * @returns {Promise} 删除结果
 */
export function deleteCategory(id) {
  return request({
    url: `/api/category/${id}`,
    method: 'delete'
  })
}
