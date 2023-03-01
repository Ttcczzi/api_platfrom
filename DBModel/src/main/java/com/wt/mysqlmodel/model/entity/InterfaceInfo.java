package com.wt.mysqlmodel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息
 * @TableName interface_info
 */
@Data
public class InterfaceInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    protected Long id;

    /**
     * 接口名
     */
    protected String name;

    /**
     * 接口地址
     */
    protected String url;

    /**
     * 请求参数
     */
    protected String requestParams;

    /**
     * 请求头
     */
    protected String requestHeader;

    /**
     * 响应头
     */
    protected String responseHeader;

    /**
     * 描述
     */
    protected String description;

    /**
     * 接口状态
     */
    protected Integer status;

    /**
     * 
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 
     */
    protected Date createTime;

    /**
     * 
     */
    protected Date updateTime;

    /**
     * 请求类型
     */
    protected String method;

    /**
     * 创建人
     */
    protected Long userId;

    protected String returnType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}