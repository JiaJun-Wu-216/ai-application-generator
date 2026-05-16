package com.chipswu.aiapplicationgenerator.service;

import com.chipswu.aiapplicationgenerator.modal.dto.chathistory.ChatHistoryQueryRequest;
import com.chipswu.aiapplicationgenerator.modal.entity.ChatHistory;
import com.chipswu.aiapplicationgenerator.modal.entity.User;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史服务层接口
 *
 * @author WuJiaJun
 */
public interface ChatHistoryService extends IService<ChatHistory> {
    /**
     * 新增对话历史
     *
     * @param appId       应用 ID
     * @param message     对话信息
     * @param messageType 信息类型
     * @param userId      用户 ID
     * @return 业务操作结果
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * 根据应用 ID 删除对话历史
     *
     * @param appId 应用 ID
     * @return 业务操作结果
     */
    boolean deleteByAppId(Long appId);

    /**
     * 查询某个应用的对话历史（分页 - 游标查询）
     *
     * @param appId          应用ID
     * @param pageSize       每页展示数量
     * @param lastCreateTime 最后一条对话记录的创建时间
     * @return 对话历史分页
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);

    /**
     * 获取对话历史信息查询包装类
     *
     * @param chatHistoryQueryRequest 对话历史查询请求信息
     * @return 对话历史信息查询包装类
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    /**
     * 加载对话历史到内存
     *
     * @param appId      应用 ID
     * @param chatMemory 对话记忆容器
     * @param maxCount   最大加载信息条数
     * @return 加载成功的条数
     */
    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);
}
