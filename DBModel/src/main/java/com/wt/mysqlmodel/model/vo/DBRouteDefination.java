package com.wt.mysqlmodel.model.vo;

import com.wt.mysqlmodel.model.entity.Route;
import lombok.Data;

import java.util.List;

/**
 * @author xcx
 * @date
 */
@Data
public class DBRouteDefination {
    String routeId;
    List<Route> routes;
}
