import request from '@/utils/request'

/**
 * 获取文章列表
 * @param {Object} params 查询参数
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页数量
 * @param {string} [params.keyword] 关键词
 * @param {number} [params.categoryId] 分类ID
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: {
 *     list: Array<{
 *       id: number,
 *       title: string,
 *       summary: string,
 *       content: string,
 *       categoryId: number,
 *       categoryName: string,
 *       viewCount: number,
 *       createTime: string,
 *       updateTime: string
 *     }>,
 *     total: number
 *   }
 * }>} 文章列表数据
 */
export function getArticleList(params) {
  return request({
    url: '/api/article/list',
    method: 'get',
    params
  })
}

/**
 * 获取文章详情
 * @param {number} id 文章ID
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: {
 *     id: number,
 *     title: string,
 *     summary: string,
 *     content: string,
 *     categoryId: number,
 *     categoryName: string,
 *     viewCount: number,
 *     createTime: string,
 *     updateTime: string
 *   }
 * }>} 文章详情数据
 */
export function getArticle(id) {
  return request({
    url: `/api/article/${id}`,
    method: 'get'
  })
}

/**
 * 创建文章
 * @param {Object} data 文章数据
 * @param {string} data.title 标题
 * @param {string} data.summary 摘要
 * @param {string} data.content 内容
 * @param {number} data.categoryId 分类ID
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: {
 *     id: number
 *   }
 * }>} 创建结果
 */
export function createArticle(data) {
  return request({
    url: '/api/article',
    method: 'post',
    data
  })
}

/**
 * 更新文章
 * @param {number} id 文章ID
 * @param {Object} data 文章数据
 * @param {string} data.title 标题
 * @param {string} data.summary 摘要
 * @param {string} data.content 内容
 * @param {number} data.categoryId 分类ID
 * @returns {Promise<{
 *   code: number,
 *   message: string
 * }>} 更新结果
 */
export function updateArticle(id, data) {
  return request({
    url: `/api/article/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除文章
 * @param {number} id 文章ID
 * @returns {Promise<{
 *   code: number,
 *   message: string
 * }>} 删除结果
 */
export function deleteArticle(id) {
  return request({
    url: `/api/article/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除文章
 * @param {Array<number|string>} ids 文章ID数组
 * @returns {Promise<{
 *   code: number,
 *   message: string
 * }>} 删除结果
 */
export function batchDeleteArticles(ids) {
  return request({
    url: '/api/article/batch',
    method: 'delete',
    data: { ids }
  })
}

/**
 * 批量发布文章
 * @param {Array<number|string>} ids 文章ID数组
 * @returns {Promise<{
 *   code: number,
 *   message: string
 * }>} 发布结果
 */
export function batchPublishArticles(ids) {
  return request({
    url: '/api/article/batch/publish',
    method: 'post',
    data: { ids }
  })
}

/**
 * 发布文章
 * @param {string|number} id 文章ID
 * @returns {Promise} 发布结果
 */
export function publishArticle(id) {
  return request({
    url: `/api/article/publish/${id}`,
    method: 'put'
  })
}

/**
 * 下线文章
 * @param {string|number} id 文章ID
 * @returns {Promise} 下线结果
 */
export function unpublishArticle(id) {
  return request({
    url: `/api/article/unpublish/${id}`,
    method: 'put'
  })
}
