package com.chipswu.aiapplicationgenerator.modal.dto.app;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 应用更新请求类
 *
 * @author WuJiaJun
 */
@Data
public class AppUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 应用 ID
     */
    @Min(value = 1, message = "应用 ID 参数异常")
    @NotNull(message = "应用 ID 不能为空")
    private Long id;

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空")
    private String appName;

}
