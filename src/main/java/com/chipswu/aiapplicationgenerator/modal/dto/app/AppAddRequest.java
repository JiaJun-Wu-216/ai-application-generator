package com.chipswu.aiapplicationgenerator.modal.dto.app;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 添加应用请求类
 *
 * @author WuJiaJun
 */
@Data
public class AppAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 应用初始化的 prompt
     */
    @NotBlank(message = "初始化 prompt 不能为空")
    private String initPrompt;

}
