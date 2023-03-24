package com.wt.backend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xcx
 * @date
 */
@Data
public class GateWayRequest {
    private String address;

    private String refreshPath;

    private String reLoadPath;
}
