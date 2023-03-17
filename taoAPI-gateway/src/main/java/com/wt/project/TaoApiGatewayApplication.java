package com.wt.project;


import com.wt.project.service.DubboDemoService;
import com.wt.project.service.DubboInterfaceInfoService;
import com.wt.project.service.DubboUserInterfaceInfoService;
import com.wt.project.service.DubboUserService;
import com.wt.project.threadpool.ThreadPool;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PreDestroy;

@SpringBootApplication
@EnableDubbo
public class TaoApiGatewayApplication {

    @DubboReference
    public DubboDemoService demoService;
    @DubboReference
    public DubboUserService dubboUserService;

    @DubboReference
    public DubboInterfaceInfoService dubboInterfaceInfoService;

    @DubboReference
    public DubboUserInterfaceInfoService dubboUserInterfaceInfoService;

    public static TaoApiGatewayApplication application;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TaoApiGatewayApplication.class, args);
        application = context.getBean(TaoApiGatewayApplication.class);
//        String result = application.demoService.sayHello("world");
//        String sekByAckAndUname = application.userService.getSekByAckAndUname("wtacx", "wt");
//        System.out.println("result: " + result);
//        System.out.println("result2: " + sekByAckAndUname);
    }

    @PreDestroy
    public void exit(){
        ThreadPool.executor.shutdown();
    }
}
