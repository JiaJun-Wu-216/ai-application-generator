package com.chipswu.aiapplicationgenerator.modal.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户更新请求类
 *
 * @author WuJiaJun
 */
@Data
public class UserUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;
}
