package com.wt.project.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

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

    private String requetParams;

    private static final long serialVersionUID = 1L;
}