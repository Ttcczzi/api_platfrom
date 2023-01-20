package com.wt.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wt.mysqlmodel.model.entity.UserInterfaceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author TAO111
* @description 针对表【user_interface_info(用户接口信息)】的数据库操作Mapper
* @createDate 2023-01-02 15:17:08
* @Entity com.wt.project.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    public List<UserInterfaceInfo> interfaceInfoInvoked(@Param("limit") int limit);
}




