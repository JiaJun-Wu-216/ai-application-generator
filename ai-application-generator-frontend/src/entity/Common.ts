/**
 * 全局响应封装类
 */
export interface BaseResponse<T> {
  code?: number
  data?: T
  message?: string
}

/**
 * 通用分页请求类
 */
export interface PageRequest {
  current?: number
  pageSize?: number
  sortField?: string
  sortOrder?: string
}

/**
 * 通用删除请求类
 */
export interface DeleteRequest {
  id: string
}
