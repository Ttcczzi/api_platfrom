package com.wt.mysqlmodel.model.dto.route;

import com.wt.mysqlmodel.model.dto.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xcx
 * @date
 */
@Data
public class RouteQueryRequest extends PageRequest implements Serializable {

    private String routeid;

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
