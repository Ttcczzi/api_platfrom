package com.wt.backend.controller;

import com.wt.backend.common.BaseResponse;
import com.wt.backend.common.ErrorCode;
import com.wt.backend.common.GateWayConstant;
import com.wt.backend.common.ResultUtils;
import com.wt.constant.RedisConstant;
import com.wt.mysqlmodel.model.dto.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

/**
 * @author xcx
 * @date
 */
@RestController
@RequestMapping("/wbusers")
@Slf4j
public class WhiteBlackUsersController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("/add")
    public BaseResponse<String> add(@RequestParam  String host){
        Long add = stringRedisTemplate.opsForSet().add(RedisConstant.CACE_WB_USERS, host);
        if(add <= 0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"该地址可能已经存在");
        }

        String res =  restTemplate.getForObject(GateWayConstant.GATEWAYIP, String.class);
        return ResultUtils.success(res);
    }

    @GetMapping("/delete")
    public BaseResponse<String> delete(@RequestParam String host){
        Long remove = stringRedisTemplate.opsForSet().remove(RedisConstant.CACE_WB_USERS, host);
        if(remove <= 0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"该地址可能不存在");
        }

        String res =  restTemplate.getForObject(GateWayConstant.GATEWAYIP, String.class);
        return ResultUtils.success(res);
    }

    @GetMapping("/queryAllWB")
    public BaseResponse<Set<String>> queryAll(){
        Set<String> members = stringRedisTemplate.opsForSet().members(RedisConstant.CACE_WB_USERS);
        return ResultUtils.success(members);
    }

}
