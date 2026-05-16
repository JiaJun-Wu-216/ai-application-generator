package com.chipswu.aiapplicationgenerator.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.chipswu.aiapplicationgenerator.common.BaseResponse;
import com.chipswu.aiapplicationgenerator.common.DeleteRequest;
import com.chipswu.aiapplicationgenerator.constant.UserConstant;
import com.chipswu.aiapplicationgenerator.exception.BusinessException;
import com.chipswu.aiapplicationgenerator.exception.ErrorCode;
import com.chipswu.aiapplicationgenerator.exception.ThrowUtils;
import com.chipswu.aiapplicationgenerator.modal.dto.app.*;
import com.chipswu.aiapplicationgenerator.modal.entity.App;
import com.chipswu.aiapplicationgenerator.modal.entity.User;
import com.chipswu.aiapplicationgenerator.modal.vo.AppVO;
import com.chipswu.aiapplicationgenerator.service.AppService;
import com.chipswu.aiapplicationgenerator.service.UserService;
import com.chipswu.aiapplicationgenerator.utils.ResultUtils;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 应用相关控制层
 *
 * @author WuJiaJun
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Resource
    private AppService appService;

    @Resource
    private UserService userService;

    /**
     * 创建应用
     *
     * @param appAddRequest 创建应用请求
     * @return 新增应用 ID
     */
    @PostMapping("/add")
    public BaseResponse<Long> addApp(@Valid @RequestBody AppAddRequest appAddRequest) {
        ThrowUtils.throwIf(appAddRequest == null, ErrorCode.PARAMS_ERROR);
        String initPrompt = appAddRequest.getInitPrompt();
        Long appId = appService.addApp(initPrompt);
        return ResultUtils.success(appId);
    }

    /**
     * 更新应用（用户端）
     *
     * @param appUpdateRequest 更新请求
     * @return 业务操作结果
     */
    @PutMapping("/update")
    public BaseResponse<Boolean> updateApp(@Valid @RequestBody AppUpdateRequest appUpdateRequest) {
        if (appUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long appId = appUpdateRequest.getId();
        String appName = appUpdateRequest.getAppName();
        User loginUser = userService.getCurrentLoginUser();
        appService.updateApp(appId, appName, loginUser);
        return ResultUtils.success(true);
    }

    /**
     * 删除应用（用户端）
     *
     * @param deleteRequest 删除请求
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deleteApp(@Valid @RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long appId = deleteRequest.getId();
        User loginUser = userService.getCurrentLoginUser();
        Boolean result = appService.deleteApp(appId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 根据应用 ID 获取应用详情
     *
     * @param appId 应用 id
     * @return 应用详情
     */
    @GetMapping("/get/vo")
    public BaseResponse<AppVO> getAppVOById(@RequestParam long appId) {
        ThrowUtils.throwIf(appId < 1, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类（包含用户信息）
        return ResultUtils.success(appService.getAppVO(app));
    }

    /**
     * 获取当前登陆用户创建的应用列表（分页）【用户端】
     *
     * @param appQueryRequest 应用查询请求信息
     * @return 当前登陆用户创建的应用列表
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<AppVO>> listMyAppVOByPage(@RequestBody AppQueryRequest appQueryRequest) {
        User loginUser = userService.getCurrentLoginUser();
        Page<AppVO> result = appService.listMyAppVOByPage(appQueryRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取精选应用列表（分页）【用户端】
     *
     * @param appQueryRequest 查询请求
     * @return 精选应用列表
     */
    @PostMapping("/good/list/page/vo")
    public BaseResponse<Page<AppVO>> listGoodAppVOByPage(@RequestBody AppQueryRequest appQueryRequest) {
        Page<AppVO> result = appService.listGoodAppVOByPage(appQueryRequest);
        return ResultUtils.success(result);
    }

    /**
     * 应用聊天生成代码（流式 SSE）
     *
     * @param appId   应用 ID
     * @param message 用户消息
     * @return 生成结果流
     */
    @GetMapping(value = "/chat/gen/code", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatToGenCode(@RequestParam Long appId,
                                                       @RequestParam String message
    ) {
        // 参数校验
        ThrowUtils.throwIf(appId == null, ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        ThrowUtils.throwIf(appId < 1, ErrorCode.PARAMS_ERROR, "应用 ID 无效");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "用户消息不能为空");
        // 获取当前登录用户
        User loginUser = userService.getCurrentLoginUser();
        // 调用服务生成代码（流式）
        Flux<String> contentFlux = appService.chatToGenCode(appId, message, loginUser);
        return contentFlux
                .map(chunk -> {
                    Map<String, String> wrapper = Map.of("d", chunk);
                    String jsonData = JSONUtil.toJsonStr(wrapper);
                    return ServerSentEvent.<String>builder()
                            .data(jsonData)
                            .build();
                })
                .concatWith(Mono.just(
                        // 发送结束事件
                        ServerSentEvent.<String>builder()
                                .event("done")
                                .build()
                ));
    }

    /**
     * 应用部署
     *
     * @param appDeployRequest 部署请求
     * @return 部署 URL
     */
    @PostMapping("/deploy")
    public BaseResponse<String> deployApp(@RequestBody AppDeployRequest appDeployRequest) {
        ThrowUtils.throwIf(appDeployRequest == null, ErrorCode.PARAMS_ERROR);
        Long appId = appDeployRequest.getAppId();
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        // 获取当前登录用户
        User loginUser = userService.getCurrentLoginUser();
        // 调用服务部署应用
        String deployUrl = appService.deployApp(appId, loginUser);
        return ResultUtils.success(deployUrl);
    }



    // region 应用管理

    /**
     * 删除应用（管理端）
     *
     * @param deleteRequest 删除请求
     * @return 删除结果
     */
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @DeleteMapping("/admin/delete")
    public BaseResponse<Boolean> deleteAppByAdmin(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        App oldApp = appService.getById(id);
        ThrowUtils.throwIf(oldApp == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = appService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 更新应用（管理端）
     *
     * @param appAdminUpdateRequest 更新请求
     * @return 业务操作结果
     */
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @PutMapping("/admin/update")
    public BaseResponse<Boolean> updateAppByAdmin(@Valid @RequestBody AppAdminUpdateRequest appAdminUpdateRequest) {
        if (appAdminUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long appId = appAdminUpdateRequest.getId();
        // 判断是否存在
        App oldApp = appService.getById(appId);
        ThrowUtils.throwIf(oldApp == null, ErrorCode.NOT_FOUND_ERROR);
        App app = new App();
        BeanUtil.copyProperties(appAdminUpdateRequest, app);
        // 设置编辑时间
        app.setEditTime(LocalDateTime.now());
        boolean result = appService.updateById(app);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 管理员分页获取应用列表
     *
     * @param appQueryRequest 查询请求
     * @return 应用列表
     */
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @PostMapping("/admin/list/page/vo")
    public BaseResponse<Page<AppVO>> listAppVOByPageByAdmin(@RequestBody AppQueryRequest appQueryRequest) {
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = appQueryRequest.getPageNum();
        long pageSize = appQueryRequest.getPageSize();
        QueryWrapper queryWrapper = appService.getQueryWrapper(appQueryRequest);
        Page<App> appPage = appService.page(Page.of(pageNum, pageSize), queryWrapper);
        // 数据封装
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        List<AppVO> appVOList = appService.getAppVOList(appPage.getRecords());
        appVOPage.setRecords(appVOList);
        return ResultUtils.success(appVOPage);
    }

    /**
     * 管理员根据应用 ID 获取应用详情
     *
     * @param appId 应用 ID
     * @return 应用详情
     */
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @GetMapping("/admin/get/vo")
    public BaseResponse<AppVO> getAppVOByIdByAdmin(@RequestParam long appId) {
        ThrowUtils.throwIf(appId < 1, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(appService.getAppVO(app));
    }

    // endregion
}
