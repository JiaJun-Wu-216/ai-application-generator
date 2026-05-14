package com.chipswu.aiapplicationgenerator.controller;

import com.chipswu.aiapplicationgenerator.common.BaseResponse;
import com.chipswu.aiapplicationgenerator.utils.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康相关
 *
 * @author WuJiaJun
 */
@RestController
public class HealthController {

    /**
     * 健康检测
     *
     * @return 健康值
     */
    @GetMapping("/health")
    public BaseResponse<String> healCheck() {
        return ResultUtils.success("application is healthy");
    }
}
