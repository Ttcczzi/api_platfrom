package com.wt.backend;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wt.backend.mapper")
@EnableDubbo
public class API_BackEnd {

    public static void main(String[] args) {
        SpringApplication.run(API_BackEnd.class, args);
    }

}
