package com.wt.mysqlmodel.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xcx
 * @date
 */
@Data
public class UserInterfaceInfoVO implements Serializable {
    private String interfacName;

    /**
     * 总调用次数
     */
    private int totalNum;

    /**
     * 剩余调用次数
     */
    private int leftNum;

    /**
     * 调用状态
     */
    private Integer status;
}
