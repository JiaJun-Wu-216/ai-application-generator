package com.chipswu.aiapplicationgenerator.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.bean.BeanUtil;
import com.chipswu.aiapplicationgenerator.common.BaseResponse;
import com.chipswu.aiapplicationgenerator.common.DeleteRequest;
import com.chipswu.aiapplicationgenerator.constant.UserConstant;
import com.chipswu.aiapplicationgenerator.exception.BusinessException;
import com.chipswu.aiapplicationgenerator.exception.ErrorCode;
import com.chipswu.aiapplicationgenerator.exception.ThrowUtils;
import com.chipswu.aiapplicationgenerator.modal.dto.user.request.*;
import com.chipswu.aiapplicationgenerator.modal.entity.User;
import com.chipswu.aiapplicationgenerator.modal.vo.LoginUserVO;
import com.chipswu.aiapplicationgenerator.modal.vo.UserVO;
import com.chipswu.aiapplicationgenerator.service.UserService;
import com.chipswu.aiapplicationgenerator.utils.ResultUtils;
import com.mybatisflex.core.paginate.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户相关控制层
 *
 * @author WuJiaJun
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求信息
     * @return 新用户 ID
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long newUserId = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(newUserId);
    }

    /**
     * 用户登陆
     *
     * @param userLoginRequest 用户登陆请求信息
     * @return 用户视图对象
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户脱敏后的信息
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getCurrentLoginUser() {
        User loginUser = userService.getCurrentLoginUser();
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    /**
     * 退出登录
     *
     * @return 业务操作结果
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {
        boolean result = userService.userLogout();
        return ResultUtils.success(result);
    }

    /**
     * 根据 userId 获取用户视图对象
     *
     * @param userId 用户 ID
     * @return 用户视图对象
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(@RequestParam long userId) {
        User user = this.getUserById(userId).getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    // region 用户管理

    /**
     * 新增用户
     *
     * @param userAddRequest 新增用户请求信息
     * @return 新增用户 ID
     */
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        final String DEFAULT_PASSWORD = "12345678";
        String encryptPassword = userService.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 根据 userId 获取用户信息（仅管理员）
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @GetMapping("/get")
    public BaseResponse<User> getUserById(@RequestParam long userId) {
        ThrowUtils.throwIf(userId <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(userId);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 删除用户
     *
     * @param deleteRequest 删除请求信息
     * @return 业务操作结果
     */
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest 用户更新请求信息
     * @return 业务操作结果
     */
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @PutMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取用户封装列表（仅管理员）
     *
     * @param userQueryRequest 查询请求参数
     * @return 用户视图封装列表
     */
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long currentPage = userQueryRequest.getPageNum();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(Page.of(currentPage, pageSize),
                userService.getQueryWrapper(userQueryRequest));
        // 数据脱敏
        Page<UserVO> userVOPage = new Page<>(currentPage, pageSize, userPage.getTotalRow());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

    // endregion
}
