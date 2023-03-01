package com.wt.mysqlmodel.model.dto.route;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xcx
 * @date
 */
@Data
public class RouteAddRequest implements Serializable {
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
}
