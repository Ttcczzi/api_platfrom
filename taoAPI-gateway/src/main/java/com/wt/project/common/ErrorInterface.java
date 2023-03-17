package com.wt.project.common;

import cn.hutool.json.JSONUtil;
import com.wt.project.TaoApiGatewayApplication;
import com.wt.project.threadpool.ThreadPool;
import com.wt.response.CommonResult;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xcx
 * @date
 */
public class ErrorInterface {
    //todo 想一个好的策略
    private static ConcurrentHashMap<Long,Integer> errorInterfaceMap = new ConcurrentHashMap<>();

    public static int getErrorNums(Long interfaceId){
        return errorInterfaceMap.getOrDefault(interfaceId, 0);
    }

    public static void addErrorNums(Long interfaceId){
        errorInterfaceMap.put(interfaceId, errorInterfaceMap.getOrDefault(interfaceId, 0) + 1);
    }

    public static void clearErrorNums(Long interfaceId){
        errorInterfaceMap.remove(interfaceId);
    }

    public static void checkErrorCount(long interfaceId){
        int count = ErrorInterface.getErrorNums(interfaceId);
        ErrorInterface.addErrorNums(interfaceId);
        //若失败次数连续超过 10，强制下线
        if(count >= 100){
            ThreadPool.executor.submit(() -> {
                TaoApiGatewayApplication.application.dubboInterfaceInfoService.offline(interfaceId);
                ErrorInterface.clearErrorNums(interfaceId);
            });
        }
    }

    public static byte[] remoteSystemErrorHandler(long interfaceId){
        checkErrorCount(interfaceId);
        CommonResult commonResult = new CommonResult();

        String msg = "服务端发生异常，请稍后再试";
        commonResult.setMessage(msg);

        return JSONUtil.toJsonStr(commonResult).getBytes(StandardCharsets.UTF_8);
    }
}
