package com.wt.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.wt.project.annotation.AuthCheck;
import com.wt.project.common.*;
import com.wt.project.constant.CommonConstant;
import com.wt.project.exception.BusinessException;
import com.wt.project.model.dto.interfaceinfo.InterfaceInvokeRequest;
import com.wt.project.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.wt.project.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.wt.project.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.wt.project.model.entity.UserInterfaceInfo;
import com.wt.project.model.entity.User;
import com.wt.project.service.UserInterfaceInfoService;
import com.wt.project.service.UserService;
import com.wt.taoapiclientsdk.client.TaoAPIClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/UserInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private TaoAPIClient taoAPIClient;

    // region 增删改查
    /**
     * 创建
     *
     * @param UserInterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = "ACX")
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest UserInterfaceInfoAddRequest, HttpServletRequest request) {
        if (UserInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo UserInterfaceInfo =  new UserInterfaceInfo();
        BeanUtils.copyProperties(UserInterfaceInfoAddRequest, UserInterfaceInfo);
        // 校验
        userInterfaceInfoService.validUserInterfaceInfo(UserInterfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        UserInterfaceInfo.setUserId(loginUser.getId());
        boolean result = userInterfaceInfoService.save(UserInterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newPostId = UserInterfaceInfo.getId();
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
    @AuthCheck(mustRole = "ACX")
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean b = userInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param UserInterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = "ACX")
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest UserInterfaceInfoUpdateRequest,
                                            HttpServletRequest request) {
        if (UserInterfaceInfoUpdateRequest == null || UserInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo UserInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(UserInterfaceInfoUpdateRequest, UserInterfaceInfo);
        // 参数校验
        userInterfaceInfoService.validUserInterfaceInfo(UserInterfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = UserInterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean result = userInterfaceInfoService.updateById(UserInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo UserInterfaceInfo = userInterfaceInfoService.getById(id);
        return ResultUtils.success(UserInterfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param UserInterfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "ACX")
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listUserInterfaceInfo(UserInterfaceInfoQueryRequest UserInterfaceInfoQueryRequest) {
        UserInterfaceInfo UserInterfaceInfoQuery = new UserInterfaceInfo();
        if (UserInterfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(UserInterfaceInfoQueryRequest, UserInterfaceInfoQuery);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(UserInterfaceInfoQuery);
        List<UserInterfaceInfo> UserInterfaceInfos = userInterfaceInfoService.list(queryWrapper);
        return ResultUtils.success(UserInterfaceInfos);
    }

    /**
     * 分页获取列表
     *
     * @param UserInterfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = "ACX")
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest UserInterfaceInfoQueryRequest, HttpServletRequest request) {
        if (UserInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo UserInterfaceInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(UserInterfaceInfoQueryRequest, UserInterfaceInfoQuery);
        long current = UserInterfaceInfoQueryRequest.getCurrent();
        long size = UserInterfaceInfoQueryRequest.getPageSize();
//        System.out.println("=============================================" + current + " " + size);
        String sortField = UserInterfaceInfoQueryRequest.getSortField();
        String sortOrder = UserInterfaceInfoQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(UserInterfaceInfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> postPage = userInterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(postPage);
    }

}
