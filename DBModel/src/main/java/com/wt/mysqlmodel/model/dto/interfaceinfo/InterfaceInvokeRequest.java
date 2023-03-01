package com.wt.mysqlmodel.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Id请求
 *
 * @author yupi
 */
@Data
public class InterfaceInvokeRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private String requestParams;

    private Map queryParams;

    private static final long serialVersionUID = 1L;
}