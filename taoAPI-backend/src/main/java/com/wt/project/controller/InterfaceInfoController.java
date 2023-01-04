package com.wt.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.wt.project.annotation.AuthCheck;
import com.wt.project.common.*;
import com.wt.project.constant.CommonConstant;
import com.wt.project.constant.RedisConstant;
import com.wt.project.exception.BusinessException;
import com.wt.project.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.wt.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.wt.project.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.wt.project.model.dto.interfaceinfo.InterfaceInvokeRequest;
import com.wt.project.model.dto.post.PostAddRequest;
import com.wt.project.model.dto.post.PostQueryRequest;
import com.wt.project.model.dto.post.PostUpdateRequest;
import com.wt.project.model.entity.InterfaceInfo;
import com.wt.project.model.entity.Post;
import com.wt.project.model.entity.User;
import com.wt.project.model.enums.InterfaceInfoStatusEnum;
import com.wt.project.service.InterfaceInfoService;
import com.wt.project.service.UserService;
import com.wt.taoapiclientsdk.client.TaoAPIClient;
import com.wt.taoapiclientsdk.request.CommonRequest;
import com.wt.taoapiclientsdk.request.RestfulRequest;
import com.wt.taoapiclientsdk.response.CommonResult;
import io.swagger.models.auth.In;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private TaoAPIClient taoAPIClient;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // region 增删改查
    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo =  new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newPostId = interfaceInfo.getId();
        return ResultUtils.success(newPostId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                            HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfos = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfos);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
//        System.out.println("=============================================" + current + " " + size);
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // content 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> postPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(postPage);
    }

    // endregion
    @AuthCheck(mustRole = "ACX")
    @PostMapping("/online")
    public BaseResponse<Boolean> onlineInterface(@RequestBody IdRequest idRequest){
        if(idRequest == null || idRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        //判断是否存在
        InterfaceInfo oldInterfaceinfo = interfaceInfoService.getById(id);
        if(oldInterfaceinfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

//        com.wt.taoapiclientsdk.model.User user = new com.wt.taoapiclientsdk.model.User();
//        user.setName("wt");
//        String postResult = taoAPIClient.getUsernameByPost(user);
//        if(StringUtils.isBlank(postResult)){
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口验证失败");
//        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean update = interfaceInfoService.updateById(interfaceInfo);

        if(update){
            //todo 在redis添加接口url redisKey-url-接口id
            InterfaceInfo url = interfaceInfoService.query().select("url").eq("id", id).one();
            Boolean aBoolean = stringRedisTemplate
                    .opsForHash().putIfAbsent(RedisConstant.CACHE_INTEFACE_URL, url.getUrl(), String.valueOf(id));
            if (aBoolean){
                return ResultUtils.success(aBoolean);
            }else{
                log.error("redis服务异常 接口发布失败，接口id：{}", id);
                ResultUtils.error(ErrorCode.SYSTEM_ERROR,"发布失败");
            }
        }

        log.error("mysql服务异常 接口发布失败，接口id：{}", id);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"发布失败");
    }


    @AuthCheck(mustRole = "ACX")
    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineInterface(@RequestBody IdRequest idRequest){
        if(idRequest == null || idRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        //判断是否存在
        InterfaceInfo oldInterfaceinfo = interfaceInfoService.getById(id);
        if(oldInterfaceinfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean update = interfaceInfoService.updateById(interfaceInfo);
        if(update){
            //todo 在redis添加接口url redisKey-url-接口id
            InterfaceInfo url = interfaceInfoService.query().select("url").eq("id", id).one();
            Long delete = stringRedisTemplate.opsForHash().delete(RedisConstant.CACHE_INTEFACE_URL, url.getUrl());
            if(delete > 0){
                return ResultUtils.success(update);
            }else{
                log.error("redis服务异常 接口下线失败，接口id：{}", id);
                ResultUtils.error(ErrorCode.SYSTEM_ERROR,"下线失败");
            }
        }

        log.error("mysql服务异常 接口下线失败，接口id：{}", id);
        return ResultUtils.success(update);
    }

    @PostMapping("/ionvoke")
    public BaseResponse invokeInerface(@RequestBody InterfaceInvokeRequest interfaceInvokeRequest, HttpServletRequest request){
        if(interfaceInvokeRequest == null || interfaceInvokeRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = interfaceInvokeRequest.getId();
        //判断是否存在
        InterfaceInfo oldInterfaceinfo = interfaceInfoService.getById(id);
        if(oldInterfaceinfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        String requetParams = interfaceInvokeRequest.getRequetParams();
        Gson gson = new Gson();
        com.wt.taoapiclientsdk.model.User user = gson.fromJson(requetParams, com.wt.taoapiclientsdk.model.User.class);
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();

        RestfulRequest restfulRequest = new RestfulRequest();
        restfulRequest.setUrl(oldInterfaceinfo.getUrl());
        restfulRequest.setRequestParams(interfaceInvokeRequest.getRequetParams());
        TaoAPIClient taoAPIClient = new TaoAPIClient(accessKey, secretKey, loginUser.getId());
        CommonResult result = taoAPIClient.restfulPost(restfulRequest);
        return ResultUtils.success(result.getData());
    }
}
