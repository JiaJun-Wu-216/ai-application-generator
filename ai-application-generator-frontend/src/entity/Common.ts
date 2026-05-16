/**
 * 全局响应封装类
 */
export interface BaseResponse<T> {
  /**
   * 响应状态码
   */
  code: number

  /**
   * 响应信息
   */
  message: string

  /**
   * 响应数据
   */
  data: T
}

/**
 * 通用分页请求类
 */
export interface PageRequest {
  /**
   * 当前页码
   */
  pageNum: number

  /**
   * 每页展示数量
   */
  pageSize: number

  /**
   * 排序字段
   */
  sortField?: string

  /**
   * 排序类型（升序或降序）
   */
  sortOrder?: string
}

/**
 * 通用删除请求类
 */
export interface DeleteRequest {
  /**
   * 需要删除数据的主键
   */
  id: string
}

/**
 * 通用分页信息类
 */
export interface Page<T> {
  /**
   * 每页数据数量最大限制
   */
  maxPageSize?: number

  /**
   * 是否优化分页查询 COUNT 语句
   */
  optimizeCountQuery?: boolean

  /**
   * 当前页码
   */
  pageNumber: string

  /**
   * 每页数据数量
   */
  pageSize: string

  /**
   * 当前页数据
   */
  records?: T[]

  /**
   * 总页数
   */
  totalPage: string

  /**
   * 总数据数量
   */
  totalRow: string
}
