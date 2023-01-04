package com.wt.project.service.impl;
import java.util.Date;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wt.project.common.ErrorCode;
import com.wt.project.exception.BusinessException;
import com.wt.project.mapper.InterfaceInfoMapper;
import com.wt.project.model.entity.InterfaceInfo;
import com.wt.project.model.entity.Post;
import com.wt.project.model.enums.PostGenderEnum;
import com.wt.project.model.enums.PostReviewStatusEnum;
import com.wt.project.service.InterfaceInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
* @author TAO111
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2022-12-31 19:01:10
*/
@DubboService
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService {

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

}




