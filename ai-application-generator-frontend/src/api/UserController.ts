import request from '@/request'
import type {
  LoginUserVO,
  User,
  UserAddRequest,
  UserLoginRequest,
  UserQueryRequest,
  UserRegisterRequest,
  UserUpdateRequest,
  UserVO,
} from '@/entity/User.ts'
import type {BaseResponse, DeleteRequest, Page} from '@/entity/Common.ts'

/**
 * 请求前缀
 */
const requestPrefix = '/user'

/**
 * 用户注册
 *
 * @param userRegisterRequest 用户注册请求信息
 * @return 新用户 ID
 */
export const register = (
  userRegisterRequest: UserRegisterRequest,
): Promise<BaseResponse<string>> => {
  return request.post(requestPrefix + '/register', userRegisterRequest)
}

/**
 * 用户登陆
 *
 * @param userLoginRequest 用户登陆请求信息
 * @return 用户视图对象
 */
export const login = (userLoginRequest: UserLoginRequest): Promise<BaseResponse<LoginUserVO>> => {
  return request.post(requestPrefix + '/login', userLoginRequest)
}

/**
 * 获取当前登录用户信息
 *
 * @return 当前登录用户脱敏后的信息
 */
export const getCurrentLoginUser = (): Promise<BaseResponse<LoginUserVO>> => {
  return request.get(requestPrefix + '/get/login')
}

/**
 * 退出登录
 *
 * @return 业务操作结果
 */
export const userLogout = (): Promise<BaseResponse<boolean>> => {
  return request.post(requestPrefix + '/logout')
}

/**
 * 根据 userId 获取用户视图对象
 *
 * @param userId 用户 ID
 * @return 用户视图对象
 */
export const getUserVOById = (userId: string): Promise<BaseResponse<UserVO>> => {
  return request.get(requestPrefix + '/get/vo', {
    params: {
      userId,
    },
  })
}

// region 用户管理

/**
 * 新增用户
 *
 * @param userAddRequest 新增用户请求信息
 * @return 新增用户 ID
 */
export const addUser = (userAddRequest: UserAddRequest): Promise<BaseResponse<string>> => {
  return request.post(requestPrefix + '/add', userAddRequest)
}

/**
 * 根据 userId 获取用户信息（仅管理员）
 *
 * @param userId 用户 ID
 * @return 用户信息
 */
export const getUserById = (userId: string): Promise<BaseResponse<User>> => {
  return request.get(requestPrefix + '/get', {
    params: {
      userId,
    },
  })
}

/**
 * 删除用户
 *
 * @param deleteRequest 删除请求信息
 * @return 业务操作结果
 */
export const deleteUser = (deleteRequest: DeleteRequest): Promise<BaseResponse<boolean>> => {
  return request.delete(requestPrefix + '/delete', {
    data: {
      deleteRequest,
    },
  })
}

/**
 * 更新用户
 *
 * @param userUpdateRequest 用户更新请求信息
 * @return 业务操作结果
 */
export const updateUser = (
  userUpdateRequest: UserUpdateRequest,
): Promise<BaseResponse<boolean>> => {
  return request.put(requestPrefix + '/update', userUpdateRequest)
}

/**
 * 分页获取用户封装列表（仅管理员）
 *
 * @param userQueryRequest 查询请求参数
 * @return 用户视图封装列表
 */
export const listUserVOByPage = (
  userQueryRequest: UserQueryRequest,
): Promise<BaseResponse<Page<UserVO[]>>> => {
  return request.post(requestPrefix + '/list/page/vo', userQueryRequest)
}

// endregion
