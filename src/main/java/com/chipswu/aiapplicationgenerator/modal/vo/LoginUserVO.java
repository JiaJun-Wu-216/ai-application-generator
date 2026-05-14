package com.chipswu.aiapplicationgenerator.modal.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登陆用户视图类
 *
 * @author WuJiaJun
 */
@Data
public class LoginUserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3299986974153782004L;

    /**
     * 用户 ID
     *
     * @titleName 用户 ID
     */
    private Long id;

    /**
     * 账号
     *
     * @titleName 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     *
     * @titleName 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     *
     * @titleName 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     *
     * @titleName 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     *
     * @titleName 用户角色
     */
    private String userRole;

    /**
     * 账户状态 0：封禁 1：正常
     */
    private Integer accountStatus;

    /**
     * 创建时间
     *
     * @titleName 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     *
     * @titleName 更新时间
     */
    private LocalDateTime updateTime;
}
