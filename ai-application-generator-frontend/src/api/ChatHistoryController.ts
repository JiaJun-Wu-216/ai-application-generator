import request from '@/request'
import type {BaseResponse, Page} from '@/entity/Common.ts'
import type {ChatHistory, ChatHistoryQueryRequest} from '@/entity/ChatHistory.ts'

const requestPrefix = '/chatHistory'

/**
 * 查询某个应用的对话历史（分页 - 游标查询）
 *
 * @param appId          应用ID
 * @param pageSize       每页展示数量
 * @param lastCreateTime 最后一条对话记录的创建时间
 * @return 对话历史分页
 */
export const listAppChatHistory = (
  appId: string,
  pageSize: number,
  lastCreateTime: string,
): Promise<BaseResponse<Page<ChatHistory>>> => {
  return request.get(requestPrefix + `/app/${appId}`, {
    params: {
      pageSize,
      lastCreateTime,
    },
  })
}

/**
 * 查询所有对话历史（分页）【管理端】
 *
 * @param chatHistoryQueryRequest 查询请求
 * @return 对话历史分页
 */
export const listAllChatHistoryByPageForAdmin = (
  chatHistoryQueryRequest: ChatHistoryQueryRequest,
): Promise<BaseResponse<Page<ChatHistory>>> => {
  return request.post('/admin/list/page', chatHistoryQueryRequest)
}
