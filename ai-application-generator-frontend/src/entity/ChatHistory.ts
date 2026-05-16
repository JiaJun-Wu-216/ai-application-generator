import type {PageRequest} from '@/entity/Common.ts'

/**
 * 对话历史实体类
 */
export interface ChatHistory {
  /**
   * 应用 ID
   */
  appId: string
  /**
   * 创建时间
   */
  createTime: string
  /**
   * 对话历史 ID
   */
  id: string
  /**
   * 是否删除
   */
  isDelete: number
  /**
   * 消息内容
   */
  message: string
  /**
   * 消息类型
   */
  messageType: string
  /**
   * 更新时间
   */
  updateTime: string
  /**
   * 用户 ID
   */
  userId: string
}

/**
 * 对话历史信息查询请求类
 */
export interface ChatHistoryQueryRequest extends PageRequest {
  /**
   * 应用 ID
   */
  appId?: string

  /**
   * 对话历史 ID
   */
  id?: string

  /**
   * 最后一条记录的创建时间，游标查询，用于分页查询，获取早于此时间的记录
   */
  lastCreateTime?: string

  /**
   * 消息内容
   */
  message?: string

  /**
   * 消息类型
   */
  messageType?: string

  /**
   * 用户 iD
   */
  userId?: string
}
