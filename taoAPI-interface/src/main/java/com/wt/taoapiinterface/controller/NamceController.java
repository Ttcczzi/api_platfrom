package com.wt.taoapiinterface.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.wt.model.User;
import com.wt.response.CommonResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xcx
 * @date
 * 查询名称
 */
@RestController
@RequestMapping("/w/getName")
public class NamceController {
    @GetMapping("/")
    public CommonResult getNameByGet(String name){
        return CommonResult.success("Get 你的名子是" + name);
    }
    @PostMapping("/")
    public CommonResult getNameByPost(@RequestParam String name){
        return CommonResult.success("Post 你的名子是" + name);
    }
    @PostMapping("/user")
    public CommonResult getUsernameByPost(@RequestBody User user, HttpServletRequest request){
        if(ObjectUtil.isNull(user)){
            throw new RuntimeException("参数错误");
        }
        return CommonResult.success("你的用户名是" + user.getName());
    }
}
