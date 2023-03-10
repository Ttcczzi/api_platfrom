package com.wt.taoapiinterface.exception;


import com.wt.response.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author xcx
 * @date
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public CommonResult exceptionHandler(Exception e){
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(500);
        commonResult.setMessage("服务异常");

        log.error(e.getMessage());
        return commonResult;
    }

    @ExceptionHandler(ArgException.class)
    public CommonResult argException(ArgException e){
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(500);
        commonResult.setMessage(e.getMessage());

        log.error(e.getMessage());
        return commonResult;
    }
}
