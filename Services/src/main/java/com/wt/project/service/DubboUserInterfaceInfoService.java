package com.wt.project.service;



/**
* @author TAO111
* @description 针对表【user_interface_info(用户接口信息)】的数据库操作Service
* @createDate 2023-01-02 15:17:08
*/
public interface DubboUserInterfaceInfoService {
    boolean invokeInterface(Long useId, Long interfaceInfoId);
    int getLeftNum(long useId, long interfaceId);
}
