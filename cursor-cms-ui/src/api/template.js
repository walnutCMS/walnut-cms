import request from '@/utils/request'

/**
 * 获取模板列表
 * @param {Object} params 查询参数
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页数量
 * @param {string} [params.keyword] 关键词
 * @param {string} [params.type] 模板类型
 * @returns {Promise} 模板列表数据
 */
export function getTemplateList(params) {
  return request({
    url: '/api/template/list',
    method: 'get',
    params
  })
}

/**
 * 获取模板详情
 * @param {number} id 模板ID
 * @returns {Promise} 模板详情数据
 */
export function getTemplate(id) {
  return request({
    url: `/api/template/${id}`,
    method: 'get'
  })
}

/**
 * 创建模板
 * @param {Object} data 模板数据
 * @returns {Promise} 创建结果
 */
export function createTemplate(data) {
  return request({
    url: '/api/template',
    method: 'post',
    data
  })
}

/**
 * 更新模板
 * @param {number} id 模板ID
 * @param {Object} data 模板数据
 * @returns {Promise} 更新结果
 */
export function updateTemplate(id, data) {
  return request({
    url: `/api/template/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除模板
 * @param {number} id 模板ID
 * @returns {Promise} 删除结果
 */
export function deleteTemplate(id) {
  return request({
    url: `/api/template/${id}`,
    method: 'delete'
  })
}

/**
 * 应用模板
 * @param {Object} data 应用数据
 * @param {number} data.templateId 模板ID
 * @param {number} data.pageId 页面ID
 * @returns {Promise} 应用结果
 */
export function applyTemplate(data) {
  return request({
    url: '/api/template/apply',
    method: 'post',
    data
  })
}

/**
 * 获取模板预览数据
 * @param {number} id 模板ID
 * @param {Object} data 预览数据
 * @returns {Promise} 预览结果
 */
export function previewTemplate(id, data) {
  return request({
    url: `/api/template/${id}/preview`,
    method: 'post',
    data
  })
}

/**
 * 获取模板配置
 * @param {number} id 模板ID
 * @returns {Promise} 模板配置数据
 */
export function getTemplateConfig(id) {
  return request({
    url: `/api/template/${id}/config`,
    method: 'get'
  })
}

/**
 * 更新模板配置
 * @param {number} id 模板ID
 * @param {Object} data 模板配置数据
 * @returns {Promise} 更新结果
 */
export function updateTemplateConfig(id, data) {
  return request({
    url: `/api/template/${id}/config`,
    method: 'put',
    data
  })
}
