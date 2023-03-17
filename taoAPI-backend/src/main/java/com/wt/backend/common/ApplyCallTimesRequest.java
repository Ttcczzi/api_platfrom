package com.wt.backend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xcx
 * @date
 */
@Data
public class ApplyCallTimesRequest implements Serializable {
    Long interfaceId;
    Integer count;
}
