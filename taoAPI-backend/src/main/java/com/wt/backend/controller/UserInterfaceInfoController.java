package com.wt.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wt.backend.annotation.AuthCheck;
import com.wt.backend.common.ApplyCallTimesRequest;
import com.wt.backend.service.api.UserInterfaceInfoService;
import com.wt.backend.common.BaseResponse;
import com.wt.backend.common.ErrorCode;
import com.wt.backend.common.ResultUtils;
import com.wt.constant.CommonConstant;
import com.wt.backend.exception.BusinessException;
import com.wt.backend.service.api.UserService;
import com.wt.mysqlmodel.model.dto.DeleteRequest;
import com.wt.mysqlmodel.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.wt.mysqlmodel.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.wt.mysqlmodel.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.wt.mysqlmodel.model.entity.User;
import com.wt.mysqlmodel.model.entity.UserInterfaceInfo;
import com.wt.mysqlmodel.model.vo.UserInterfaceInfoVO;
import com.wt.clientsdk.client.TaoAPIClient;
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


    /**
     * 创建
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
        UserInterfaceInfo UserInterfaceInfo = new UserInterfaceInfo();
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
     * 申请增加接口的请求次数
     * @param applyCallTimesRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = "ACX")
    @PostMapping("/application")
    public BaseResponse<String> applicationCallTimes(@RequestBody ApplyCallTimesRequest applyCallTimesRequest, HttpServletRequest request){
        if(applyCallTimesRequest == null || applyCallTimesRequest.getInterfaceId() < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(applyCallTimesRequest.getCount() > 500){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"请求次数过大");
        }

        User loginUser = userService.getLoginUser(request);

        boolean application = userInterfaceInfoService.application(applyCallTimesRequest, loginUser);

        return application?ResultUtils.success("申请成功"):ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 获取用户调用过的接口
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<List<UserInterfaceInfoVO>> getUserInterfaceInfo(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null || loginUser.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<UserInterfaceInfoVO> userInterfaceInfoVOList =
                userInterfaceInfoService.getInterfaceInfoByUserId(loginUser.getId());
        return ResultUtils.success(userInterfaceInfoVOList);
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
