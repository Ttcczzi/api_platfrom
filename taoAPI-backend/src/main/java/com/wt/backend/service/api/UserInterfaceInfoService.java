package com.wt.backend.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wt.backend.model.entity.UserInterfaceInfo;

/**
* @author TAO111
* @description 针对表【user_interface_info(用户接口信息)】的数据库操作Service
* @createDate 2023-01-02 15:17:08
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo>{

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b);

}
