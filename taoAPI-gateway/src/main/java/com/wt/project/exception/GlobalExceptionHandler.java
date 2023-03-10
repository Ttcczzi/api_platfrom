package com.wt.project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.sql.SQLException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(ConnectException.class)
    public String connectExceptionHandler(ConnectException e){

        log.error(e.getMessage());
        return "服务器连接异常";
    }
}