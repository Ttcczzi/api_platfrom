package com.wt.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wt.mysqlmodel.model.entity.Route;
import com.wt.mysqlmodel.model.vo.DBRouteDefination;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author TAO111
* @description 针对表【route】的数据库操作Mapper
* @createDate 2023-01-17 21:55:43
* @Entity generator.domain.Route
*/
@Mapper
public interface RouteMapper extends BaseMapper<Route> {
    List<DBRouteDefination> queryAllByRoutId();
}




