package com.chipswu.aiapplicationgenerator.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.chipswu.aiapplicationgenerator.common.BaseResponse;
import com.chipswu.aiapplicationgenerator.constant.UserConstant;
import com.chipswu.aiapplicationgenerator.exception.ErrorCode;
import com.chipswu.aiapplicationgenerator.exception.ThrowUtils;
import com.chipswu.aiapplicationgenerator.modal.dto.chathistory.ChatHistoryQueryRequest;
import com.chipswu.aiapplicationgenerator.modal.entity.ChatHistory;
import com.chipswu.aiapplicationgenerator.modal.entity.User;
import com.chipswu.aiapplicationgenerator.service.ChatHistoryService;
import com.chipswu.aiapplicationgenerator.service.UserService;
import com.chipswu.aiapplicationgenerator.utils.ResultUtils;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @Resource
    private UserService userService;

    /**
     * 查询某个应用的对话历史（分页 - 游标查询）
     *
     * @param appId          应用ID
     * @param pageSize       每页展示数量
     * @param lastCreateTime 最后一条对话记录的创建时间
     * @return 对话历史分页
     */
    @GetMapping("/app/{appId}")
    public BaseResponse<Page<ChatHistory>> listAppChatHistory(@PathVariable Long appId,
                                                              @RequestParam(defaultValue = "10") int pageSize,
                                                              @RequestParam(required = false) LocalDateTime lastCreateTime) {
        User loginUser = userService.getCurrentLoginUser();
        Page<ChatHistory> result = chatHistoryService.listAppChatHistoryByPage(appId, pageSize, lastCreateTime, loginUser);
        return ResultUtils.success(result);
    }

    // region 管理端

    /**
     * 查询所有对话历史（分页）【管理端】
     *
     * @param chatHistoryQueryRequest 查询请求
     * @return 对话历史分页
     */
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @PostMapping("/admin/list/page")
    public BaseResponse<Page<ChatHistory>> listAllChatHistoryByPageForAdmin(@RequestBody ChatHistoryQueryRequest chatHistoryQueryRequest) {
        ThrowUtils.throwIf(chatHistoryQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = chatHistoryQueryRequest.getPageNum();
        long pageSize = chatHistoryQueryRequest.getPageSize();
        // 查询数据
        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(chatHistoryQueryRequest);
        Page<ChatHistory> result = chatHistoryService.page(Page.of(pageNum, pageSize), queryWrapper);
        return ResultUtils.success(result);
    }

}
