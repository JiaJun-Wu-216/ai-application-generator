package com.chipswu.aiapplicationgenerator.modal.dto.app;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 管理员应用更新请求类
 *
 * @author WuJiaJun
 */
@Data
public class AppAdminUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Min(value = 1, message = "应用 ID 参数异常")
    @NotNull(message = "应用 ID 不能为空")
    private Long id;

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空")
    private String appName;

    /**
     * 应用封面
     */
    @NotBlank(message = "应用封面不能为空")
    private String cover;

    /**
     * 优先级
     */
    @Min(value = 0, message = "优先级不能为负数")
    @NotNull(message = "优先级不能为空")
    private Integer priority;

}
