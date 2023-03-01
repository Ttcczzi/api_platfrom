package com.wt.mysqlmodel.model.dto.interfaceinfo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {
    /**
     * 接口名
     */

    private String name;

    /**
     * 接口地址
     */
    private String url;


    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 描述
     */
    private String description;

    /**
     * 请求类型
     */
    private String method;

    private String returnType;
}