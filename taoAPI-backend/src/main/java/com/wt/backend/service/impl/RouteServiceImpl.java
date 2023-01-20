package com.wt.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wt.backend.mapper.RouteMapper;
import com.wt.backend.service.api.RouteService;
import com.wt.mysqlmodel.model.entity.Route;
import org.springframework.stereotype.Service;

/**
* @author TAO111
* @description 针对表【route】的数据库操作Service实现
* @createDate 2023-01-17 21:55:43
*/
@Service
public class RouteServiceImpl extends ServiceImpl<RouteMapper, Route>
    implements RouteService {

}




