package com.wt.backend.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wt.backend.common.ErrorCode;
import com.wt.backend.common.ResultUtils;
import com.wt.backend.exception.BusinessException;
import com.wt.backend.mapper.InterfaceInfoMapper;
import com.wt.backend.service.api.InterfaceInfoService;
import com.wt.constant.RedisConstant;
import com.wt.mysqlmodel.model.entity.InterfaceInfo;
import com.wt.mysqlmodel.model.enums.InterfaceInfoStatusEnum;
import com.wt.project.service.DubboInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author TAO111
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2022-12-31 19:01:10
*/
@DubboService(interfaceClass = DubboInterfaceInfoService.class)
@Service
@Slf4j
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService, DubboInterfaceInfoService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
        String url = interfaceInfo.getUrl();
        Integer status = interfaceInfo.getStatus();
        String method = interfaceInfo.getMethod();
        Long userId = interfaceInfo.getUserId();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Override
    public Long existInterface(String url) {
        InterfaceInfo res = this.query().select("id").eq("url", url).one();
        if(ObjectUtil.isNull(res)){
            return -1L;
        }
        return res.getId();
    }

    @Override
    public boolean offline(long id) {
        return offlineInterface(id);
    }

    @Override
    public boolean offlineInterface(long id) {
        InterfaceInfo oldInterfaceinfo = this.getById(id);
        if(oldInterfaceinfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean update = this.updateById(interfaceInfo);
        if(update){
            //todo 在redis添加接口url redisKey-url-接口id
            InterfaceInfo url = this.query().select("url").eq("id", id).one();
            Long delete = stringRedisTemplate.opsForHash().delete(RedisConstant.CACHE_INTEFACE_URL, url.getUrl());
            if(delete > 0){
                return true;
            }else{
                log.error("redis服务异常 接口下线失败，接口id：{}", id);
                ResultUtils.error(ErrorCode.SYSTEM_ERROR,"下线失败");
            }
        }

        log.error("mysql服务异常 接口下线失败，接口id：{}", id);
        return false;
    }

}




