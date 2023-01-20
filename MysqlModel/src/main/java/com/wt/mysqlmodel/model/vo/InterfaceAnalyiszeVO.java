package com.wt.mysqlmodel.model.vo;

import com.wt.mysqlmodel.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xcx
 * @date
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceAnalyiszeVO extends InterfaceInfo {
    private Integer totalNum;
}
