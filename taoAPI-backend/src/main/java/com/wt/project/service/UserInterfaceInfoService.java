package com.wt.project.service;

import com.wt.project.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TAO111
* @description 针对表【user_interface_info(用户接口信息)】的数据库操作Service
* @createDate 2023-01-02 15:17:08
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b);

    boolean invokeInterface(Long useId, Long interfaceInfoId);

    int getLeftNum(long useId, long interfaceId);
}
