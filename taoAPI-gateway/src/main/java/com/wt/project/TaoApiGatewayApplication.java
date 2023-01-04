package com.wt.project;

import com.wt.project.service.DemoService;
import com.wt.project.service.InterfaceInfoService;
import com.wt.project.service.UserInterfaceInfoService;
import com.wt.project.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDubbo
public class TaoApiGatewayApplication {

    @DubboReference
    public DemoService demoService;

    @DubboReference
    public UserService userService;

    @DubboReference
    public InterfaceInfoService interfaceInfoService;

    @DubboReference
    public UserInterfaceInfoService userInterfaceInfoService;

    public static TaoApiGatewayApplication application;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TaoApiGatewayApplication.class, args);
        application = context.getBean(TaoApiGatewayApplication.class);
//        String result = application.demoService.sayHello("world");
//        String sekByAckAndUname = application.userService.getSekByAckAndUname("wtacx", "wt");
//        System.out.println("result: " + result);
//        System.out.println("result2: " + sekByAckAndUname);
    }

}
