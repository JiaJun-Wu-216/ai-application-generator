/**
 * 用户注册请求类
 */
export interface UserRegisterRequest {
  /**
   * 确认密码
   */
  checkPassword: string

  /**
   * 账号
   */
  userAccount: string

  /**
   * 密码
   */
  userPassword: string
}

/**
 * 登陆用户视图类
 */
export interface LoginUserVO {
  /**
   * 账户状态 ，0：封禁 1：正常
   */
  accountStatus: number

  /**
   * 创建时间
   */
  createTime: string

  /**
   * 用户 ID
   */
  id: string

  /**
   * 更新时间
   */
  updateTime: string

  /**
   * 账号
   */
  userAccount: string

  /**
   * 用户头像
   */
  userAvatar: string

  /**
   * 用户昵称
   */
  userName: string

  /**
   * 用户简介
   */
  userProfile: string

  /**
   * 用户角色，user/admin
   */
  userRole: string
}

/**
 * 用户上传请求类
 */
export interface UserUpdateRequest {
  /**
   * id
   */
  id: string

  /**
   * 用户头像
   */
  userAvatar: string

  /**
   * 用户昵称
   */
  userName: string

  /**
   * 简介
   */
  userProfile: null | string

  /**
   * 用户角色，user/admin
   */
  userRole: string
}

/**
 * 用户实体类
 */
export interface User {
  /**
   * 账户状态 ，0：封禁 1：正常
   */
  accountStatus: number

  /**
   * 创建时间
   */
  createTime: string

  /**
   * 编辑时间
   */
  editTime: string

  /**
   * id
   */
  id: string

  /**
   * 是否删除
   */
  isDelete: number

  /**
   * 更新时间
   */
  updateTime: string

  /**
   * 账号
   */
  userAccount: string

  /**
   * 用户头像
   */
  userAvatar: string

  /**
   * 用户昵称
   */
  userName: string

  /**
   * 密码
   */
  userPassword: string

  /**
   * 用户简介
   */
  userProfile: string

  /**
   * 用户角色，user/admin
   */
  userRole: string
}

/**
 * 新增用户请求类
 */
export interface UserAddRequest {
  /**
   * 账号
   */
  userAccount: string

  /**
   * 用户头像
   */
  userAvatar: string

  /**
   * 用户昵称
   */
  userName: string

  /**
   * 用户简介
   */
  userProfile?: string

  /**
   * 用户角色，user, admin
   */
  userRole: string
}

/**
 * 用户视图类
 */
export interface UserVO {
  /**
   * 账户状态
   */
  accountStatus?: number

  /**
   * 创建时间
   */
  createTime?: string

  /**
   * id
   */
  id?: string

  /**
   * 账户状态描述
   */
  statusDescription?: string

  /**
   * 账号
   */
  userAccount?: string

  /**
   * 用户头像
   */
  userAvatar?: string

  /**
   * 用户昵称
   */
  userName?: string

  /**
   * 用户简介
   */
  userProfile?: string

  /**
   * 用户角色，user/admin
   */
  userRole?: string
}

/**
 * 用户登陆请求类
 */
export interface UserLoginRequest {
  /**
   * 账号
   */
  userAccount: string

  /**
   * 密码
   */
  userPassword: string
}

/**
 * 用户查询请求类
 */
export interface UserQueryRequest {
  /**
   * 当前页码
   */
  current: number

  /**
   * id
   */
  id?: string

  /**
   * 每页展示数量
   */
  pageSize: number

  /**
   * 排序字段
   */
  sortField?: string

  /**
   * 排序顺序，默认降序
   */
  sortOrder?: string

  /**
   * 账号
   */
  userAccount?: string

  /**
   * 用户昵称
   */
  userName?: string

  /**
   * 简介
   */
  userProfile?: string

  /**
   * 用户角色，user/admin
   */
  userRole?: string
}
