package com.wt.project.exception;

import com.wt.response.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.net.SocketException;
import java.sql.SQLException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(InterfaceConnectionException.class)
    public CommonResult ConnectExceptionHandler(InterfaceConnectionException e){
        CommonResult commonResult = new CommonResult();
        commonResult.setMessage(e.getMessage());

        return commonResult;
    }
}