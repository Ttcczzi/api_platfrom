package com.wt.backend.controller;


/**
 * 帖子接口
 *
 * @author yupi
 */

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wt.backend.common.BaseResponse;
import com.wt.backend.common.ResultUtils;
import com.wt.backend.mapper.UserInterfaceInfoMapper;
import com.wt.backend.service.api.InterfaceInfoService;
import com.wt.mysqlmodel.model.entity.InterfaceInfo;
import com.wt.mysqlmodel.model.entity.UserInterfaceInfo;
import com.wt.mysqlmodel.model.vo.InterfaceAnalyiszeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/analysize")
public class InterfaceAnalysizeController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Autowired
    private InterfaceInfoService interfaceInfoService;

    @RequestMapping("/top/interface")
    public BaseResponse<List<InterfaceAnalyiszeVO>> listInterfaceInvoked(){
        List<UserInterfaceInfo> list =
                userInterfaceInfoMapper.interfaceInfoInvoked(10);

        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();

        Map<Long, List<UserInterfaceInfo>> collect =
                list.stream().collect(Collectors.groupingBy(item -> item.getInterfaceInfoId()));
        wrapper.in("id", collect.keySet());

        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(wrapper);
        List<InterfaceAnalyiszeVO> res = interfaceInfoList.stream().map(interfaceInfo -> {
            InterfaceAnalyiszeVO interfaceAnalyiszeVO = new InterfaceAnalyiszeVO();
            BeanUtil.copyProperties(interfaceInfo, interfaceAnalyiszeVO);
            interfaceAnalyiszeVO.setTotalNum(collect.get(interfaceInfo.getId()).get(0).getTotalNum());
            return interfaceAnalyiszeVO;
        }).collect(Collectors.toList());

        return ResultUtils.success(res);
    }
}
