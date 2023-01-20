package com.wt.project.controller;


import com.wt.mysqlmodel.model.entity.Route;
import com.wt.project.route.Refrash;
import com.wt.project.service.api.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xcx
 * @date
 */
@RestController
@RequestMapping("/route")
@Slf4j
public class RouteCacheController {
    @Autowired
    private Refrash refrash;

    @GetMapping("/refrash")
    public String refrash(){
        log.info("收到refrash的请求");
        refrash.refrash();
        return "已重新加载路由";
    }

}
