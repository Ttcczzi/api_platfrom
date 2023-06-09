package com.wt.backend.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wt.backend.common.ApplyCallTimesRequest;
import com.wt.backend.service.api.InterfaceInfoService;
import com.wt.backend.service.api.UserInterfaceInfoService;
import com.wt.backend.common.ErrorCode;
import com.wt.constant.RedisConstant;
import com.wt.backend.exception.BusinessException;
import com.wt.backend.mapper.UserInterfaceInfoMapper;
import com.wt.mysqlmodel.model.entity.InterfaceInfo;
import com.wt.mysqlmodel.model.entity.User;
import com.wt.mysqlmodel.model.entity.UserInterfaceInfo;
import com.wt.mysqlmodel.model.vo.UserInterfaceInfoVO;
import com.wt.project.service.DubboUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TAO111
 * @description 针对表【user_interface_info(用户接口信息)】的数据库操作Service实现
 * @createDate 2023-01-02 15:17:08
 */
@DubboService(interfaceClass = DubboUserInterfaceInfoService.class)
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService, DubboUserInterfaceInfoService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    /**
     * 检差 用户接口是否都存在
     * @param userInterfaceInfo
     * @param add
     */
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
        if (userInterfaceInfo.getLeftNum() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求次数不可以小于0");
        }
    }

    /**
     * 总请求次数 +1，剩余请求次数 - 1
     * @param useId
     * @param interfaceInfoId
     * @return
     */
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
    public int getLeftNum(long useId, long interfaceId) {
        UserInterfaceInfo res = null;
        //判断用户是否是第一次使用该接口，若是，则在数据库生成基本数值，并返回
        String redisKey = RedisConstant.CACHE_INVOKEDINTERFACE + interfaceId;
        Long count = stringRedisTemplate.opsForSet().add(redisKey, String.valueOf(useId));
        if (count <= 0) {
            //不是第一次调用，在数据库查找剩余调用次数
            try{
                //可能有sql异常
                res = this.query().select("leftNum")
                        .eq("userId", useId)
                        .eq("interfaceInfoId", interfaceId)
                        .eq("status", 1)
                        .one();
            }catch (Exception e){
                log.error(e.getMessage());

                return -100;
            }

            if (ObjectUtil.isNull(res)) {
                return 0;
            }
            return res.getLeftNum();
        }
        //到这说明redis没有缓存，应该是第一次调用
        //todo 以防万一，去数据库检查
        res = new UserInterfaceInfo();
        res.setUserId(useId);
        res.setInterfaceInfoId(interfaceId);
        res.setTotalNum(0);
        res.setLeftNum(100);
        res.setStatus(1);
        this.save(res);
        return res.getLeftNum();
    }

    public List<UserInterfaceInfoVO> getInterfaceInfoByUserId(long userId){
        return userInterfaceInfoMapper.getInterfaceInfoByUserId(userId);
    }

    @Override
    public boolean application(ApplyCallTimesRequest applyCallTimesRequest, User loginUser) {
        long interfaceId = applyCallTimesRequest.getInterfaceId();

        InterfaceInfo interfaceInfo = interfaceInfoService.query().eq("id", interfaceId).one();

        if(interfaceInfo == null || interfaceInfo.getStatus() == 0){
            return false;
        }

        applyCallTimesRequest.getInterfaceId();
        UserInterfaceInfo info = null;
        try{
            info = this.
                    query().eq("interfaceInfoId", interfaceId).eq("userId", loginUser.getId()).one();
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
        if(info == null){
            info = new UserInterfaceInfo();
            info.setUserId(loginUser.getId());
            info.setInterfaceInfoId(interfaceId);
            info.setTotalNum(0);
            info.setInterfaceInfoId(applyCallTimesRequest.getInterfaceId());
            info.setStatus(1);

            return this.save(info);
        }else{
            return this.update().setSql("leftNum = leftNum + " + applyCallTimesRequest.getCount())
                    .eq("interfaceInfoId", interfaceId).eq("userId", loginUser.getId())
                    .update();
        }
    }
}




