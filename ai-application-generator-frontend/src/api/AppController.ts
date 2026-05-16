import request from '@/request'
import type {
  AppAddRequest,
  AppAdminUpdateRequest,
  AppDeployRequest,
  AppQueryRequest,
  AppUpdateRequest,
  AppVO,
} from '@/entity/App.ts'
import type {BaseResponse, DeleteRequest, Page} from '@/entity/Common.ts'

const requestPrefix = '/app'

/**
 * 创建应用
 *
 * @param appAddRequest 创建应用请求
 * @return 新增应用 ID
 */
export const addApp = (appAddRequest: AppAddRequest): Promise<BaseResponse<string>> => {
  return request.post(requestPrefix + '/add', appAddRequest)
}

/**
 * 更新应用（用户端）
 *
 * @param appUpdateRequest 更新请求
 * @return 业务操作结果
 */
export const updateApp = (appUpdateRequest: AppUpdateRequest): Promise<BaseResponse<boolean>> => {
  return request.put(requestPrefix + '/update', appUpdateRequest)
}

/**
 * 删除应用（用户端）
 *
 * @param deleteRequest 删除请求
 * @return 删除结果
 */
export const deleteAppOfUserClient = (
  deleteRequest: DeleteRequest,
): Promise<BaseResponse<boolean>> => {
  return request.delete(requestPrefix + '/delete', {
    data: {
      deleteRequest,
    },
  })
}

/**
 * 根据应用 ID 获取应用详情
 *
 * @param appId 应用 id
 * @return 应用详情
 */
export const getAppVOById = (appId: string): Promise<BaseResponse<AppVO>> => {
  return request.get(requestPrefix + '/get/vo', {
    params: {
      appId,
    },
  })
}

/**
 * 获取当前登陆用户创建的应用列表（分页）【用户端】
 *
 * @param appQueryRequest 应用查询请求信息
 * @return 当前登陆用户创建的应用列表
 */
export const listMyAppVOByPage = (
  appQueryRequest: AppQueryRequest,
): Promise<BaseResponse<Page<AppVO>>> => {
  return request.post(requestPrefix + '/my/list/page/vo', appQueryRequest)
}

/**
 * 获取精选应用列表（分页）【用户端】
 *
 * @param appQueryRequest 查询请求
 * @return 精选应用列表
 */
export const listGoodAppVOByPage = (
  appQueryRequest: AppQueryRequest,
): Promise<BaseResponse<Page<AppVO>>> => {
  return request.post(requestPrefix + '/good/list/page/vo', appQueryRequest)
}

/**
 * 应用部署
 *
 * @param appDeployRequest 部署请求
 * @return 部署 URL
 */
export const deployAppApi = (appDeployRequest: AppDeployRequest): Promise<BaseResponse<string>> => {
  return request.post(requestPrefix + '/deploy', appDeployRequest)
}

// region 应用管理

/**
 * 删除应用（管理端）
 *
 * @param deleteRequest 删除请求
 * @return 删除结果
 */
export const deleteAppByAdmin = (deleteRequest: DeleteRequest): Promise<BaseResponse<boolean>> => {
  return request.delete(requestPrefix + '/admin/delete', {
    data: {
      deleteRequest,
    },
  })
}

/**
 * 更新应用（管理端）
 *
 * @param appAdminUpdateRequest 更新请求
 * @return 业务操作结果
 */
export const updateAppByAdmin = (
  appAdminUpdateRequest: AppAdminUpdateRequest,
): Promise<BaseResponse<boolean>> => {
  return request.put(requestPrefix + '/admin/update', appAdminUpdateRequest)
}

/**
 * 管理员分页获取应用列表
 *
 * @param appQueryRequest 查询请求
 * @return 应用列表
 */
export const listAppVOByPageByAdmin = (
  appQueryRequest: AppQueryRequest,
): Promise<BaseResponse<Page<AppVO>>> => {
  return request.post(requestPrefix + '/admin/list/page/vo', appQueryRequest)
}

/**
 * 管理员根据应用 ID 获取应用详情
 *
 * @param appId 应用 ID
 * @return 应用详情
 */
export const getAppVOByIdByAdmin = (appId: string): Promise<BaseResponse<AppVO>> => {
  return request.get(requestPrefix + '/admin/get/vo', {
    params: {
      appId,
    },
  })
}

// endregion
