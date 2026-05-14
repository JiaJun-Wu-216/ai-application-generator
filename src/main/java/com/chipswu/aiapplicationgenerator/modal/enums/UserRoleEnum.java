package com.chipswu.aiapplicationgenerator.modal.enums;

import cn.hutool.core.util.ObjUtil;
import com.mybatisflex.annotation.EnumValue;
import lombok.Getter;

/**
 * 用户角色枚举
 *
 * @author WuJiaJun
 */
@Getter
public enum UserRoleEnum {

    USER("用户", "user"),
    ADMIN("管理员", "admin");

    /**
     * 描述
     */
    private final String text;

    /**
     * 状态值
     */
    @EnumValue
    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static UserRoleEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
