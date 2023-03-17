package com.wt.project.common;

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
}
