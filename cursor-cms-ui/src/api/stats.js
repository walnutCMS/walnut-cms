import request from '@/utils/request'

/**
 * 获取统计数据
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: {
 *     articleCount: number,
 *     categoryCount: number,
 *     publishedCount: number,
 *     draftCount: number,
 *     viewCount: number,
 *     todayViewCount: number,
 *     weekData: {
 *       dates: string[],
 *       views: number[],
 *       articles: number[]
 *     }
 *   }
 * }>} 统计数据
 */
export function getStats() {
  return request({
    url: '/api/stats',
    method: 'get'
  })
}


/**
 * 获取最新文章
 * @param {number} limit 获取数量
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: Array<{
 *     id: number,
 *     title: string,
 *     author: string,
 *     createTime: string
 *   }>
 * }>} 文章列表
 */
export function getLatestArticles(limit = 5) {
  return request({
    url: '/api/stats/article/latest',
    method: 'get',
    params: { limit }
  })
}
