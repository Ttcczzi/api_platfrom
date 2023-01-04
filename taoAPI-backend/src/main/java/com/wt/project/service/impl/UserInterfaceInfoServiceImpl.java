package com.wt.project.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.shaded.io.grpc.NameResolver;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wt.project.common.ErrorCode;
import com.wt.project.constant.RedisConstant;
import com.wt.project.exception.BusinessException;
import com.wt.project.model.entity.User;
import com.wt.project.model.entity.UserInterfaceInfo;
import com.wt.project.service.UserInterfaceInfoService;
import com.wt.project.mapper.UserInterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author TAO111
* @description 针对表【user_interface_info(用户接口信息)】的数据库操作Service实现
* @createDate 2023-01-02 15:17:08
*/
@DubboService
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建时，所有参数必须非空
        if (add) {
            if (userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
            }
        }
        if(userInterfaceInfo.getLeftNum() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求次数不可以小于0");
        }
    }

    @Override
    public boolean invokeInterface(Long useId, Long interfaceInfoId) {
        UpdateWrapper<UserInterfaceInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("userId", useId);
        wrapper.eq("interfaceInfoId", interfaceInfoId);
        wrapper.gt("leftNum", 0);
        wrapper.setSql("leftNum = leftNUm - 1, totalNum = totalNum + 1");
        return this.update(wrapper);
    }

    @Override
    public int getLeftNum(long useId, long interfaceId){
        UserInterfaceInfo res = null;
        //判断用户是否是第一次使用该接口，若是，则在数据库生成基本数值，并返回
        String redisKey = RedisConstant.CACHE_INVOKEDINTERFACE + interfaceId;
        Long count = stringRedisTemplate.opsForSet().add(redisKey, String.valueOf(useId));
        if(count <= 0){
            //不是第一次调用，在数据库查找剩余调用次数
             res = this.query().select("leftNum")
                    .eq("userId", useId)
                    .eq("interfaceInfoId", interfaceId)
                     .eq("status", 1)
                    .one();
            if(ObjectUtil.isNull(res)){
                return 0;
            }
        }
        res = new UserInterfaceInfo();
        res.setUserId(useId);
        res.setInterfaceInfoId(interfaceId);
        res.setTotalNum(0);
        res.setLeftNum(100);
        res.setStatus(1);
        this.save(res);
        return res.getLeftNum();
    }
}




