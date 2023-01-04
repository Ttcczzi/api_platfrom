package com.wt.project.service;

public interface UserInterfaceInfoService {
    //修改用户调用次数
    boolean invokeInterface(Long useId, Long interfaceInfoId);
    //查看用户调用次数
    int getLeftNum(long useId, long interfaceId);
}
