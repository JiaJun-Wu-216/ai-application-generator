package com.chipswu.aiapplicationgenerator.controller;

import com.chipswu.aiapplicationgenerator.service.ChatHistoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对话历史相关控制层
 *
 * @author WuJiaJun
 */
@RestController
@RequestMapping("/chatHistory")
public class ChatHistoryController {

    @Resource
    private ChatHistoryService chatHistoryService;

}
