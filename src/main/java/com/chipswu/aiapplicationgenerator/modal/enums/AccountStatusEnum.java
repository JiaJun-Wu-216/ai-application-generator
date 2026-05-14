package com.chipswu.aiapplicationgenerator.modal.enums;

import cn.hutool.core.util.ObjUtil;
import com.mybatisflex.annotation.EnumValue;
import lombok.Getter;


/**
 * 账户状态枚举
 *
 * @author WuJiaJun
 */
@Getter
public enum AccountStatusEnum {
    BANED("已封禁", 0),
    NORMAL("正常", 1),
    ;

    /**
     * 描述
     */
    private final String text;

    /**
     * 状态值
     */
    @EnumValue
    private final Integer value;

    AccountStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static AccountStatusEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (AccountStatusEnum anEnum : AccountStatusEnum.values()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
