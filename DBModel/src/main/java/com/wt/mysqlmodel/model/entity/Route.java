package com.wt.mysqlmodel.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName route
 */
@TableName(value ="route")
@Data
public class Route implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String routeid;

    /**
     * 
     */
    private String predicate;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private String targethost;

    /**
     * 
     */
    @TableLogic
    private Integer isdelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}