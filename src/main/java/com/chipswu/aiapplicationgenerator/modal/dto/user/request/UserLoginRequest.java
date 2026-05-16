package com.chipswu.aiapplicationgenerator.modal.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登陆请求信息
 *
 * @author WuJiaJun
 */
@Data
public class UserLoginRequest implements Serializable {

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
}
