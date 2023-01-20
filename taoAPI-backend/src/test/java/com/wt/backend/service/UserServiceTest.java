package com.wt.backend.service;

import cn.hutool.json.JSONUtil;
import com.wt.backend.service.api.UserInterfaceInfoService;
import com.wt.backend.service.api.UserService;
import com.wt.mysqlmodel.model.entity.User;
import com.wt.request.CommonRequest;
import com.wt.request.RestfulRequest;
import com.wt.taoapiclientsdk.client.TaoAPIClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;

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
        CommonRequest request = new CommonRequest();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("te","te");
        request.setRequestParams(hashMap);
        request.setUrl("http://localhost:8090/api/t/test");
        System.out.println(taoAPIClient.get(request));
    }

    @Test
    void testAddUser() {
        RestfulRequest request = new RestfulRequest();
        request.setUrl("http://localhost:8090/api/w/getName/user/");
        com.wt.model.User user =
                new com.wt.model.User();
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