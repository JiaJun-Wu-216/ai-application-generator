package com.chipswu.aiapplicationgenerator.service.impl;

import com.chipswu.aiapplicationgenerator.mapper.ChatHistoryMapper;
import com.chipswu.aiapplicationgenerator.modal.entity.ChatHistory;
import com.chipswu.aiapplicationgenerator.service.ChatHistoryService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 对话历史服务层接口实现类
 *
 * @author WuJiaJun
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory>  implements ChatHistoryService{

}
