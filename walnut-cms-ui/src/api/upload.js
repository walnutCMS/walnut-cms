import request from '@/utils/request'

/**
 * 上传文件
 * @param {FormData} data 文件表单数据
 * @param {File} data.file 文件
 * @returns {Promise<{
 *   code: number,
 *   message: string,
 *   data: {
 *     url: string,      // 文件访问URL
 *     name: string,     // 文件名称
 *     size: number,     // 文件大小(字节)
 *     mimeType: string  // 文件MIME类型
 *   }
 * }>} 上传结果
 */
export function uploadFile(data) {
  return request({
    url: '/api/file/upload',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
