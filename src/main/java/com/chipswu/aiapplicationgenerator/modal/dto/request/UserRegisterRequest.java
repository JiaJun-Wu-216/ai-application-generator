package com.chipswu.aiapplicationgenerator.modal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求类
 *
 * @author WuJiaJun
 */
@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 账号
     *
     * @titleName 账号
     */
    @Size(min = 4, message = "【账号】长度过短")
    @NotBlank(message = "【账号】不能为空")
    private String userAccount;

    /**
     * 密码
     *
     * @titleName 密码
     */
    @Size(min = 8, max = 16, message = "【密码】长度不能小于8位且不能超过16位")
    @NotBlank(message = "【密码】不能为空")
    private String userPassword;

    /**
     * 确认密码
     *
     * @titleName 确认密码
     */
    @Size(min = 8, max = 16, message = "【确认密码】长度不能小于8位且不能超过16位")
    @NotBlank(message = "【确认密码】不能为空")
    private String checkPassword;
}
