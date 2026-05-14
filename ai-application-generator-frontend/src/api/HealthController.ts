import type {BaseResponse} from '@/entity/Common.ts'
import request from '@/request'

/**
 * 健康检测
 */
export const healCheck = (): Promise<BaseResponse<string>> => {
  return request.get('/health')
}
