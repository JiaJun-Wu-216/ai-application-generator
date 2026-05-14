package com.chipswu.aiapplicationgenerator.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.chipswu.aiapplicationgenerator.common.BaseResponse;
import com.chipswu.aiapplicationgenerator.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author WuJiaJun
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Sa-Token 未登录异常处理器
     *
     * @param e 未登录异常
     * @return 错误信息
     */
    @ExceptionHandler(NotLoginException.class)
    public BaseResponse<?> notLoginException(NotLoginException e) {
        log.error("NotLoginException", e);
        // 判断场景值，定制化异常信息
        String message = switch (e.getType()) {
            case NotLoginException.NOT_TOKEN -> "未能读取到有效 token";
            case NotLoginException.INVALID_TOKEN -> "token 无效";
            case NotLoginException.TOKEN_TIMEOUT -> "token 已过期";
            case NotLoginException.BE_REPLACED -> "当前账号已在别处登录";
            case NotLoginException.KICK_OUT -> "token 已被踢下线";
            case NotLoginException.TOKEN_FREEZE -> "token 已被冻结";
            case NotLoginException.NO_PREFIX -> "未按照指定前缀提交 token";
            default -> ErrorCode.NOT_LOGIN_ERROR.getMessage();
        };
        return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, message);
    }

    /**
     * Sa-Token 无权限异常处理器
     *
     * @param e 无权限异常
     * @return 错误信息
     */
    @ExceptionHandler(NotPermissionException.class)
    public BaseResponse<?> notPermissionExceptionHandler(NotPermissionException e) {
        log.error("NotPermissionException", e);
        return ResultUtils.error(ErrorCode.NO_AUTH_ERROR, ErrorCode.NO_AUTH_ERROR.getMessage());
    }

    /**
     * Sa-Token 无角色异常处理器
     *
     * @param e 无权限异常
     * @return 错误信息
     */
    @ExceptionHandler(NotRoleException.class)
    public BaseResponse<?> notRoleExceptionHandler(NotRoleException e) {
        log.error("NotRoleException", e);
        return ResultUtils.error(ErrorCode.NO_AUTH_ERROR, ErrorCode.NO_AUTH_ERROR.getMessage());
    }

    /**
     * 业务异常处理器
     *
     * @param e 业务异常信息
     * @return 处理结果
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    /**
     * 运行时异常处理器
     *
     * @param e 运行时异常信息
     * @return 处理结果
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}

