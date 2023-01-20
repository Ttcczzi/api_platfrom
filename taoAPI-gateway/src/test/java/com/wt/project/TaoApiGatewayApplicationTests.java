package com.wt.project;

import com.wt.project.mapper.RouteMapper;
import com.wt.mysqlmodel.model.vo.DBRouteDefination;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class TaoApiGatewayApplicationTests {

    @Resource
    RouteMapper routeMapper;
    @Test
    void contextLoads() {
        List<DBRouteDefination> DBRouteDefinations =
                routeMapper.queryAllByRoutId();
        System.out.println(DBRouteDefinations);
    }

}
