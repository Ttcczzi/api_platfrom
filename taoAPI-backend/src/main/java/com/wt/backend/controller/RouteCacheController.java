package com.wt.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wt.backend.common.BaseResponse;
import com.wt.backend.common.ErrorCode;
import com.wt.backend.common.GateWayConstant;
import com.wt.backend.common.ResultUtils;
import com.wt.backend.exception.BusinessException;
import com.wt.backend.mapper.RouteMapper;
import com.wt.backend.service.api.RouteService;
import com.wt.constant.CommonConstant;
import com.wt.mysqlmodel.model.dto.DeleteRequest;
import com.wt.mysqlmodel.model.dto.route.RouteAddRequest;
import com.wt.mysqlmodel.model.dto.route.RouteQueryRequest;
import com.wt.mysqlmodel.model.dto.route.RouteUpdateRequest;
import com.wt.mysqlmodel.model.entity.InterfaceInfo;
import com.wt.mysqlmodel.model.entity.Route;
import com.wt.mysqlmodel.model.vo.DBRouteDefination;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xcx
 * @date
 */
@RestController
@RequestMapping("/route")
@Slf4j
public class RouteCacheController {
    @Autowired
    private RouteService routeService;
    @Resource
    private RestTemplate restTemplate;

    @Resource
    private RouteMapper routeMapper;


    @PostMapping("/add")
    public BaseResponse<String> addCache(@RequestBody RouteAddRequest request){
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1. 先去查找有没有这个routeId，若有再查找是否已经存在该Predicate
        QueryWrapper<Route> wrapper = new QueryWrapper<>();
        wrapper.eq("routeId", request.getRouteid());
        wrapper.eq("predicate", request.getPredicate());
        Long count = routeMapper.selectCount(wrapper);
        if(count > 0){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"该断言已经存在，你可以选择修改");
        }

        Long aLong = routeMapper.selectCount(wrapper);
        Route route = new Route();
        // todo
        String routeId = request.getTargethost();
        route.setRouteid(request.getRouteid());
        route.setPredicate(request.getPredicate());
        route.setContent(request.getContent());
        route.setTargethost(request.getTargethost());
        boolean save = routeService.save(route);

        //todo 通知GateWay重新加载路由
        String res = "error";
        if(save){
            res = restTemplate.getForObject(GateWayConstant.GATEWAYIP, String.class);
        }
        return ResultUtils.success(res);
    }

    @PostMapping("/update")
    public BaseResponse<String> update(@RequestBody RouteUpdateRequest request){
        if(request == null || request.getId() == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1. 先去查找有没有这个routeId，若有再查找是否已经存在该Predicate
        Route oldRoute = routeService.getById(request.getId());
        if(oldRoute == null){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"修改目标不存在");
        }
        Route route = new Route();
        BeanUtils.copyProperties(request,route);
        boolean update = routeService.updateById(route);

        //todo 通知GateWay重新加载路由
        String res = "error";
        if(update){
            res = restTemplate.getForObject(GateWayConstant.GATEWAYIP, String.class);
        }
        return ResultUtils.success(res);
    }

    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestBody DeleteRequest request){
        int i = routeMapper.deleteById(request.getId());
        String res = "error";
        if(i > 0){
            res = restTemplate.getForObject(GateWayConstant.GATEWAYIP, String.class);
        }
        return ResultUtils.success("删除成功，" + res);
    }



    @GetMapping("/allRoutes")
    public BaseResponse<List<DBRouteDefination>> allRoutes(){
        List<DBRouteDefination> dbRouteDefinations =
                routeMapper.queryAllByRoutId();
        return ResultUtils.success(dbRouteDefinations);
    }


    @PostMapping("/routes")
    public BaseResponse<Page<Route>> routesByrouteId(@RequestBody RouteQueryRequest request){
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Route route = new Route();
        BeanUtils.copyProperties(request, route);
        long current = request.getCurrent();
        long size = request.getPageSize();

        String sortField = request.getSortField();
        String sortOrder = request.getSortOrder();

        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Route> queryWrapper = new QueryWrapper<>(route);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<Route> pages = routeService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(pages);
    }
}
