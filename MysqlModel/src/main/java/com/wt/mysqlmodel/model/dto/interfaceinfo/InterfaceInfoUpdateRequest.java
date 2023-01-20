package com.wt.mysqlmodel.model.dto.interfaceinfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {
    private Long id;
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
     * 接口状态
     */
    private Integer status;


    /**
     * 请求类型
     */
    private String method;

}