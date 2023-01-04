package com.wt.project.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.wt.project.constant.CommonConstant;
import com.wt.project.model.entity.User;
import com.wt.taoapiclientsdk.client.TaoAPIClient;
import com.wt.taoapiclientsdk.request.CommonRequest;
import com.wt.taoapiclientsdk.request.RestfulRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务测试
 *
 * @author yupi
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private TaoAPIClient taoAPIClient;
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    private UserService userService;


    @Test
    void tests(){
        User one = userService.query().select("secretKey")
                .eq("accessKey", "wtacx")
                .eq("userName", "wt").one();
        System.out.println(one);
    }

    @Test
    void testAddUser() {
        RestfulRequest request = new RestfulRequest();
        request.setUrl("http://localhost:8090/api/w/getName/user/");
        com.wt.taoapiclientsdk.model.User user =
                new com.wt.taoapiclientsdk.model.User();
        user.setName("wt");
        String params = JSONUtil.toJsonStr(user);
        request.setRequestParams(params);
        System.out.println(taoAPIClient.restfulPost(request));
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        boolean result = userService.updateById(user);
        Assertions.assertTrue(result);
    }

    @Test
    void testDeleteUser() {
        boolean result = userService.removeById(1L);
        Assertions.assertTrue(result);
    }

    @Test
    void testGetUser() {
        User user = userService.getById(1L);
        Assertions.assertNotNull(user);
    }

    @Test
    void userRegister() {
        String userAccount = "yupi";
        String userPassword = "";
        String checkPassword = "123456";
        try {
            long result = userService.userRegister(userAccount, userPassword, checkPassword);
            Assertions.assertEquals(-1, result);
            userAccount = "yu";
            result = userService.userRegister(userAccount, userPassword, checkPassword);
            Assertions.assertEquals(-1, result);
            userAccount = "yupi";
            userPassword = "123456";
            result = userService.userRegister(userAccount, userPassword, checkPassword);
            Assertions.assertEquals(-1, result);
            userAccount = "yu pi";
            userPassword = "12345678";
            result = userService.userRegister(userAccount, userPassword, checkPassword);
            Assertions.assertEquals(-1, result);
            checkPassword = "123456789";
            result = userService.userRegister(userAccount, userPassword, checkPassword);
            Assertions.assertEquals(-1, result);
            userAccount = "dogYupi";
            checkPassword = "12345678";
            result = userService.userRegister(userAccount, userPassword, checkPassword);
            Assertions.assertEquals(-1, result);
            userAccount = "yupi";
            result = userService.userRegister(userAccount, userPassword, checkPassword);
            Assertions.assertEquals(-1, result);
        } catch (Exception e) {

        }
    }
}