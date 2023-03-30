package com.wt.backend;

import com.wt.backend.service.impl.InterfaceInfoServiceImpl;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.wt.backend.mapper")
@EnableDubbo

public class API_BackEnd {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(API_BackEnd.class, args);

    }

}
