package com.wt.project.model.dto.interfaceinfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.wt.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *
 * @author yupi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

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

    /**
     * 创建人
     */
    private Long userId;


}