import type {PageRequest} from '@/entity/Common.ts'
import type {UserVO} from '@/entity/User.ts'

/**
 * 添加应用请求类
 */
export interface AppAddRequest {
  /**
   * 应用初始化的 prompt
   */
  initPrompt: string
}

/**
 * 管理员应用更新请求类
 */
export interface AppAdminUpdateRequest {
  /**
   * 应用名称
   */
  appName?: string
  /**
   * 应用封面
   */
  cover?: string
  /**
   * 应用 ID
   */
  id: string
  /**
   * 优先级
   */
  priority: number
}

/**
 * 应用部署请求类
 */
export interface AppDeployRequest {
  /**
   * 应用 ID
   */
  appId: string
}

/**
 * 应用查询请求类
 */
export interface AppQueryRequest extends PageRequest {
  /**
   * 应用名称
   */
  appName?: string
  /**
   * 代码生成类型（枚举）
   */
  codeGenType?: string
  /**
   * 应用封面
   */
  cover?: string
  /**
   * 部署标识
   */
  deployKey?: string
  /**
   * 应用 ID
   */
  id?: string
  /**
   * 应用初始化的 prompt
   */
  initPrompt?: string
  /**
   * 优先级
   */
  priority?: string
  /**
   * 创建用户 ID
   */
  userId?: string
}

/**
 * 应用更新请求类
 */
export interface AppUpdateRequest {
  /**
   * 应用名称
   */
  appName: string
  /**
   * 应用 ID
   */
  id: string
}

/**
 * 应用视图类
 */
export interface AppVO {
  /**
   * 应用名称
   */
  appName: string
  /**
   * 代码生成类型（枚举）
   */
  codeGenType: string
  /**
   * 应用封面
   */
  cover: string
  /**
   * 创建时间
   */
  createTime: string
  /**
   * 部署时间
   */
  deployedTime: string
  /**
   * 部署标识
   */
  deployKey: string
  /**
   * 应用 ID
   */
  id: string
  /**
   * 应用初始化的 prompt
   */
  initPrompt: string
  /**
   * 优先级
   */
  priority: number
  /**
   * 更新时间
   */
  updateTime: string
  /**
   * 创建用户信息
   */
  user: UserVO
  /**
   * 创建用户 ID
   */
  userId: string
}
