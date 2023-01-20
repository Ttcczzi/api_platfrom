package com.wt.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wt.mysqlmodel.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.yupi.project.model.domain.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




