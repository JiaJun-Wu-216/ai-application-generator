package com.chipswu.aiapplicationgenerator.common;

import com.chipswu.aiapplicationgenerator.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 全局响应封装类
 *
 * @param <T> 返回数据类型
 * @author WuJiaJun
 */
@Data
public class BaseResponse<T> implements Serializable {
    /**
     * 状态码
     *
     * @titleName 状态码
     */
    private int code;

    /**
     * 返回信息
     *
     * @titleName 返回信息
     */
    private String message;

    /**
     * 返回数据
     *
     * @titleName 返回数据
     */
    private T data;

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, "", data);
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), null);
    }

}


