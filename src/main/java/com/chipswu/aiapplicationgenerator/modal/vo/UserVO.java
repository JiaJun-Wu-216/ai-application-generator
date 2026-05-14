package com.chipswu.aiapplicationgenerator.modal.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户视图类
 *
 * @author WuJiaJun
 */
@Data
public class UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    /**
     * 账户状态
     */
    private Integer accountStatus;

    /**
     * 账户状态描述
     */
    private String statusDescription;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
