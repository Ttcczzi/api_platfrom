package com.wt.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wt.project.model.entity.InterfaceInfo;
import com.wt.project.model.entity.Post;
import io.swagger.models.auth.In;


/**
* @author TAO111
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2022-12-31 19:01:10
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    Long existInterface(String url);
}
