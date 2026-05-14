package com.chipswu.aiapplicationgenerator.common;

import lombok.Data;

/**
 * 通用分页请求类
 *
 * @author WuJiaJun
 */
@Data
public class PageRequest {

    /**
     * 当前页码
     */
    private int current = 1;

    /**
     * 每页展示数量
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";
}
